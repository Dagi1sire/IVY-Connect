package com.example.data.repository

import com.example.data.local.IvyDao
import com.example.data.local.IvySampleData
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.data.model.IvyNotificationEntity
import com.example.data.model.IvyResource
import com.example.data.model.UserAccountEntity
import com.example.data.sync.IvySyncManager
import com.example.data.sync.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class IvyRepository(
  private val dao: IvyDao,
  val syncManager: IvySyncManager = IvySyncManager(dao)
) {

  val activeAnnouncements: Flow<List<AnnouncementEntity>> = dao.getActiveAnnouncements()
  val allAnnouncements: Flow<List<AnnouncementEntity>> = dao.getAllAnnouncements()
  val allEvents: Flow<List<EventEntity>> = dao.getAllEvents()
  val allNotifications: Flow<List<IvyNotificationEntity>> = dao.getAllNotifications()
  val unreadNotificationCount: Flow<Int> = dao.getUnreadNotificationCount()
  val allUserAccounts: Flow<List<UserAccountEntity>> = dao.getAllUserAccounts()
  val parentAccounts: Flow<List<UserAccountEntity>> = dao.getParentAccounts()

  val syncStatus: StateFlow<SyncStatus> = syncManager.syncStatus
  val lastSyncTimestamp: StateFlow<Long?> = syncManager.lastSyncTimestamp
  val syncMessage: StateFlow<String?> = syncManager.syncMessage

  suspend fun ensureInitialData() {
    // Purge legacy sample announcements and events only to maintain clean slate
    // Parent user accounts are preserved permanently across app restarts
    dao.purgeSampleAnnouncements()
    dao.purgeSampleEvents()
    dao.purgeSampleNotifications()

    // Ensure only Dagi97 admin account exists as default if missing
    val admin = dao.getUserAccount("Dagi97")
    if (admin == null || admin.password != "Dagi9714") {
      dao.insertUserAccount(
        UserAccountEntity(
          username = "Dagi97",
          password = "Dagi9714",
          role = "ADMIN",
          fullName = "Administrator",
          phone = "",
          email = "Dagi.dt3@gmail.com",
          childrenNames = ""
        )
      )
    }

    // Start real-time Firestore sync & initial upload/fetch
    syncManager.startRealtimeSync()
  }

  suspend fun getUserAccount(username: String): UserAccountEntity? {
    val trimmed = username.trim()
    val local = dao.getUserAccount(trimmed)
    if (local != null) return local
    val remote = syncManager.fetchUserAccountFromFirestore(trimmed)
    if (remote != null) {
      dao.insertUserAccount(remote)
    }
    return remote
  }

  suspend fun triggerManualSync(): Boolean {
    return syncManager.syncAllToFirestore()
  }

  suspend fun authenticate(username: String, password: String): UserAccountEntity? {
    val trimmedUser = username.trim()
    val trimmedPass = password.trim()
    if (trimmedUser.isBlank() || trimmedPass.isBlank()) return null

    // 1. Fast local Room check (works offline & instantaneous)
    val localAccount = dao.getUserAccount(trimmedUser)
    if (localAccount != null) {
      if (localAccount.password == trimmedPass) {
        return localAccount
      }
      // If local password doesn't match and user is local admin, reject immediately
      if (localAccount.role == "ADMIN") {
        return null
      }
    }

    // 2. Not found locally or password mismatch on non-admin: query Firestore directly
    // This allows parents to log in immediately on their own devices without waiting for sync
    val remoteAccount = syncManager.fetchUserAccountFromFirestore(trimmedUser)
    if (remoteAccount != null) {
      // Cache in local Room database for offline access & seamless subsequent sessions
      dao.insertUserAccount(remoteAccount)
      if (remoteAccount.password == trimmedPass) {
        return remoteAccount
      }
    }

    return null
  }

  suspend fun createUserAccount(account: UserAccountEntity) {
    dao.insertUserAccount(account)
    syncManager.uploadUserAccount(account)
  }

  suspend fun deleteUserAccount(username: String) {
    dao.deleteUserAccount(username)
    syncManager.deleteUserAccountFromFirestore(username)
  }

  suspend fun updateUserPassword(username: String, newPassword: String) {
    dao.updateUserPassword(username, newPassword.trim())
    val updated = dao.getUserAccount(username)
    if (updated != null) {
      syncManager.uploadUserAccount(updated)
    }
  }

  fun getAnnouncementById(id: String): Flow<AnnouncementEntity?> = dao.getAnnouncementById(id)

  fun getEventById(id: String): Flow<EventEntity?> = dao.getEventById(id)

  suspend fun markAnnouncementAsRead(id: String) {
    dao.markAnnouncementAsRead(id)
  }

  suspend fun acknowledgeAnnouncement(id: String) {
    val timestamp = System.currentTimeMillis()
    dao.acknowledgeAnnouncement(id, timestamp)
  }

  suspend fun updateAnnouncementRsvp(id: String, status: String, count: Int) {
    dao.updateAnnouncementRsvp(id, status, count)
  }

  suspend fun updateEventRsvp(id: String, status: String, count: Int) {
    dao.updateEventRsvp(id, status, count)
  }

  suspend fun markNotificationAsRead(id: String) {
    dao.markNotificationAsRead(id)
  }

  suspend fun markAllNotificationsAsRead() {
    dao.markAllNotificationsAsRead()
  }

  suspend fun createAnnouncement(announcement: AnnouncementEntity) {
    dao.insertAnnouncement(announcement)
    // Synchronize newly created announcement to Firestore
    syncManager.uploadAnnouncement(announcement)
  }

  suspend fun createEvent(event: EventEntity) {
    dao.insertEvent(event)
    // Synchronize newly created event to Firestore
    syncManager.uploadEvent(event)
  }

  suspend fun togglePinAnnouncement(id: String, isPinned: Boolean) {
    dao.togglePinAnnouncement(id, isPinned)
    val updated = dao.getAnnouncementByIdDirect(id)
    if (updated != null) {
      syncManager.uploadAnnouncement(updated)
    }
  }

  suspend fun archiveAnnouncement(id: String) {
    dao.archiveAnnouncement(id)
    val updated = dao.getAnnouncementByIdDirect(id)
    if (updated != null) {
      syncManager.uploadAnnouncement(updated)
    }
  }

  fun getResources(): List<IvyResource> = IvySampleData.getIvyResources()
}

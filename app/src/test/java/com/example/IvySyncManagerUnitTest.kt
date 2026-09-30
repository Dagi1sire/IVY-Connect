package com.example

import com.example.data.local.IvyDao
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.data.model.IvyNotificationEntity
import com.example.data.model.UserAccountEntity
import com.example.data.sync.IvySyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

class FakeIvyDao : IvyDao {
  private val userAccounts = mutableListOf<UserAccountEntity>()

  override fun getActiveAnnouncements(): Flow<List<AnnouncementEntity>> = flowOf(emptyList())
  override fun getAllAnnouncements(): Flow<List<AnnouncementEntity>> = flowOf(emptyList())
  override suspend fun getAllAnnouncementsDirect(): List<AnnouncementEntity> = emptyList()
  override fun getAnnouncementById(id: String): Flow<AnnouncementEntity?> = flowOf(null)
  override suspend fun getAnnouncementByIdDirect(id: String): AnnouncementEntity? = null
  override suspend fun insertAnnouncements(items: List<AnnouncementEntity>) {}
  override suspend fun insertAnnouncement(item: AnnouncementEntity) {}
  override suspend fun deleteAnnouncementById(id: String) {}
  override suspend fun updateAnnouncement(item: AnnouncementEntity) {}
  override suspend fun markAnnouncementAsRead(id: String) {}
  override suspend fun acknowledgeAnnouncement(id: String, timestamp: Long) {}
  override suspend fun updateAnnouncementRsvp(id: String, status: String, count: Int) {}
  override suspend fun togglePinAnnouncement(id: String, isPinned: Boolean) {}
  override suspend fun archiveAnnouncement(id: String) {}

  override fun getAllEvents(): Flow<List<EventEntity>> = flowOf(emptyList())
  override suspend fun getAllEventsDirect(): List<EventEntity> = emptyList()
  override fun getEventById(id: String): Flow<EventEntity?> = flowOf(null)
  override suspend fun getEventByIdDirect(id: String): EventEntity? = null
  override suspend fun insertEvents(items: List<EventEntity>) {}
  override suspend fun insertEvent(item: EventEntity) {}
  override suspend fun deleteEventById(id: String) {}
  override suspend fun updateEventRsvp(id: String, status: String, count: Int) {}

  override fun getAllNotifications(): Flow<List<IvyNotificationEntity>> = flowOf(emptyList())
  override fun getUnreadNotificationCount(): Flow<Int> = flowOf(0)
  override suspend fun insertNotifications(items: List<IvyNotificationEntity>) {}
  override suspend fun markNotificationAsRead(id: String) {}
  override suspend fun markAllNotificationsAsRead() {}

  override fun getAllUserAccounts(): Flow<List<UserAccountEntity>> = flowOf(userAccounts)
  override suspend fun getAllUserAccountsDirect(): List<UserAccountEntity> = userAccounts
  override fun getParentAccounts(): Flow<List<UserAccountEntity>> = flowOf(userAccounts.filter { it.role == "PARENT" })
  override suspend fun getUserAccount(username: String): UserAccountEntity? =
    userAccounts.firstOrNull { it.username.equals(username, ignoreCase = true) }

  override suspend fun insertUserAccount(account: UserAccountEntity) {
    userAccounts.removeAll { it.username.equals(account.username, ignoreCase = true) }
    userAccounts.add(account)
  }

  override suspend fun insertUserAccounts(accounts: List<UserAccountEntity>) {
    accounts.forEach { insertUserAccount(it) }
  }

  override suspend fun deleteUserAccount(username: String) {
    userAccounts.removeAll { it.username.equals(username, ignoreCase = true) }
  }

  override suspend fun updateUserPassword(username: String, newPassword: String) {
    val existing = getUserAccount(username)
    if (existing != null) {
      insertUserAccount(existing.copy(password = newPassword))
    }
  }

  override suspend fun purgeAllNonAdminAccounts() {
    userAccounts.removeAll { it.role != "ADMIN" }
  }

  override suspend fun purgeSampleAnnouncements() {}
  override suspend fun purgeSampleEvents() {}
  override suspend fun purgeSampleNotifications() {}
}

@RunWith(RobolectricTestRunner::class)
class IvySyncManagerUnitTest {

  @Test
  fun testUserAccountSync_skipsAdmin() = runTest {
    val fakeDao = FakeIvyDao()
    val syncManager = IvySyncManager(dao = fakeDao, firestoreInstance = null)

    val adminAccount = UserAccountEntity(
      username = "Dagi97",
      password = "password",
      role = "ADMIN",
      fullName = "Admin User"
    )

    // Should complete gracefully without firestore instance or uploading admin account
    val result = syncManager.uploadUserAccount(adminAccount)
    assertEquals(true, result)
  }

  @Test
  fun testFetchUserAccountFromFirestore_nullFirestoreReturnsNull() = runTest {
    val fakeDao = FakeIvyDao()
    val syncManager = IvySyncManager(dao = fakeDao, firestoreInstance = null)

    val result = syncManager.fetchUserAccountFromFirestore("nonExistentUser")
    assertEquals(null, result)
  }

  @Test
  fun testUploadParentAccount_executesSuccessfully() = runTest {
    val fakeDao = FakeIvyDao()
    val syncManager = IvySyncManager(dao = fakeDao, firestoreInstance = null)

    val parentAccount = UserAccountEntity(
      username = "SarahM",
      password = "password123",
      role = "PARENT",
      fullName = "Sarah Miller"
    )

    val result = syncManager.uploadUserAccount(parentAccount)
    assertEquals(true, result)
  }
}

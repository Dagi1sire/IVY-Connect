package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.data.model.IvyNotificationEntity
import com.example.data.model.UserAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IvyDao {

  // Announcements
  @Query("SELECT * FROM announcements WHERE isArchived = 0 ORDER BY isPinned DESC, createdAt DESC")
  fun getActiveAnnouncements(): Flow<List<AnnouncementEntity>>

  @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
  fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

  @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
  suspend fun getAllAnnouncementsDirect(): List<AnnouncementEntity>

  @Query("SELECT * FROM announcements WHERE id = :id")
  fun getAnnouncementById(id: String): Flow<AnnouncementEntity?>

  @Query("SELECT * FROM announcements WHERE id = :id")
  suspend fun getAnnouncementByIdDirect(id: String): AnnouncementEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnnouncements(items: List<AnnouncementEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnnouncement(item: AnnouncementEntity)

  @Query("DELETE FROM announcements WHERE id = :id")
  suspend fun deleteAnnouncementById(id: String)

  @Update
  suspend fun updateAnnouncement(item: AnnouncementEntity)

  @Query("UPDATE announcements SET isRead = 1 WHERE id = :id")
  suspend fun markAnnouncementAsRead(id: String)

  @Query("UPDATE announcements SET isAcknowledged = 1, acknowledgedAt = :timestamp, isRead = 1 WHERE id = :id")
  suspend fun acknowledgeAnnouncement(id: String, timestamp: Long)

  @Query("UPDATE announcements SET rsvpStatus = :status, rsvpGuestCount = :count WHERE id = :id")
  suspend fun updateAnnouncementRsvp(id: String, status: String, count: Int)

  @Query("UPDATE announcements SET isPinned = :isPinned WHERE id = :id")
  suspend fun togglePinAnnouncement(id: String, isPinned: Boolean)

  @Query("UPDATE announcements SET isArchived = 1 WHERE id = :id")
  suspend fun archiveAnnouncement(id: String)

  // Events
  @Query("SELECT * FROM events ORDER BY date ASC")
  fun getAllEvents(): Flow<List<EventEntity>>

  @Query("SELECT * FROM events ORDER BY date ASC")
  suspend fun getAllEventsDirect(): List<EventEntity>

  @Query("SELECT * FROM events WHERE id = :id")
  fun getEventById(id: String): Flow<EventEntity?>

  @Query("SELECT * FROM events WHERE id = :id")
  suspend fun getEventByIdDirect(id: String): EventEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEvents(items: List<EventEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEvent(item: EventEntity)

  @Query("DELETE FROM events WHERE id = :id")
  suspend fun deleteEventById(id: String)

  @Query("UPDATE events SET rsvpStatus = :status, rsvpGuestCount = :count WHERE id = :id")
  suspend fun updateEventRsvp(id: String, status: String, count: Int)

  // Notifications
  @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
  fun getAllNotifications(): Flow<List<IvyNotificationEntity>>

  @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
  fun getUnreadNotificationCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotifications(items: List<IvyNotificationEntity>)

  @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
  suspend fun markNotificationAsRead(id: String)

  @Query("UPDATE notifications SET isRead = 1")
  suspend fun markAllNotificationsAsRead()

  // User Accounts
  @Query("SELECT * FROM user_accounts ORDER BY role ASC, createdAt DESC")
  fun getAllUserAccounts(): Flow<List<UserAccountEntity>>

  @Query("SELECT * FROM user_accounts ORDER BY role ASC, createdAt DESC")
  suspend fun getAllUserAccountsDirect(): List<UserAccountEntity>

  @Query("SELECT * FROM user_accounts WHERE role = 'PARENT' ORDER BY createdAt DESC")
  fun getParentAccounts(): Flow<List<UserAccountEntity>>

  @Query("SELECT * FROM user_accounts WHERE LOWER(username) = LOWER(:username) LIMIT 1")
  suspend fun getUserAccount(username: String): UserAccountEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUserAccount(account: UserAccountEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUserAccounts(accounts: List<UserAccountEntity>)

  @Query("DELETE FROM user_accounts WHERE username = :username")
  suspend fun deleteUserAccount(username: String)

  @Query("UPDATE user_accounts SET password = :newPassword WHERE username = :username")
  suspend fun updateUserPassword(username: String, newPassword: String)

  @Query("DELETE FROM user_accounts WHERE username != 'Dagi97' AND role != 'ADMIN'")
  suspend fun purgeAllNonAdminAccounts()

  @Query("DELETE FROM announcements WHERE id LIKE 'ann_%'")
  suspend fun purgeSampleAnnouncements()

  @Query("DELETE FROM events WHERE id LIKE 'evt_%'")
  suspend fun purgeSampleEvents()

  @Query("DELETE FROM notifications WHERE id LIKE 'notif_%'")
  suspend fun purgeSampleNotifications()
}

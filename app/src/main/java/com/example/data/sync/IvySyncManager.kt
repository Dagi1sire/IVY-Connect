package com.example.data.sync

import android.util.Log
import com.example.data.local.IvyDao
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.data.model.UserAccountEntity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

enum class SyncStatus {
  IDLE,
  SYNCING,
  SUCCESS,
  ERROR
}

/**
 * Room-to-Firestore bidirectional synchronization manager.
 * Handles:
 * 1. Listening to real-time updates from Firestore "announcements" and "events" collections,
 *    upserting into the local Room database.
 * 2. Uploading locally created/modified announcements and events to Firestore.
 * 3. Seed initial Room data to Firestore if Firestore is empty.
 * 4. Graceful handling of network, offline caching, and exceptions without crashing on startup.
 */
class IvySyncManager(
  private val dao: IvyDao,
  private val firestoreInstance: FirebaseFirestore? = null,
  private val customFirestore: FirebaseFirestore? = firestoreInstance
) {
  private val TAG = "IvySyncManager"
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  // Dynamic safe access to Firestore to avoid caching null if Firebase initialization is slightly deferred
  private val firestore: FirebaseFirestore?
    get() = customFirestore ?: firestoreInstance ?: try {
      FirebaseFirestore.getInstance()
    } catch (e: Throwable) {
      Log.e(TAG, "FirebaseFirestore instance unavailable: ${e.message}", e)
      null
    }

  private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
  val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

  private val _lastSyncTimestamp = MutableStateFlow<Long?>(null)
  val lastSyncTimestamp: StateFlow<Long?> = _lastSyncTimestamp.asStateFlow()

  private val _syncMessage = MutableStateFlow<String?>(null)
  val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

  private var announcementsListener: ListenerRegistration? = null
  private var eventsListener: ListenerRegistration? = null
  private var usersListener: ListenerRegistration? = null

  companion object {
    const val COLLECTION_ANNOUNCEMENTS = "announcements"
    const val COLLECTION_EVENTS = "events"
    const val COLLECTION_USERS = "users"
  }

  /**
   * Starts real-time listeners on Firestore collections and performs an initial sync pass.
   */
  fun startRealtimeSync() {
    val fs = firestore
    if (fs == null) {
      Log.w(TAG, "Firestore is unavailable. Operating in local-only mode.")
      _syncStatus.value = SyncStatus.IDLE
      _syncMessage.value = "Local mode (Firestore offline)"
      return
    }

    listenToAnnouncements(fs)
    listenToEvents(fs)
    listenToUsers(fs)
    scope.launch {
      syncAllToFirestore()
    }
  }

  /**
   * Stops real-time listeners.
   */
  fun stopRealtimeSync() {
    announcementsListener?.remove()
    announcementsListener = null
    eventsListener?.remove()
    eventsListener = null
    usersListener?.remove()
    usersListener = null
  }

  /**
   * Listen to real-time snapshot changes from Firestore users collection.
   */
  private fun listenToUsers(fs: FirebaseFirestore) {
    try {
      usersListener?.remove()
      usersListener = fs.collection(COLLECTION_USERS)
        .addSnapshotListener { snapshots, error ->
          if (error != null) {
            Log.e(TAG, "Firestore users listener error: ${error.message}", error)
            return@addSnapshotListener
          }

          if (snapshots != null && !snapshots.isEmpty) {
            scope.launch {
              try {
                for (doc in snapshots.documents) {
                  val user = docToUserAccount(doc)
                  if (user != null && !user.username.equals("Dagi97", ignoreCase = true) && !user.role.equals("ADMIN", ignoreCase = true)) {
                    dao.insertUserAccount(user)
                  }
                }
              } catch (e: Exception) {
                Log.e(TAG, "Error inserting Firestore users to Room: ${e.message}", e)
              }
            }
          }
        }
    } catch (e: Exception) {
      Log.e(TAG, "Failed to register users listener: ${e.message}", e)
    }
  }

  /**
   * Listen to real-time snapshot changes from Firestore announcements collection.
   */
  private fun listenToAnnouncements(fs: FirebaseFirestore) {
    try {
      announcementsListener?.remove()
      announcementsListener = fs.collection(COLLECTION_ANNOUNCEMENTS)
        .addSnapshotListener { snapshots, error ->
          if (error != null) {
            Log.e(TAG, "Firestore announcements listener error: ${error.message}", error)
            return@addSnapshotListener
          }

          if (snapshots != null && !snapshots.isEmpty) {
            scope.launch {
              try {
                for (doc in snapshots.documents) {
                  val announcement = docToAnnouncement(doc)
                  if (announcement != null) {
                    val local = dao.getAnnouncementByIdDirect(announcement.id)
                    // Preserve local read and acknowledged state if newer or set locally
                    val resolved = if (local != null) {
                      announcement.copy(
                        isRead = local.isRead || announcement.isRead,
                        isAcknowledged = local.isAcknowledged || announcement.isAcknowledged,
                        acknowledgedAt = local.acknowledgedAt ?: announcement.acknowledgedAt,
                        rsvpStatus = local.rsvpStatus ?: announcement.rsvpStatus,
                        rsvpGuestCount = if (local.rsvpStatus != null) local.rsvpGuestCount else announcement.rsvpGuestCount
                      )
                    } else {
                      announcement
                    }
                    dao.insertAnnouncement(resolved)
                  }
                }
                _lastSyncTimestamp.value = System.currentTimeMillis()
              } catch (e: Exception) {
                Log.e(TAG, "Error inserting Firestore announcements to Room: ${e.message}", e)
              }
            }
          }
        }
    } catch (e: Exception) {
      Log.e(TAG, "Failed to register announcements listener: ${e.message}", e)
    }
  }

  /**
   * Listen to real-time snapshot changes from Firestore events collection.
   */
  private fun listenToEvents(fs: FirebaseFirestore) {
    try {
      eventsListener?.remove()
      eventsListener = fs.collection(COLLECTION_EVENTS)
        .addSnapshotListener { snapshots, error ->
          if (error != null) {
            Log.e(TAG, "Firestore events listener error: ${error.message}", error)
            return@addSnapshotListener
          }

          if (snapshots != null && !snapshots.isEmpty) {
            scope.launch {
              try {
                for (doc in snapshots.documents) {
                  val event = docToEvent(doc)
                  if (event != null) {
                    val local = dao.getEventByIdDirect(event.id)
                    // Preserve local RSVP status
                    val resolved = if (local != null && local.rsvpStatus != null) {
                      event.copy(
                        rsvpStatus = local.rsvpStatus,
                        rsvpGuestCount = local.rsvpGuestCount
                      )
                    } else {
                      event
                    }
                    dao.insertEvent(resolved)
                  }
                }
                _lastSyncTimestamp.value = System.currentTimeMillis()
              } catch (e: Exception) {
                Log.e(TAG, "Error inserting Firestore events to Room: ${e.message}", e)
              }
            }
          }
        }
    } catch (e: Exception) {
      Log.e(TAG, "Failed to register events listener: ${e.message}", e)
    }
  }

  /**
   * Synchronizes all local Room announcements and events to Firestore.
   */
  suspend fun syncAllToFirestore(): Boolean = withContext(Dispatchers.IO) {
    val fs = firestore
    if (fs == null) {
      _syncStatus.value = SyncStatus.ERROR
      _syncMessage.value = "Firestore is offline or unavailable"
      return@withContext false
    }

    _syncStatus.value = SyncStatus.SYNCING
    try {
      // 1. Sync announcements
      val localAnnouncements = dao.getAllAnnouncementsDirect()
      for (announcement in localAnnouncements) {
        uploadAnnouncement(announcement)
      }

      // 2. Fetch remote announcements that might not exist locally
      val remoteAnnouncements = fs.collection(COLLECTION_ANNOUNCEMENTS).get().await()
      for (doc in remoteAnnouncements.documents) {
        val remoteItem = docToAnnouncement(doc)
        if (remoteItem != null && dao.getAnnouncementByIdDirect(remoteItem.id) == null) {
          dao.insertAnnouncement(remoteItem)
        }
      }

      // 3. Sync events
      val localEvents = dao.getAllEventsDirect()
      for (event in localEvents) {
        uploadEvent(event)
      }

      // 4. Fetch remote events that might not exist locally
      val remoteEvents = fs.collection(COLLECTION_EVENTS).get().await()
      for (doc in remoteEvents.documents) {
        val remoteEvent = docToEvent(doc)
        if (remoteEvent != null && dao.getEventByIdDirect(remoteEvent.id) == null) {
          dao.insertEvent(remoteEvent)
        }
      }

      // 5. Sync parent user accounts to Firestore
      val localUsers = dao.getAllUserAccountsDirect()
      for (user in localUsers) {
        if (!user.role.equals("ADMIN", ignoreCase = true) && !user.username.equals("Dagi97", ignoreCase = true)) {
          uploadUserAccount(user)
        }
      }

      // 6. Fetch remote users and update local Room database
      val remoteUsers = fs.collection(COLLECTION_USERS).get().await()
      for (doc in remoteUsers.documents) {
        val remoteUser = docToUserAccount(doc)
        if (remoteUser != null && !remoteUser.username.equals("Dagi97", ignoreCase = true) && !remoteUser.role.equals("ADMIN", ignoreCase = true)) {
          dao.insertUserAccount(remoteUser)
        }
      }

      _syncStatus.value = SyncStatus.SUCCESS
      _lastSyncTimestamp.value = System.currentTimeMillis()
      _syncMessage.value = "Synced ${localAnnouncements.size} notices, ${localEvents.size} events & ${localUsers.size} accounts"
      true
    } catch (e: Exception) {
      Log.e(TAG, "Sync to Firestore failed: ${e.message}", e)
      _syncStatus.value = SyncStatus.ERROR
      _syncMessage.value = e.localizedMessage ?: "Sync error"
      false
    }
  }

  /**
   * Push a single announcement to Firestore
   */
  suspend fun uploadAnnouncement(announcement: AnnouncementEntity) = withContext(Dispatchers.IO) {
    val fs = firestore ?: return@withContext
    try {
      val data = mapOf(
        "id" to announcement.id,
        "titleEn" to announcement.titleEn,
        "titleAm" to announcement.titleAm,
        "bodyEn" to announcement.bodyEn,
        "bodyAm" to announcement.bodyAm,
        "category" to announcement.category,
        "priority" to announcement.priority,
        "audience" to announcement.audience,
        "isPinned" to announcement.isPinned,
        "requiresAcknowledgment" to announcement.requiresAcknowledgment,
        "enableRSVP" to announcement.enableRSVP,
        "eventDate" to (announcement.eventDate ?: ""),
        "eventTime" to (announcement.eventTime ?: ""),
        "publishDate" to announcement.publishDate,
        "createdAt" to announcement.createdAt,
        "isArchived" to announcement.isArchived,
        "attachmentsJson" to announcement.attachmentsJson,
        "galleryDrawablesJson" to announcement.galleryDrawablesJson
      )
      fs.collection(COLLECTION_ANNOUNCEMENTS)
        .document(announcement.id)
        .set(data, SetOptions.merge())
        .await()
      Log.d(TAG, "Successfully uploaded announcement ${announcement.id} to Firestore")
    } catch (e: Exception) {
      Log.e(TAG, "Failed to upload announcement ${announcement.id}: ${e.message}", e)
    }
  }

  /**
   * Push a single event to Firestore
   */
  suspend fun uploadEvent(event: EventEntity) = withContext(Dispatchers.IO) {
    val fs = firestore ?: return@withContext
    try {
      val data = mapOf(
        "id" to event.id,
        "titleEn" to event.titleEn,
        "titleAm" to event.titleAm,
        "descriptionEn" to event.descriptionEn,
        "descriptionAm" to event.descriptionAm,
        "date" to event.date,
        "displayDate" to event.displayDate,
        "time" to event.time,
        "location" to event.location,
        "eventType" to event.eventType,
        "iconEmoji" to event.iconEmoji,
        "whatToBringEn" to event.whatToBringEn,
        "whatToBringAm" to event.whatToBringAm,
        "enableRSVP" to event.enableRSVP,
        "rsvpDeadline" to (event.rsvpDeadline ?: "")
      )
      fs.collection(COLLECTION_EVENTS)
        .document(event.id)
        .set(data, SetOptions.merge())
        .await()
      Log.d(TAG, "Successfully uploaded event ${event.id} to Firestore")
    } catch (e: Exception) {
      Log.e(TAG, "Failed to upload event ${event.id}: ${e.message}", e)
    }
  }

  /**
   * Delete an announcement from Firestore
   */
  suspend fun deleteAnnouncementFromFirestore(id: String) = withContext(Dispatchers.IO) {
    val fs = firestore ?: return@withContext
    try {
      fs.collection(COLLECTION_ANNOUNCEMENTS).document(id).delete().await()
    } catch (e: Exception) {
      Log.e(TAG, "Failed to delete announcement $id: ${e.message}", e)
    }
  }

  /**
   * Delete an event from Firestore
   */
  suspend fun deleteEventFromFirestore(id: String) = withContext(Dispatchers.IO) {
    val fs = firestore ?: return@withContext
    try {
      fs.collection(COLLECTION_EVENTS).document(id).delete().await()
    } catch (e: Exception) {
      Log.e(TAG, "Failed to delete event $id: ${e.message}", e)
    }
  }

  /**
   * Directly fetch a user account from Firestore by username.
   * Performs an immediate cloud lookup across normalized lowercase ID, original ID,
   * and username queries to ensure instant login on newly connected devices.
   */
  suspend fun fetchUserAccountFromFirestore(username: String): UserAccountEntity? = withContext(Dispatchers.IO) {
    val fs = firestore ?: return@withContext null
    val trimmed = username.trim()
    if (trimmed.isBlank()) return@withContext null

    try {
      val lowerUser = trimmed.lowercase()

      // 1. Try direct lookup by normalized lowercase document ID
      val lowerDoc = fs.collection(COLLECTION_USERS).document(lowerUser).get().await()
      if (lowerDoc.exists()) {
        val user = docToUserAccount(lowerDoc)
        if (user != null) return@withContext user
      }

      // 2. Try exact document ID if casing was different
      if (trimmed != lowerUser) {
        val exactDoc = fs.collection(COLLECTION_USERS).document(trimmed).get().await()
        if (exactDoc.exists()) {
          val user = docToUserAccount(exactDoc)
          if (user != null) return@withContext user
        }
      }

      // 3. Fallback: Query by usernameLower property
      val queryLower = fs.collection(COLLECTION_USERS)
        .whereEqualTo("usernameLower", lowerUser)
        .limit(1)
        .get()
        .await()
      if (!queryLower.isEmpty) {
        val user = docToUserAccount(queryLower.documents[0])
        if (user != null) return@withContext user
      }

      // 4. Fallback: Query by username property
      val queryExact = fs.collection(COLLECTION_USERS)
        .whereEqualTo("username", trimmed)
        .limit(1)
        .get()
        .await()
      if (!queryExact.isEmpty) {
        val user = docToUserAccount(queryExact.documents[0])
        if (user != null) return@withContext user
      }

      null
    } catch (e: Exception) {
      Log.e(TAG, "Error fetching user account '$trimmed' from Firestore: ${e.message}", e)
      null
    }
  }

  /**
   * Upload a user account to Firestore (skips admin accounts).
   * Normalizes document IDs to ensure case-insensitive matching across devices.
   */
  suspend fun uploadUserAccount(account: UserAccountEntity): Boolean = withContext(Dispatchers.IO) {
    if (account.role.equals("ADMIN", ignoreCase = true)) {
      Log.d(TAG, "Skipping upload for ADMIN account: ${account.username}")
      return@withContext true
    }
    val fs = firestore
    if (fs == null) {
      Log.w(TAG, "Cannot upload user account ${account.username}: Firestore unavailable")
      return@withContext false
    }
    try {
      val trimmedUser = account.username.trim()
      val lowerUser = trimmedUser.lowercase()
      val data = hashMapOf(
        "username" to trimmedUser,
        "usernameLower" to lowerUser,
        "password" to account.password.trim(),
        "role" to account.role,
        "fullName" to account.fullName.trim(),
        "phone" to account.phone.trim(),
        "email" to account.email.trim(),
        "childrenNames" to account.childrenNames.trim(),
        "createdAt" to account.createdAt
      )

      // Store in normalized lowercase document ID for reliable cloud indexing
      fs.collection(COLLECTION_USERS)
        .document(lowerUser)
        .set(data, SetOptions.merge())
        .await()

      // Also store in original casing document ID if different, ensuring seamless lookup
      if (trimmedUser != lowerUser) {
        fs.collection(COLLECTION_USERS)
          .document(trimmedUser)
          .set(data, SetOptions.merge())
          .await()
      }

      Log.d(TAG, "Successfully uploaded user account ${account.username} to Firestore")
      true
    } catch (e: Exception) {
      Log.e(TAG, "Failed to upload user account ${account.username}: ${e.message}", e)
      false
    }
  }

  /**
   * Delete a user account from Firestore.
   */
  suspend fun deleteUserAccountFromFirestore(username: String) = withContext(Dispatchers.IO) {
    val fs = firestore ?: return@withContext
    try {
      val trimmed = username.trim()
      val lower = trimmed.lowercase()
      fs.collection(COLLECTION_USERS).document(lower).delete().await()
      if (trimmed != lower) {
        fs.collection(COLLECTION_USERS).document(trimmed).delete().await()
      }
      Log.d(TAG, "Successfully deleted user account $username from Firestore")
    } catch (e: Exception) {
      Log.e(TAG, "Failed to delete user account $username: ${e.message}", e)
    }
  }

  // --- Helpers to parse Firestore Documents into Room Entities ---

  private fun docToAnnouncement(doc: DocumentSnapshot): AnnouncementEntity? {
    return try {
      val id = doc.getString("id") ?: doc.id
      val titleEn = doc.getString("titleEn") ?: ""
      val titleAm = doc.getString("titleAm") ?: ""
      val bodyEn = doc.getString("bodyEn") ?: ""
      val bodyAm = doc.getString("bodyAm") ?: ""
      val category = doc.getString("category") ?: "General"
      val priority = doc.getString("priority") ?: "NORMAL"
      val audience = doc.getString("audience") ?: "ALL"
      val isPinned = doc.getBoolean("isPinned") ?: false
      val requiresAcknowledgment = doc.getBoolean("requiresAcknowledgment") ?: false
      val enableRSVP = doc.getBoolean("enableRSVP") ?: false
      val eventDate = doc.getString("eventDate")?.takeIf { it.isNotBlank() }
      val eventTime = doc.getString("eventTime")?.takeIf { it.isNotBlank() }
      val publishDate = doc.getString("publishDate") ?: ""
      val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
      val isArchived = doc.getBoolean("isArchived") ?: false
      val attachmentsJson = doc.getString("attachmentsJson") ?: ""
      val galleryDrawablesJson = doc.getString("galleryDrawablesJson") ?: ""

      AnnouncementEntity(
        id = id,
        titleEn = titleEn,
        titleAm = titleAm,
        bodyEn = bodyEn,
        bodyAm = bodyAm,
        category = category,
        priority = priority,
        audience = audience,
        isPinned = isPinned,
        requiresAcknowledgment = requiresAcknowledgment,
        enableRSVP = enableRSVP,
        eventDate = eventDate,
        eventTime = eventTime,
        publishDate = publishDate,
        createdAt = createdAt,
        isArchived = isArchived,
        attachmentsJson = attachmentsJson,
        galleryDrawablesJson = galleryDrawablesJson
      )
    } catch (e: Exception) {
      Log.e(TAG, "Error parsing announcement doc ${doc.id}: ${e.message}", e)
      null
    }
  }

  private fun docToUserAccount(doc: DocumentSnapshot): UserAccountEntity? {
    return try {
      val username = doc.getString("username") ?: doc.id
      val password = doc.getString("password") ?: ""
      val role = doc.getString("role") ?: "PARENT"
      val fullName = doc.getString("fullName") ?: ""
      val phone = doc.getString("phone") ?: ""
      val email = doc.getString("email") ?: ""
      val childrenNames = doc.getString("childrenNames") ?: ""
      val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()

      UserAccountEntity(
        username = username,
        password = password,
        role = role,
        fullName = fullName,
        phone = phone,
        email = email,
        childrenNames = childrenNames,
        createdAt = createdAt
      )
    } catch (e: Exception) {
      Log.e(TAG, "Error parsing user doc ${doc.id}: ${e.message}", e)
      null
    }
  }

  private fun docToEvent(doc: DocumentSnapshot): EventEntity? {
    return try {
      val id = doc.getString("id") ?: doc.id
      val titleEn = doc.getString("titleEn") ?: ""
      val titleAm = doc.getString("titleAm") ?: ""
      val descriptionEn = doc.getString("descriptionEn") ?: ""
      val descriptionAm = doc.getString("descriptionAm") ?: ""
      val date = doc.getString("date") ?: ""
      val displayDate = doc.getString("displayDate") ?: date
      val time = doc.getString("time") ?: ""
      val location = doc.getString("location") ?: "IVY Childcare Services, Main Campus"
      val eventType = doc.getString("eventType") ?: "Special Activity"
      val iconEmoji = doc.getString("iconEmoji") ?: "📅"
      val whatToBringEn = doc.getString("whatToBringEn") ?: ""
      val whatToBringAm = doc.getString("whatToBringAm") ?: ""
      val enableRSVP = doc.getBoolean("enableRSVP") ?: true
      val rsvpDeadline = doc.getString("rsvpDeadline")?.takeIf { it.isNotBlank() }

      EventEntity(
        id = id,
        titleEn = titleEn,
        titleAm = titleAm,
        descriptionEn = descriptionEn,
        descriptionAm = descriptionAm,
        date = date,
        displayDate = displayDate,
        time = time,
        location = location,
        eventType = eventType,
        iconEmoji = iconEmoji,
        whatToBringEn = whatToBringEn,
        whatToBringAm = whatToBringAm,
        enableRSVP = enableRSVP,
        rsvpDeadline = rsvpDeadline
      )
    } catch (e: Exception) {
      Log.e(TAG, "Error parsing event doc ${doc.id}: ${e.message}", e)
      null
    }
  }
}

package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.IvyDatabase
import com.example.data.model.AnnouncementEntity
import com.example.data.model.ChildInfo
import com.example.data.model.EventEntity
import com.example.data.model.IvyNotificationEntity
import com.example.data.model.IvyResource
import com.example.data.model.ParentProfile
import com.example.data.model.UserAccountEntity
import com.example.data.repository.IvyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class IvyUiState(
  val language: String = "en", // "en" or "am"
  val currentTab: Int = 0, // 0: Home, 1: Notices, 2: Calendar, 3: IVY
  val selectedCategory: String = "All",
  val searchQuery: String = "",
  val activeAudienceFilter: String = "ALL", // "ALL", "PRESCHOOL", "TODDLER", "INFANT"
  val selectedAnnouncementId: String? = null,
  val selectedEventId: String? = null,
  val showNotificationsScreen: Boolean = false,
  val showAdminDashboard: Boolean = false,
  val showCreateAnnouncement: Boolean = false,
  val showWhatsNewBanner: Boolean = true,
  val calendarViewMode: String = "LIST", // "LIST" or "MONTH"
  val selectedCalendarDate: String = "2026-09-10",
  val parentProfile: ParentProfile = ParentProfile(),
  val isAdminMode: Boolean = false,
  val currentUserAccount: UserAccountEntity? = null,
  val showLoginDialog: Boolean = false,
  val loginErrorMessage: String? = null,
  val isLoggingIn: Boolean = false,
  val showCreateParentDialog: Boolean = false,
  val accountCreationSuccessMessage: String? = null
)

class IvyViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: IvyRepository

  private val _uiState = MutableStateFlow(IvyUiState())
  val uiState: StateFlow<IvyUiState> = _uiState.asStateFlow()

  init {
    val database = IvyDatabase.getDatabase(application)
    repository = IvyRepository(database.ivyDao())
    viewModelScope.launch {
      repository.ensureInitialData()
    }
  }

  val announcements: StateFlow<List<AnnouncementEntity>> = repository.activeAnnouncements
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allAnnouncements: StateFlow<List<AnnouncementEntity>> = repository.allAnnouncements
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val events: StateFlow<List<EventEntity>> = repository.allEvents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val notifications: StateFlow<List<IvyNotificationEntity>> = repository.allNotifications
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val unreadCount: StateFlow<Int> = repository.unreadNotificationCount
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  val parentAccounts: StateFlow<List<UserAccountEntity>> = repository.parentAccounts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allUserAccounts: StateFlow<List<UserAccountEntity>> = repository.allUserAccounts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val syncStatus: StateFlow<com.example.data.sync.SyncStatus> = repository.syncStatus
  val lastSyncTimestamp: StateFlow<Long?> = repository.lastSyncTimestamp
  val syncMessage: StateFlow<String?> = repository.syncMessage

  val resources: List<IvyResource> = repository.getResources()

  fun triggerSync() {
    viewModelScope.launch {
      repository.triggerManualSync()
    }
  }

  fun createCalendarEvent(event: EventEntity) {
    viewModelScope.launch {
      repository.createEvent(event)
    }
  }

  // Authentication & Account Management
  fun openLoginDialog() {
    _uiState.value = _uiState.value.copy(showLoginDialog = true, loginErrorMessage = null)
  }

  fun closeLoginDialog() {
    _uiState.value = _uiState.value.copy(showLoginDialog = false, loginErrorMessage = null)
  }

  fun login(username: String, password: String, onResult: ((Boolean, String?) -> Unit)? = null) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoggingIn = true, loginErrorMessage = null)
      val account = repository.authenticate(username, password)
      if (account != null) {
        val isAdmin = account.role == "ADMIN"
        val updatedProfile = if (isAdmin) {
          ParentProfile(
            username = account.username,
            name = account.fullName,
            phone = account.phone,
            email = account.email,
            children = emptyList(),
            isAdmin = true
          )
        } else {
          val childList = parseChildren(account.childrenNames)
          ParentProfile(
            username = account.username,
            name = account.fullName,
            phone = account.phone,
            email = account.email,
            children = childList,
            isAdmin = false
          )
        }

        _uiState.value = _uiState.value.copy(
          isLoggingIn = false,
          currentUserAccount = account,
          parentProfile = updatedProfile,
          isAdminMode = isAdmin,
          showLoginDialog = false,
          loginErrorMessage = null,
          showAdminDashboard = isAdmin
        )
        onResult?.invoke(true, null)
      } else {
        val error = if (_uiState.value.language == "am") "የተሳሳተ የተጠቃሚ ስም ወይም የይለፍ ቃል" else "Invalid username or password"
        _uiState.value = _uiState.value.copy(isLoggingIn = false, loginErrorMessage = error)
        onResult?.invoke(false, error)
      }
    }
  }

  fun logout() {
    _uiState.value = _uiState.value.copy(
      currentUserAccount = null,
      isAdminMode = false,
      showAdminDashboard = false,
      showCreateAnnouncement = false,
      showNotificationsScreen = false,
      selectedAnnouncementId = null,
      selectedEventId = null,
      parentProfile = ParentProfile(),
      loginErrorMessage = null
    )
  }

  fun openCreateParentDialog() {
    _uiState.value = _uiState.value.copy(showCreateParentDialog = true)
  }

  fun closeCreateParentDialog() {
    _uiState.value = _uiState.value.copy(showCreateParentDialog = false)
  }

  fun createParentAccount(
    fullName: String,
    username: String,
    password: String,
    phone: String,
    email: String,
    childrenNames: String,
    onResult: (Boolean, String) -> Unit
  ) {
    viewModelScope.launch {
      val trimmedUser = username.trim()
      val trimmedPass = password.trim()
      val trimmedName = fullName.trim()

      if (trimmedUser.isBlank() || trimmedPass.isBlank() || trimmedName.isBlank()) {
        onResult(false, if (_uiState.value.language == "am") "እባክዎ ሁሉንም አስፈላጊ መረጃዎች ይሙሉ" else "Please fill in all required fields")
        return@launch
      }

      val existing = repository.getUserAccount(trimmedUser)
      if (existing != null) {
        onResult(false, if (_uiState.value.language == "am") "ይህ የተጠቃሚ ስም አስቀድሞ ተወስዷል" else "This username is already taken")
        return@launch
      }

      val newAccount = UserAccountEntity(
        username = trimmedUser,
        password = trimmedPass,
        role = "PARENT",
        fullName = trimmedName,
        phone = phone.trim(),
        email = email.trim(),
        childrenNames = childrenNames.trim(),
        createdAt = System.currentTimeMillis()
      )

      repository.createUserAccount(newAccount)

      val wasLoggedOut = _uiState.value.currentUserAccount == null
      if (wasLoggedOut) {
        val childList = parseChildren(newAccount.childrenNames)
        _uiState.value = _uiState.value.copy(
          showCreateParentDialog = false,
          currentUserAccount = newAccount,
          parentProfile = ParentProfile(
            username = newAccount.username,
            name = newAccount.fullName,
            phone = newAccount.phone,
            email = newAccount.email,
            children = childList,
            isAdmin = false
          ),
          isAdminMode = false,
          showAdminDashboard = false,
          loginErrorMessage = null,
          accountCreationSuccessMessage = if (_uiState.value.language == "am")
            "የወላጅ አካውንት ለ $trimmedName በተሳካ ሁኔታ ተፈጥሯል!"
          else
            "Parent account for $trimmedName created successfully!"
        )
      } else {
        _uiState.value = _uiState.value.copy(
          showCreateParentDialog = false,
          accountCreationSuccessMessage = if (_uiState.value.language == "am")
            "የወላጅ አካውንት ለ $trimmedName በተሳካ ሁኔታ ተፈጥሯል!"
          else
            "Parent account for $trimmedName created successfully!"
        )
      }
      onResult(true, "Success")
    }
  }

  fun deleteParentAccount(username: String) {
    viewModelScope.launch {
      repository.deleteUserAccount(username)
    }
  }

  fun updateParentPassword(username: String, newPass: String) {
    viewModelScope.launch {
      repository.updateUserPassword(username, newPass)
    }
  }

  fun clearAccountCreationMessage() {
    _uiState.value = _uiState.value.copy(accountCreationSuccessMessage = null)
  }

  private fun parseChildren(childrenStr: String): List<ChildInfo> {
    if (childrenStr.isBlank()) {
      return listOf(
        ChildInfo(name = "Child", department = "Preschool", className = "Butterflies Class", avatarEmoji = "👧")
      )
    }
    return childrenStr.split(",").mapNotNull { part ->
      val trimmed = part.trim()
      if (trimmed.isEmpty()) return@mapNotNull null
      val name = trimmed.substringBefore("(").substringBefore("-").trim()
      val dept = if (trimmed.contains("(") && trimmed.contains(")")) {
        trimmed.substringAfter("(").substringBefore(")").trim()
      } else if (trimmed.contains("-")) {
        trimmed.substringAfter("-").trim()
      } else {
        "Preschool"
      }
      ChildInfo(
        name = if (name.isNotEmpty()) name else trimmed,
        department = dept,
        className = "$dept Class",
        avatarEmoji = if (dept.contains("Toddler", ignoreCase = true) || name.endsWith("el") || name.endsWith("om")) "👦" else "👧"
      )
    }
  }

  // Navigation & Screen transitions
  fun setTab(tabIndex: Int) {
    _uiState.value = _uiState.value.copy(
      currentTab = tabIndex,
      selectedAnnouncementId = null,
      selectedEventId = null,
      showNotificationsScreen = false,
      showAdminDashboard = false,
      showCreateAnnouncement = false
    )
  }

  fun selectAnnouncement(id: String?) {
    _uiState.value = _uiState.value.copy(selectedAnnouncementId = id)
    if (id != null) {
      viewModelScope.launch {
        repository.markAnnouncementAsRead(id)
      }
    }
  }

  fun selectEvent(id: String?) {
    _uiState.value = _uiState.value.copy(selectedEventId = id)
  }

  fun setNotificationsVisible(visible: Boolean) {
    _uiState.value = _uiState.value.copy(showNotificationsScreen = visible)
  }

  fun setAdminDashboardVisible(visible: Boolean) {
    _uiState.value = _uiState.value.copy(showAdminDashboard = visible)
  }

  fun setCreateAnnouncementVisible(visible: Boolean) {
    _uiState.value = _uiState.value.copy(showCreateAnnouncement = visible)
  }

  fun dismissWhatsNew() {
    _uiState.value = _uiState.value.copy(showWhatsNewBanner = false)
  }

  fun setCategory(category: String) {
    _uiState.value = _uiState.value.copy(selectedCategory = category)
  }

  fun setSearchQuery(query: String) {
    _uiState.value = _uiState.value.copy(searchQuery = query)
  }

  fun setAudienceFilter(audience: String) {
    _uiState.value = _uiState.value.copy(activeAudienceFilter = audience)
  }

  fun toggleLanguage() {
    val newLang = if (_uiState.value.language == "en") "am" else "en"
    _uiState.value = _uiState.value.copy(language = newLang)
  }

  fun setLanguage(lang: String) {
    _uiState.value = _uiState.value.copy(language = lang)
  }

  fun setCalendarViewMode(mode: String) {
    _uiState.value = _uiState.value.copy(calendarViewMode = mode)
  }

  fun setSelectedCalendarDate(date: String) {
    _uiState.value = _uiState.value.copy(selectedCalendarDate = date)
  }

  // Interactive Actions
  fun acknowledgeAnnouncement(id: String) {
    viewModelScope.launch {
      repository.acknowledgeAnnouncement(id)
    }
  }

  fun submitAnnouncementRsvp(id: String, status: String, guestCount: Int) {
    viewModelScope.launch {
      repository.updateAnnouncementRsvp(id, status, guestCount)
    }
  }

  fun submitEventRsvp(id: String, status: String, guestCount: Int) {
    viewModelScope.launch {
      repository.updateEventRsvp(id, status, guestCount)
    }
  }

  fun markNotificationRead(id: String) {
    viewModelScope.launch {
      repository.markNotificationAsRead(id)
    }
  }

  fun markAllNotificationsRead() {
    viewModelScope.launch {
      repository.markAllNotificationsAsRead()
    }
  }

  fun publishNewAnnouncement(announcement: AnnouncementEntity) {
    viewModelScope.launch {
      repository.createAnnouncement(announcement)
      _uiState.value = _uiState.value.copy(showCreateAnnouncement = false)
    }
  }

  fun togglePinAnnouncement(id: String, currentPin: Boolean) {
    viewModelScope.launch {
      repository.togglePinAnnouncement(id, !currentPin)
    }
  }

  fun archiveAnnouncement(id: String) {
    viewModelScope.launch {
      repository.archiveAnnouncement(id)
    }
  }

  fun updateNotificationPreference(type: String, enabled: Boolean) {
    val current = _uiState.value.parentProfile
    val updated = when (type) {
      "announcements" -> current.copy(notifyAnnouncements = enabled)
      "events" -> current.copy(notifyEvents = enabled)
      "reminders" -> current.copy(notifyReminders = enabled)
      "celebrations" -> current.copy(notifyCelebrations = enabled)
      else -> current
    }
    _uiState.value = _uiState.value.copy(parentProfile = updated)
  }
}

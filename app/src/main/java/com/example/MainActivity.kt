package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EventEntity
import com.example.ui.components.CreateParentAccountDialog
import com.example.ui.components.IvyLoginDialog
import com.example.ui.components.IvyTopHeader
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AnnouncementDetailScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.CreateAnnouncementScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IvyLoginScreen
import com.example.ui.screens.IvyResourcesScreen
import com.example.ui.screens.NoticesScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.IvyTheme
import com.example.ui.theme.IvyUrgentRed
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.IvyViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: IvyViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      IvyTheme {
        IvyApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun IvyApp(viewModel: IvyViewModel) {
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsState()
  val announcements by viewModel.announcements.collectAsState()
  val allAnnouncements by viewModel.allAnnouncements.collectAsState()
  val parentAccounts by viewModel.parentAccounts.collectAsState()
  val events by viewModel.events.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val unreadCount by viewModel.unreadCount.collectAsState()
  val syncStatus by viewModel.syncStatus.collectAsState()

  val isAmharic = uiState.language == "am"

  // Selected event for RSVP Dialog
  var rsvpDialogEvent by remember { mutableStateOf<EventEntity?>(null) }

  val currentUser = uiState.currentUserAccount

  // Back button handling when in sub-screens
  BackHandler(
    enabled = currentUser != null && (
      uiState.selectedAnnouncementId != null ||
      uiState.selectedEventId != null ||
      uiState.showNotificationsScreen ||
      uiState.showCreateAnnouncement
    )
  ) {
    when {
      uiState.showCreateAnnouncement -> viewModel.setCreateAnnouncementVisible(false)
      uiState.showNotificationsScreen -> viewModel.setNotificationsVisible(false)
      uiState.selectedAnnouncementId != null -> viewModel.selectAnnouncement(null)
      uiState.selectedEventId != null -> viewModel.selectEvent(null)
    }
  }

  // Active announcement object for detail screen
  val activeAnnouncement = uiState.selectedAnnouncementId?.let { id ->
    allAnnouncements.firstOrNull { it.id == id }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(IvyNavy)
  ) {
    when {
      // 1. When app opens or user logs out -> Login Page directly
      currentUser == null -> {
        IvyLoginScreen(
          isAmharic = isAmharic,
          errorMessage = uiState.loginErrorMessage,
          isLoading = uiState.isLoggingIn,
          onLanguageToggle = { viewModel.toggleLanguage() },
          onLogin = { username, password ->
            viewModel.login(username, password)
          },
          onCreateAccountClick = { viewModel.openCreateParentDialog() },
          modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
        )
      }

      // 2. Admin Account Logged In -> Admin Dashboard and Admin Actions
      currentUser.role == "ADMIN" -> {
        when {
          uiState.showCreateAnnouncement -> {
            CreateAnnouncementScreen(
              isAmharic = isAmharic,
              onBackClick = { viewModel.setCreateAnnouncementVisible(false) },
              onPublish = { newNotice ->
                viewModel.publishNewAnnouncement(newNotice)
              },
              modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
          }

          else -> {
            AdminDashboardScreen(
              announcements = allAnnouncements,
              parentAccounts = parentAccounts,
              isAmharic = isAmharic,
              onBackClick = { viewModel.logout() },
              onCreateAnnouncementClick = { viewModel.setCreateAnnouncementVisible(true) },
              onCreateParentClick = { viewModel.openCreateParentDialog() },
              onTogglePin = { id, currentPin -> viewModel.togglePinAnnouncement(id, currentPin) },
              onArchive = { id -> viewModel.archiveAnnouncement(id) },
              onDeleteParentAccount = { viewModel.deleteParentAccount(it) },
              onUpdateParentPassword = { user, pass -> viewModel.updateParentPassword(user, pass) },
              onLogout = { viewModel.logout() },
              syncStatus = syncStatus,
              onTriggerSync = { viewModel.triggerSync() },
              modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
          }
        }
      }

      // 3. Parent Account Logged In -> Dedicated Parent Dashboard Screen
      else -> {
        ParentDashboardScreen(
          userAccount = currentUser,
          parentProfile = uiState.parentProfile,
          announcements = announcements,
          events = events,
          notifications = notifications,
          unreadCount = unreadCount,
          isAmharic = isAmharic,
          onLanguageToggle = { viewModel.toggleLanguage() },
          onLogout = { viewModel.logout() },
          onAcknowledgeAnnouncement = { viewModel.acknowledgeAnnouncement(it) },
          onMarkNotificationRead = { viewModel.markNotificationRead(it) },
          onClearAllNotifications = { viewModel.markAllNotificationsRead() },
          modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
        )
      }
    }
  }

  // Event RSVP Modal Dialog
  if (rsvpDialogEvent != null) {
    val event = rsvpDialogEvent!!
    var selectedOption by remember(event.rsvpStatus) {
      mutableStateOf(event.rsvpStatus ?: "YES")
    }
    var guestCount by remember(event.rsvpGuestCount) {
      mutableIntStateOf(if (event.rsvpGuestCount > 0) event.rsvpGuestCount else 1)
    }

    AlertDialog(
      onDismissRequest = { rsvpDialogEvent = null },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = event.iconEmoji, fontSize = 24.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isAmharic) "የተሳትፎ ምላሽ (RSVP)" else "Event RSVP",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = IvyNavy
          )
        }
      },
      text = {
        Column {
          Text(
            text = if (isAmharic && event.titleAm.isNotBlank()) event.titleAm else event.titleEn,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = IvyNavy
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${event.displayDate} • ${event.time}",
            fontSize = 12.sp,
            color = IvyGold,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = if (isAmharic) "ልጅዎ ይሳተፋል?" else "Will your child attend?",
            fontSize = 13.sp,
            color = IvyTextSecondary
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            val options = listOf(
              "YES" to (if (isAmharic) "አዎ (እገኛለሁ)" else "Yes, Attending"),
              "NO" to (if (isAmharic) "አልችልም" else "No"),
              "NOT_SURE" to (if (isAmharic) "እርግጠኛ አይደለሁም" else "Not Sure")
            )

            options.forEach { (key, label) ->
              val isSelected = selectedOption == key
              val optColor = when (key) {
                "YES" -> IvyGreen
                "NO" -> IvyUrgentRed
                else -> IvyGold
              }

              Surface(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { selectedOption = key },
                color = if (isSelected) optColor else IvyBackground,
                border = androidx.compose.foundation.BorderStroke(
                  width = 1.dp,
                  color = if (isSelected) optColor else IvyBorder
                ),
                shape = RoundedCornerShape(8.dp)
              ) {
                Box(
                  modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else IvyTextPrimary,
                    maxLines = 1
                  )
                }
              }
            }
          }

          if (selectedOption == "YES") {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (isAmharic) "የተሳታፊዎች ብዛት፡" else "Number attending:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = IvyTextPrimary
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                  onClick = { if (guestCount > 1) guestCount-- },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text(
                  text = "$guestCount",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = IvyNavy,
                  modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(
                  onClick = { if (guestCount < 6) guestCount++ },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = "Increase")
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.submitEventRsvp(event.id, selectedOption, guestCount)
            rsvpDialogEvent = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = IvyNavy),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(text = if (isAmharic) "አረጋግጥ" else "Confirm RSVP")
        }
      },
      dismissButton = {
        TextButton(onClick = { rsvpDialogEvent = null }) {
          Text(text = if (isAmharic) "ሰርዝ" else "Cancel", color = IvyTextSecondary)
        }
      }
    )
  }

  // Toast feedback on parent account creation
  LaunchedEffect(uiState.accountCreationSuccessMessage) {
    uiState.accountCreationSuccessMessage?.let { msg ->
      Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
      viewModel.clearAccountCreationMessage()
    }
  }

  // Admin / Parent Authentication Dialog
  if (uiState.showLoginDialog) {
    IvyLoginDialog(
      isAmharic = isAmharic,
      errorMessage = uiState.loginErrorMessage,
      onDismiss = { viewModel.closeLoginDialog() },
      onLogin = { username, password ->
        viewModel.login(username, password)
      }
    )
  }

  // Create Parent Account Dialog (Add parents as you go)
  if (uiState.showCreateParentDialog) {
    CreateParentAccountDialog(
      isAmharic = isAmharic,
      onDismiss = { viewModel.closeCreateParentDialog() },
      onCreateAccount = { fullName, username, password, phone, email, childrenNames ->
        viewModel.createParentAccount(fullName, username, password, phone, email, childrenNames) { success, msg ->
          if (!success) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          }
        }
      }
    )
  }
}

@Composable
fun IvyBottomBar(
  currentTab: Int,
  isAmharic: Boolean,
  onTabSelected: (Int) -> Unit
) {
  NavigationBar(
    containerColor = Color.White,
    contentColor = IvyNavy,
    tonalElevation = 8.dp,
    modifier = Modifier.testTag("ivy_bottom_navigation")
  ) {
    val items = listOf(
      Triple(0, if (isAmharic) "መነሻ" else "Home", Icons.Default.Home to Icons.Outlined.Home),
      Triple(1, if (isAmharic) "ማስታወቂያዎች" else "Notices", Icons.Default.Campaign to Icons.Outlined.Campaign),
      Triple(2, if (isAmharic) "ካሌንደር" else "Calendar", Icons.Default.CalendarMonth to Icons.Outlined.CalendarToday),
      Triple(3, if (isAmharic) "አይቪ" else "IVY", Icons.Default.Info to Icons.Outlined.Info)
    )

    items.forEach { (index, label, iconPair) ->
      val isSelected = currentTab == index
      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(index) },
        icon = {
          Icon(
            imageVector = if (isSelected) iconPair.first else iconPair.second,
            contentDescription = label,
            modifier = Modifier.size(24.dp)
          )
        },
        label = {
          Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = IvyNavy,
          selectedTextColor = IvyNavy,
          indicatorColor = IvyNavyContainer,
          unselectedIconColor = IvyTextSecondary,
          unselectedTextColor = IvyTextSecondary
        ),
        modifier = Modifier.testTag("bottom_tab_$index")
      )
    }
  }
}

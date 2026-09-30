package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.data.model.IvyNotificationEntity
import com.example.data.model.IvyResource
import com.example.data.model.ParentProfile
import com.example.data.model.UserAccountEntity
import com.example.ui.components.AnnouncementCard
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary

@Composable
fun ParentDashboardScreen(
  userAccount: UserAccountEntity,
  parentProfile: ParentProfile,
  announcements: List<AnnouncementEntity>,
  events: List<EventEntity>,
  notifications: List<IvyNotificationEntity>,
  unreadCount: Int,
  isAmharic: Boolean,
  onLanguageToggle: () -> Unit,
  onLogout: () -> Unit,
  onAcknowledgeAnnouncement: (String) -> Unit,
  onMarkNotificationRead: (String) -> Unit,
  onClearAllNotifications: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var selectedAnnouncement by remember { mutableStateOf<AnnouncementEntity?>(null) }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .testTag("parent_dashboard_screen"),
    topBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(IvyNavy)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Surface(
              shape = CircleShape,
              color = IvyGold,
              modifier = Modifier.size(38.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.Face,
                  contentDescription = null,
                  tint = IvyNavy,
                  modifier = Modifier.size(22.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (isAmharic) "እንደምን አደሩ፣ ${userAccount.fullName}" else "Welcome, ${userAccount.fullName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = if (isAmharic) "የወላጅ ፖርታል • አይቪ የህፃናት ማቆያ" else "Parent Portal • IVY Childcare",
                fontSize = 11.sp,
                color = IvyGold
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
              onClick = onLanguageToggle,
              modifier = Modifier.testTag("parent_language_toggle")
            ) {
              Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Switch Language",
                tint = Color.White
              )
            }

            IconButton(
              onClick = onLogout,
              modifier = Modifier.testTag("parent_top_logout_btn")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Sign Out",
                tint = Color.White.copy(alpha = 0.9f)
              )
            }
          }
        }

        // Child banner if children assigned
        if (userAccount.childrenNames.isNotBlank()) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = IvyGold,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isAmharic) "የተመዘገበ ልጅ: ${userAccount.childrenNames}" else "Enrolled: ${userAccount.childrenNames}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
              )
            }
          }
        }
      }
    },
    bottomBar = {
      NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp
      ) {
        NavigationBarItem(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          icon = { Icon(Icons.Default.Campaign, contentDescription = "Announcements") },
          label = { Text(if (isAmharic) "ማስታወቂያዎች" else "Notices", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = IvyNavy,
            selectedTextColor = IvyNavy,
            indicatorColor = IvyGold.copy(alpha = 0.25f)
          )
        )
        NavigationBarItem(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          icon = { Icon(Icons.Default.DateRange, contentDescription = "Events") },
          label = { Text(if (isAmharic) "መርሃ-ግብር" else "Calendar", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = IvyNavy,
            selectedTextColor = IvyNavy,
            indicatorColor = IvyGold.copy(alpha = 0.25f)
          )
        )
        NavigationBarItem(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          icon = {
            BadgedBox(
              badge = {
                if (unreadCount > 0) {
                  Badge(containerColor = IvyGold) {
                    Text("$unreadCount", color = IvyNavy, fontWeight = FontWeight.Bold)
                  }
                }
              }
            ) {
              Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
          },
          label = { Text(if (isAmharic) "መልዕክቶች" else "Alerts", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = IvyNavy,
            selectedTextColor = IvyNavy,
            indicatorColor = IvyGold.copy(alpha = 0.25f)
          )
        )
        NavigationBarItem(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          icon = { Icon(Icons.Default.MenuBook, contentDescription = "Resources") },
          label = { Text(if (isAmharic) "መረጃ" else "Resources", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = IvyNavy,
            selectedTextColor = IvyNavy,
            indicatorColor = IvyGold.copy(alpha = 0.25f)
          )
        )
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(IvyBackground)
    ) {
      when (selectedTab) {
        0 -> {
          // Tab 0: Announcements
          ParentAnnouncementsTab(
            announcements = announcements,
            isAmharic = isAmharic,
            onAcknowledge = onAcknowledgeAnnouncement,
            onSelect = { selectedAnnouncement = it }
          )
        }
        1 -> {
          // Tab 1: Calendar / Events
          CalendarScreen(
            events = events,
            isAmharic = isAmharic,
            onEventClick = {},
            onRsvpClick = {}
          )
        }
        2 -> {
          // Tab 2: Notifications
          NotificationsScreen(
            notifications = notifications,
            isAmharic = isAmharic,
            onBackClick = { selectedTab = 0 },
            onNotificationClick = { onMarkNotificationRead(it.id) },
            onMarkAllAsRead = onClearAllNotifications
          )
        }
        3 -> {
          // Tab 3: Resources & Profile
          IvyResourcesScreen(
            resources = emptyList(),
            parentProfile = parentProfile.copy(
              username = userAccount.username,
              name = userAccount.fullName,
              phone = userAccount.phone,
              email = userAccount.email,
              isAdmin = false
            ),
            isAmharic = isAmharic,
            onLanguageToggle = onLanguageToggle,
            onNotificationPreferenceChange = { _, _ -> },
            onOpenAdminDashboard = null,
            onLogout = onLogout
          )
        }
      }

      // Detailed View Dialog if parent clicks an announcement
      selectedAnnouncement?.let { notice ->
        AlertDialog(
          onDismissRequest = { selectedAnnouncement = null },
          title = {
            Text(
              text = if (isAmharic && notice.titleAm.isNotBlank()) notice.titleAm else notice.titleEn,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )
          },
          text = {
            Column {
              Text(
                text = if (isAmharic && notice.bodyAm.isNotBlank()) notice.bodyAm else notice.bodyEn,
                style = MaterialTheme.typography.bodyMedium,
                color = IvyTextPrimary,
                lineHeight = 22.sp
              )
              if (notice.requiresAcknowledgment && !notice.isAcknowledged) {
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                  onClick = {
                    onAcknowledgeAnnouncement(notice.id)
                    selectedAnnouncement = null
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = IvyGreen),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (isAmharic) "ተረድቻለሁ / አረጋግጥ" else "I Acknowledge / Confirm",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          },
          confirmButton = {
            TextButton(onClick = { selectedAnnouncement = null }) {
              Text(if (isAmharic) "ዝጋ" else "Close", color = IvyNavy, fontWeight = FontWeight.Bold)
            }
          }
        )
      }
    }
  }
}

@Composable
private fun ParentAnnouncementsTab(
  announcements: List<AnnouncementEntity>,
  isAmharic: Boolean,
  onAcknowledge: (String) -> Unit,
  onSelect: (AnnouncementEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("All") }
  val categories = listOf("All", "Emergency", "Academic", "Event", "General")

  val filtered = remember(announcements, selectedCategory) {
    if (selectedCategory == "All") announcements
    else announcements.filter { it.category.equals(selectedCategory, ignoreCase = true) }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Filter Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        categories.forEach { cat ->
          val isSelected = selectedCategory == cat
          FilterChip(
            selected = isSelected,
            onClick = { selectedCategory = cat },
            label = {
              Text(
                text = when (cat) {
                  "All" -> if (isAmharic) "ሁሉም" else "All"
                  "Emergency" -> if (isAmharic) "አስቸኳይ" else "Urgent"
                  "Academic" -> if (isAmharic) "ትምህርት" else "Curriculum"
                  "Event" -> if (isAmharic) "ዝግጅቶች" else "Events"
                  else -> if (isAmharic) "አጠቃላይ" else "General"
                },
                fontSize = 11.sp
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = IvyNavy,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    if (filtered.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = IvyNavy.copy(alpha = 0.4f),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = if (isAmharic) "በአሁኑ ጊዜ አዲስ ማስታወቂያ የለም" else "No Announcements Yet",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = IvyTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (isAmharic) "ትምህርት ቤቱ አዲስ ማስታወቂያ ሲለጥፍ እዚህ ይታያል" else "New school notices will appear here once published by admin.",
              fontSize = 12.sp,
              color = IvyTextSecondary
            )
          }
        }
      }
    } else {
      items(filtered, key = { it.id }) { notice ->
        AnnouncementCard(
          announcement = notice,
          isAmharic = isAmharic,
          onClick = { onSelect(notice) }
        )
      }
    }
  }
}

package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.example.data.model.UserAccountEntity
import com.example.ui.components.ParentAccountDetailsDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AnnouncementEntity
import com.example.ui.components.AnnouncementCard
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGoldContainer
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyGreenContainer
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyDark
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.IvyUrgentContainer
import com.example.ui.theme.IvyUrgentRed
import com.example.ui.theme.ivyTextFieldColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  announcements: List<AnnouncementEntity>,
  parentAccounts: List<UserAccountEntity> = emptyList(),
  isAmharic: Boolean,
  onBackClick: () -> Unit,
  onCreateAnnouncementClick: () -> Unit,
  onCreateParentClick: () -> Unit = {},
  onTogglePin: (String, Boolean) -> Unit,
  onArchive: (String) -> Unit,
  onDeleteParentAccount: (String) -> Unit = {},
  onUpdateParentPassword: (String, String) -> Unit = { _, _ -> },
  onLogout: () -> Unit = {},
  syncStatus: com.example.data.sync.SyncStatus = com.example.data.sync.SyncStatus.IDLE,
  onTriggerSync: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var parentSearchQuery by remember { mutableStateOf("") }
  var selectedParentForDetails by remember { mutableStateOf<UserAccountEntity?>(null) }

  if (selectedParentForDetails != null) {
    ParentAccountDetailsDialog(
      account = selectedParentForDetails!!,
      isAmharic = isAmharic,
      onDismiss = { selectedParentForDetails = null },
      onDeleteAccount = { username ->
        onDeleteParentAccount(username)
        selectedParentForDetails = null
      },
      onChangePassword = { username, newPass ->
        onUpdateParentPassword(username, newPass)
      }
    )
  }

  Scaffold(
    topBar = {
      Column {
        TopAppBar(
          title = {
            Column {
              Text(
                text = if (isAmharic) "የአይቪ አስተዳዳሪ ዳሽቦርድ" else "IVY Communication Admin",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = IvyGold
                ) {
                  Text(
                    text = if (isAmharic) "አስተዳዳሪ" else "ADMINISTRATOR",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = IvyNavy,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.width(6.dp))
                // Cloud Sync Status Pill
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = when (syncStatus) {
                    com.example.data.sync.SyncStatus.SYNCING -> IvyGold.copy(alpha = 0.25f)
                    com.example.data.sync.SyncStatus.SUCCESS -> IvyGreen.copy(alpha = 0.25f)
                    com.example.data.sync.SyncStatus.ERROR -> Color.Red.copy(alpha = 0.25f)
                    com.example.data.sync.SyncStatus.IDLE -> Color.White.copy(alpha = 0.15f)
                  }
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    val statusText = when (syncStatus) {
                      com.example.data.sync.SyncStatus.SYNCING -> if (isAmharic) "በማመሳሰል ላይ..." else "Syncing..."
                      com.example.data.sync.SyncStatus.SUCCESS -> if (isAmharic) "ደመና ዝግጁ" else "Firestore Live"
                      com.example.data.sync.SyncStatus.ERROR -> if (isAmharic) "ስህተት" else "Sync Error"
                      com.example.data.sync.SyncStatus.IDLE -> if (isAmharic) "ደመና ዝግጁ" else "Cloud Ready"
                    }
                    Text(
                      text = statusText,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      color = when (syncStatus) {
                        com.example.data.sync.SyncStatus.ERROR -> Color(0xFFFF8A80)
                        com.example.data.sync.SyncStatus.SUCCESS -> Color(0xFFA7F3D0)
                        else -> Color.White
                      }
                    )
                  }
                }
              }
            }
          },
          navigationIcon = {
            IconButton(onClick = onBackClick) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
          },
          actions = {
            IconButton(
              onClick = onTriggerSync,
              modifier = Modifier.testTag("admin_sync_button")
            ) {
              Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Sync with Firestore",
                tint = if (syncStatus == com.example.data.sync.SyncStatus.SYNCING) IvyGold else Color.White
              )
            }
            IconButton(
              onClick = {
                onLogout()
                onBackClick()
              }
            ) {
              Icon(Icons.Default.Logout, contentDescription = "Log Out", tint = Color.White)
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = IvyNavy)
        )

        // Tab Row: Notices vs Parent Accounts
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = IvyNavy,
          contentColor = Color.White,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = IvyGold,
              height = 3.dp
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = {
              Text(
                text = if (isAmharic) "ማስታወቂያዎች (${announcements.size})" else "Notices (${announcements.size})",
                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedTab == 0) IvyGold else Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
              )
            }
          )

          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = {
              Text(
                text = if (isAmharic) "የወላጆች አካውንቶች (${parentAccounts.size})" else "Parent Accounts (${parentAccounts.size})",
                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedTab == 1) IvyGold else Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
              )
            }
          )
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          if (selectedTab == 0) onCreateAnnouncementClick() else onCreateParentClick()
        },
        containerColor = IvyGreen,
        contentColor = Color.White,
        modifier = Modifier.testTag("admin_create_fab")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (selectedTab == 0) Icons.Default.Add else Icons.Default.PersonAdd,
            contentDescription = "Create"
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (selectedTab == 0) {
              if (isAmharic) "አዲስ ማስታወቂያ" else "New Notice"
            } else {
              if (isAmharic) "አዲስ ወላጅ" else "Add Parent"
            },
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(IvyBackground)
        .padding(innerPadding)
        .testTag("admin_dashboard_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      if (selectedTab == 0) {
        // Key Analytics Cards (Section 25 & 30)
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          AdminMetricCard(
            title = if (isAmharic) "የተመዘገቡ ወላጆች" else "Active Parents",
            value = "126",
            subtitle = if (isAmharic) "100% ደርሷል" else "100% reachable",
            color = IvyNavy,
            modifier = Modifier.weight(1f)
          )

          AdminMetricCard(
            title = if (isAmharic) "የማንበብ መጠን" else "Read Rate",
            value = "93.7%",
            subtitle = if (isAmharic) "ከፍተኛ ተሳትፎ" else "High engagement",
            color = IvyGreen,
            modifier = Modifier.weight(1f)
          )

          AdminMetricCard(
            title = if (isAmharic) "የማረጋገጫ መጠን" else "Ack. Rate",
            value = "92.9%",
            subtitle = if (isAmharic) "አስፈላጊ ማስታወቂያ" else "Compliance",
            color = IvyGold,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Published Notices Management Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isAmharic) "የማስታወቂያዎች አስተዳደርና ስታቲስቲክስ" else "Published Notices & Engagement",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = IvyNavy
          )
          Text(
            text = "${announcements.size} " + if (isAmharic) "ማስታወቂያዎች" else "total",
            fontSize = 12.sp,
            color = IvyTextSecondary
          )
        }
      }

      // List of Announcements with Engagement Stats
      items(announcements, key = { it.id }) { item ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_notice_card_${item.id}"),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Surface(
                  color = IvyNavyContainer,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = item.category.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                if (item.priority != "NORMAL") {
                  PriorityBadge(priority = item.priority, isAmharic = isAmharic)
                }
              }

              Text(
                text = item.publishDate,
                fontSize = 11.sp,
                color = IvyTextMuted
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = item.titleEn,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = IvyNavyDark
            )

            if (item.titleAm.isNotBlank()) {
              Text(
                text = item.titleAm,
                fontSize = 13.sp,
                color = IvyTextSecondary
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Real-time Delivery & Engagement Metrics (Section 30)
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .background(IvyBackground, RoundedCornerShape(8.dp))
                .padding(10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (isAmharic) "የማንበብ ሁኔታ (Read)" else "Opened & Read",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = IvyNavy
                )
                Text(
                  text = "118 / 126 parents (93.7%)",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = IvyGreen
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              LinearProgressIndicator(
                progress = { 0.937f },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(5.dp),
                color = IvyGreen,
                trackColor = IvyBorder,
              )

              if (item.requiresAcknowledgment) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = if (isAmharic) "የተረጋገጠ (Acknowledged)" else "Acknowledged",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IvyNavy
                  )
                  Text(
                    text = if (item.isAcknowledged) "117 / 126 (92.9%)" else "98 / 126 (77.8%)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = IvyGold
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                  progress = { if (item.isAcknowledged) 0.929f else 0.778f },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp),
                  color = IvyGold,
                  trackColor = IvyBorder,
                )
              }

              if (item.enableRSVP) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = if (isAmharic) "የተመዘገቡ ተሳታፊዎች" else "RSVP Attending",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IvyNavy
                  )
                  Text(
                    text = "84 children attending",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons (Pin & Archive)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End,
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedButton(
                onClick = { onTogglePin(item.id, item.isPinned) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  contentColor = if (item.isPinned) IvyGold else IvyTextSecondary
                )
              ) {
                Icon(
                  imageVector = Icons.Default.PushPin,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (item.isPinned) {
                    if (isAmharic) "ተሰክቷል" else "Pinned"
                  } else {
                    if (isAmharic) "ለጥፍ" else "Pin"
                  },
                  fontSize = 11.sp
                )
              }

              Spacer(modifier = Modifier.width(8.dp))

              OutlinedButton(
                onClick = { onArchive(item.id) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = IvyTextSecondary)
              ) {
                Icon(
                  imageVector = Icons.Default.Archive,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (item.isArchived) {
                    if (isAmharic) "የማህደር ሰነድ" else "Archived"
                  } else {
                    if (isAmharic) "ወደ ማህደር" else "Archive"
                  },
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }
    } else {
      // Tab 1: Parent Accounts Management (Section requested by user: "I want to create more accounts for parents as i go")
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          AdminMetricCard(
            title = if (isAmharic) "የተመዘገቡ ወላጆች" else "Total Parents",
            value = "${parentAccounts.size}",
            subtitle = if (isAmharic) "አካውንቶች ተፈጥረዋል" else "Active accounts",
            color = IvyNavy,
            modifier = Modifier.weight(1f)
          )

          AdminMetricCard(
            title = if (isAmharic) "ደህንነት" else "Access Level",
            value = if (isAmharic) "ንቁ" else "Admin",
            subtitle = if (isAmharic) "ሙሉ ፈቃድ" else "Authorized",
            color = IvyGold,
            modifier = Modifier.weight(1f)
          )

          AdminMetricCard(
            title = if (isAmharic) "የልጆች ማቆያ" else "Portal Status",
            value = "Ready",
            subtitle = if (isAmharic) "ወላጅ መጨመር ይቻላል" else "Add as you go",
            color = IvyGreen,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // "+ Add Parent Account" Banner Card
      item {
        Card(
          onClick = onCreateParentClick,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_add_parent_banner"),
          colors = CardDefaults.cardColors(containerColor = IvyGreen),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = CircleShape,
              color = Color.White.copy(alpha = 0.2f),
              modifier = Modifier.size(44.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  Icons.Default.PersonAdd,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (isAmharic) "+ አዲስ የወላጅ አካውንት መዝግብ" else "+ Create New Parent Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = if (isAmharic) "የተጠቃሚ ስም፣ የይለፍ ቃል እና የልጆችን መረጃ አስገብተው ይመዝግቡ" else "Add parents as you go with credentials and child details",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
              )
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
          }
        }
      }

      // Search Parents
      item {
        OutlinedTextField(
          value = parentSearchQuery,
          onValueChange = { parentSearchQuery = it },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("search_parents_input"),
          placeholder = { Text(if (isAmharic) "በወላጅ ወይም በልጅ ስም ፈልግ..." else "Search parents by name, username, or child...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IvyNavy) },
          trailingIcon = {
            if (parentSearchQuery.isNotBlank()) {
              IconButton(onClick = { parentSearchQuery = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear", tint = IvyTextMuted)
              }
            }
          },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 14.sp),
          shape = RoundedCornerShape(12.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )
      }

      val filteredParents = parentAccounts.filter { account ->
        parentSearchQuery.isBlank() ||
          account.fullName.contains(parentSearchQuery, ignoreCase = true) ||
          account.username.contains(parentSearchQuery, ignoreCase = true) ||
          account.childrenNames.contains(parentSearchQuery, ignoreCase = true)
      }

      if (filteredParents.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = if (isAmharic) "ምንም የወላጅ አካውንት አልተገኘም" else "No parent accounts found",
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = if (isAmharic) "ከላይ ያለውን ቁልፍ በመጫን አዲስ ወላጅ መመዝገብ ይችላሉ" else "Click '+ Create New Parent Account' above to add your first parent!",
                fontSize = 12.sp,
                color = IvyTextSecondary
              )
            }
          }
        }
      } else {
        items(filteredParents, key = { it.username }) { account ->
          ParentAccountAdminCard(
            account = account,
            isAmharic = isAmharic,
            onViewDetails = { selectedParentForDetails = account },
            onDelete = { onDeleteParentAccount(account.username) }
          )
        }
      }
    }
  }
}
}

@Composable
fun ParentAccountAdminCard(
  account: UserAccountEntity,
  isAmharic: Boolean,
  onViewDetails: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    onClick = onViewDetails,
    modifier = modifier
      .fillMaxWidth()
      .testTag("parent_account_card_${account.username}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = IvyNavyContainer,
            modifier = Modifier.size(40.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = account.fullName.take(1).uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = IvyNavy
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = account.fullName,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = IvyTextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = IvyGreen.copy(alpha = 0.12f)
              ) {
                Text(
                  text = "@${account.username}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = IvyGreen,
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                )
              }
              if (account.phone.isNotBlank()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = account.phone,
                  fontSize = 11.sp,
                  color = IvyTextSecondary
                )
              }
            }
          }
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = IvyGoldContainer
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Key, contentDescription = null, tint = IvyNavy, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = account.password,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )
          }
        }
      }

      if (account.childrenNames.isNotBlank()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = if (isAmharic) "ልጆች: " else "Children: ",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = IvyNavy
          )
          Text(
            text = account.childrenNames,
            fontSize = 12.sp,
            color = IvyTextSecondary,
            maxLines = 1
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      HorizontalDivider(color = IvyBorder)
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isAmharic) "የወላጅ ፖርታል አካውንት" else "Parent Portal Account",
          fontSize = 11.sp,
          color = IvyTextMuted
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          OutlinedButton(
            onClick = onViewDetails,
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isAmharic) "መረጃና ማጋራት" else "Credentials & Share",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AdminMetricCard(
  title: String,
  value: String,
  subtitle: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = Color.White),
    shape = RoundedCornerShape(10.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = IvyTextSecondary,
        maxLines = 1
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        color = color
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = IvyTextMuted,
        maxLines = 1
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnnouncementScreen(
  isAmharic: Boolean,
  onBackClick: () -> Unit,
  onPublish: (AnnouncementEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  var titleEn by remember { mutableStateOf("") }
  var titleAm by remember { mutableStateOf("") }
  var bodyEn by remember { mutableStateOf("") }
  var bodyAm by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Celebrations") }
  var priority by remember { mutableStateOf("NORMAL") }
  var audience by remember { mutableStateOf("ALL") }
  var isPinned by remember { mutableStateOf(false) }
  var requiresAck by remember { mutableStateOf(false) }
  var enableRsvp by remember { mutableStateOf(false) }
  var eventDate by remember { mutableStateOf("") }

  var currentStep by remember { mutableIntStateOf(1) } // 1: Content, 2: Options & Audience, 3: Preview

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (isAmharic) "አዲስ ማስታወቂያ ማዘጋጀት" else "Create Announcement",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = IvyNavy)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .background(IvyBackground)
        .padding(innerPadding)
    ) {
      // Wizard Steps Indicator
      Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          WizardStepItem(step = 1, label = if (isAmharic) "ይዘት" else "Content", isActive = currentStep == 1, isCompleted = currentStep > 1)
          WizardStepItem(step = 2, label = if (isAmharic) "ዒላማና አማራጮች" else "Audience", isActive = currentStep == 2, isCompleted = currentStep > 2)
          WizardStepItem(step = 3, label = if (isAmharic) "ቅድመ ዕይታ" else "Preview", isActive = currentStep == 3, isCompleted = false)
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        when (currentStep) {
          1 -> {
            // STEP 1: Content
            item {
              Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
              ) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Text(
                    text = if (isAmharic) "የማስታወቂያው ዝርዝር መረጃ" else "Announcement Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy
                  )

                  Spacer(modifier = Modifier.height(12.dp))

                  // Title English
                  OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("Title (English) *") },
                    placeholder = { Text("e.g. Center Closure Notice") },
                    singleLine = true,
                    textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier
                      .fillMaxWidth()
                      .testTag("admin_title_en_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ivyTextFieldColors(containerColor = Color.White)
                  )

                  Spacer(modifier = Modifier.height(10.dp))

                  // Title Amharic
                  OutlinedTextField(
                    value = titleAm,
                    onValueChange = { titleAm = it },
                    label = { Text("ርዕስ (አማርኛ) *") },
                    placeholder = { Text("ለምሳሌ፡ የማዕከሉ የእረፍት ማስታወቂያ") },
                    singleLine = true,
                    textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier
                      .fillMaxWidth()
                      .testTag("admin_title_am_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ivyTextFieldColors(containerColor = Color.White)
                  )

                  Spacer(modifier = Modifier.height(14.dp))

                  // Category Selector
                  Text(
                    text = if (isAmharic) "ምድብ ይምረጡ" else "Category",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy
                  )
                  Spacer(modifier = Modifier.height(6.dp))
                  val categories = listOf("Celebrations", "Closures", "Events", "Reminders", "What to Bring", "Meals", "Important")
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    categories.take(4).forEach { cat ->
                      Surface(
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .clickable { category = cat },
                        color = if (category == cat) IvyNavy else IvyNavyContainer,
                        shape = RoundedCornerShape(6.dp)
                      ) {
                        Text(
                          text = cat,
                          fontSize = 11.sp,
                          fontWeight = FontWeight.Bold,
                          color = if (category == cat) Color.White else IvyNavy,
                          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                      }
                    }
                  }

                  Spacer(modifier = Modifier.height(14.dp))

                  // Priority Selector
                  Text(
                    text = if (isAmharic) "የአስፈላጊነት ደረጃ" else "Priority Level",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy
                  )
                  Spacer(modifier = Modifier.height(6.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    PrioritySelectChip(
                      label = "NORMAL",
                      selected = priority == "NORMAL",
                      color = IvyNavy,
                      onClick = { priority = "NORMAL" },
                      modifier = Modifier.weight(1f)
                    )
                    PrioritySelectChip(
                      label = "IMPORTANT",
                      selected = priority == "IMPORTANT",
                      color = IvyGold,
                      onClick = { priority = "IMPORTANT" },
                      modifier = Modifier.weight(1f)
                    )
                    PrioritySelectChip(
                      label = "URGENT",
                      selected = priority == "URGENT",
                      color = IvyUrgentRed,
                      onClick = { priority = "URGENT" },
                      modifier = Modifier.weight(1f)
                    )
                  }

                  Spacer(modifier = Modifier.height(14.dp))

                  // Body English
                  OutlinedTextField(
                    value = bodyEn,
                    onValueChange = { bodyEn = it },
                    label = { Text("Announcement Body (English) *") },
                    textStyle = TextStyle(color = IvyTextPrimary, fontSize = 14.sp),
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(120.dp)
                      .testTag("admin_body_en_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ivyTextFieldColors(containerColor = Color.White)
                  )

                  Spacer(modifier = Modifier.height(10.dp))

                  // Body Amharic
                  OutlinedTextField(
                    value = bodyAm,
                    onValueChange = { bodyAm = it },
                    label = { Text("የማስታወቂያው ዝርዝር መልዕክት (አማርኛ) *") },
                    textStyle = TextStyle(color = IvyTextPrimary, fontSize = 14.sp),
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(120.dp)
                      .testTag("admin_body_am_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ivyTextFieldColors(containerColor = Color.White)
                  )
                }
              }
            }

            item {
              Button(
                onClick = { currentStep = 2 },
                enabled = titleEn.isNotBlank() || titleAm.isNotBlank(),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("admin_next_step_1_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = IvyNavy),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(text = if (isAmharic) "ቀጣይ ደረጃ (ዒላማና አማራጮች) →" else "Next: Audience & Controls →")
              }
            }
          }

          2 -> {
            // STEP 2: Audience & Publishing Controls
            item {
              Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
              ) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Text(
                    text = if (isAmharic) "የማስታወቂያው ዒላማ ወላጆች" else "Audience Targeting",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy
                  )

                  Spacer(modifier = Modifier.height(10.dp))

                  val audiences = listOf("ALL", "PRESCHOOL", "TODDLER", "INFANT")
                  audiences.forEach { aud ->
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable { audience = aud }
                        .padding(vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Checkbox(
                        checked = audience == aud,
                        onCheckedChange = { audience = aud },
                        colors = CheckboxDefaults.colors(checkedColor = IvyNavy)
                      )
                      Text(
                        text = when (aud) {
                          "ALL" -> if (isAmharic) "ሁሉም ወላጆች (All Parents)" else "All Parents & Guardians"
                          "PRESCHOOL" -> if (isAmharic) "ቅድመ-ትምህርት ቤት (Preschool)" else "Preschool Class"
                          "TODDLER" -> if (isAmharic) "ታዳጊ ህፃናት (Toddler Group)" else "Toddler Group"
                          else -> if (isAmharic) "ጨቅላ ህፃናት (Infants)" else "Infant Care"
                        },
                        fontSize = 13.sp,
                        color = IvyTextPrimary
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(16.dp))

                  Text(
                    text = if (isAmharic) "ልዩ አማራጮች" else "Publishing Options",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy
                  )

                  Spacer(modifier = Modifier.height(8.dp))

                  // Pin Switch
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(text = if (isAmharic) "ከላይ ይሰካ (Pin to Top)" else "Pin to Top", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                      Text(text = if (isAmharic) "በመነሻ ገጽ ላይ ጎልቶ እንዲታይ" else "Display prominently at top of Home feed", fontSize = 11.sp, color = IvyTextSecondary)
                    }
                    Switch(checked = isPinned, onCheckedChange = { isPinned = it })
                  }

                  // Acknowledgment Switch
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(text = if (isAmharic) "የወላጅ ማረጋገጫ ይጠይቃል" else "Require Acknowledgment", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                      Text(text = if (isAmharic) "ወላጁ ማንበቡን ማረጋገጥ አለበት" else "Parent must click 'I Have Read This Notice'", fontSize = 11.sp, color = IvyTextSecondary)
                    }
                    Switch(checked = requiresAck, onCheckedChange = { requiresAck = it })
                  }

                  // RSVP Switch
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(text = if (isAmharic) "የተሳትፎ ምላሽ (Enable RSVP)" else "Enable RSVP", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                      Text(text = if (isAmharic) "ወላጆች መገኘታቸውን እንዲያሳውቁ" else "Allow parents to confirm attendance", fontSize = 11.sp, color = IvyTextSecondary)
                    }
                    Switch(checked = enableRsvp, onCheckedChange = { enableRsvp = it })
                  }
                }
              }
            }

            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                OutlinedButton(
                  onClick = { currentStep = 1 },
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(text = if (isAmharic) "← ተመለስ" else "← Back")
                }

                Button(
                  onClick = { currentStep = 3 },
                  modifier = Modifier.weight(1f),
                  colors = ButtonDefaults.buttonColors(containerColor = IvyNavy),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(text = if (isAmharic) "ቅድመ ዕይታ →" else "Preview →")
                }
              }
            }
          }

          3 -> {
            // STEP 3: Preview & Publish
            item {
              Text(
                text = if (isAmharic) "ወላጆች የሚያዩት ቅድመ ዕይታ" else "Live Parent Preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
            }

            item {
              val previewItem = AnnouncementEntity(
                id = "preview_${UUID.randomUUID()}",
                titleEn = titleEn.ifBlank { "Announcement Title Preview" },
                titleAm = titleAm,
                bodyEn = bodyEn.ifBlank { "Preview body text for IVY parents..." },
                bodyAm = bodyAm,
                category = category,
                priority = priority,
                audience = audience,
                isPinned = isPinned,
                requiresAcknowledgment = requiresAck,
                enableRSVP = enableRsvp,
                publishDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date()),
                isRead = false,
                isAcknowledged = false
              )

              AnnouncementCard(
                announcement = previewItem,
                isAmharic = isAmharic,
                onClick = {}
              )
            }

            item {
              Button(
                onClick = {
                  val newNotice = AnnouncementEntity(
                    id = "ann_${System.currentTimeMillis()}",
                    titleEn = titleEn.ifBlank { "IVY Center Announcement" },
                    titleAm = titleAm.ifBlank { "የአይቪ ማስታወቂያ" },
                    bodyEn = bodyEn.ifBlank { "Official notification from IVY Childcare Services." },
                    bodyAm = bodyAm.ifBlank { "ከአይቪ የህፃናት ማቆያ የተላለፈ መልዕክት።" },
                    category = category,
                    priority = priority,
                    audience = audience,
                    isPinned = isPinned,
                    requiresAcknowledgment = requiresAck,
                    enableRSVP = enableRsvp,
                    publishDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date()),
                    isRead = false,
                    isAcknowledged = false,
                    coverDrawableRes = if (category == "Celebrations") R.drawable.img_celebration else R.drawable.img_campus
                  )
                  onPublish(newNotice)
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(52.dp)
                  .testTag("admin_publish_announcement_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = IvyGreen),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isAmharic) "ማስታወቂያውን አትም (PUBLISH NOW)" else "PUBLISH ANNOUNCEMENT",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun WizardStepItem(
  step: Int,
  label: String,
  isActive: Boolean,
  isCompleted: Boolean
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Surface(
      shape = CircleShape,
      color = if (isCompleted) IvyGreen else if (isActive) IvyNavy else IvyBorder,
      modifier = Modifier.size(24.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        if (isCompleted) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        } else {
          Text(text = "$step", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = label,
      fontSize = 12.sp,
      fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
      color = if (isActive) IvyNavy else IvyTextSecondary
    )
  }
}

@Composable
private fun PrioritySelectChip(
  label: String,
  selected: Boolean,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() },
    color = if (selected) color else IvyBackground,
    border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) color else IvyBorder),
    shape = RoundedCornerShape(8.dp)
  ) {
    Box(
      modifier = Modifier.padding(vertical = 8.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = if (selected) Color.White else IvyTextPrimary
      )
    }
  }
}

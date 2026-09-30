package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IvyNotificationEntity
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.IvyUrgentRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
  notifications: List<IvyNotificationEntity>,
  isAmharic: Boolean,
  onBackClick: () -> Unit,
  onNotificationClick: (IvyNotificationEntity) -> Unit,
  onMarkAllAsRead: () -> Unit,
  modifier: Modifier = Modifier
) {
  val groupedNotifications = remember(notifications) {
    notifications.groupBy { it.groupTime }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (isAmharic) "ማሳወቂያዎች" else "Notifications",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        actions = {
          TextButton(onClick = onMarkAllAsRead) {
            Icon(
              imageVector = Icons.Default.DoneAll,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isAmharic) "ሁሉንም አንብብ" else "Mark all read",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = IvyNavy)
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(IvyBackground)
        .padding(innerPadding)
        .testTag("notifications_list"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (notifications.isEmpty()) {
        item {
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 40.dp),
            color = Color.White,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
          ) {
            Column(
              modifier = Modifier.padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = IvyTextMuted,
                modifier = Modifier.size(40.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = if (isAmharic) "ምንም ማሳወቂያ የለም" else "No notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (isAmharic) "አዲስ ማስታወቂያ ወይም ክስተት ሲኖር እዚህ ያዩታል" else "You will be alerted here when new updates arrive.",
                style = MaterialTheme.typography.bodySmall,
                color = IvyTextSecondary
              )
            }
          }
        }
      } else {
        listOf("TODAY", "YESTERDAY", "EARLIER").forEach { groupKey ->
          val itemsInGroup = groupedNotifications[groupKey] ?: emptyList()
          if (itemsInGroup.isNotEmpty()) {
            item {
              Text(
                text = when (groupKey) {
                  "TODAY" -> if (isAmharic) "ዛሬ" else "TODAY"
                  "YESTERDAY" -> if (isAmharic) "ትናንት" else "YESTERDAY"
                  else -> if (isAmharic) "ቀደም ብሎ" else "EARLIER"
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 4.dp)
              )
            }

            items(itemsInGroup, key = { it.id }) { item ->
              NotificationRowItem(
                item = item,
                isAmharic = isAmharic,
                onClick = { onNotificationClick(item) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun NotificationRowItem(
  item: IvyNotificationEntity,
  isAmharic: Boolean,
  onClick: () -> Unit
) {
  val title = if (isAmharic && item.titleAm.isNotBlank()) item.titleAm else item.titleEn
  val message = if (isAmharic && item.messageAm.isNotBlank()) item.messageAm else item.messageEn

  Card(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("notification_item_${item.id}"),
    colors = CardDefaults.cardColors(
      containerColor = if (!item.isRead) Color.White else IvyBackground
    ),
    shape = RoundedCornerShape(10.dp),
    border = androidx.compose.foundation.BorderStroke(
      width = 1.dp,
      color = if (!item.isRead) IvyNavyContainer else IvyBorder
    )
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Surface(
        color = when (item.type) {
          "URGENT" -> IvyUrgentRed
          "EVENT_REMINDER" -> IvyGreen
          "RSVP_REMINDER" -> IvyGold
          else -> IvyNavy
        },
        shape = CircleShape,
        modifier = Modifier.size(36.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = when (item.type) {
              "URGENT" -> Icons.Default.Error
              "EVENT_REMINDER" -> Icons.Default.CalendarMonth
              "RSVP_REMINDER" -> Icons.Default.HowToReg
              else -> Icons.Default.Campaign
            },
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (!item.isRead) FontWeight.Bold else FontWeight.SemiBold,
            color = IvyNavy,
            modifier = Modifier.weight(1f)
          )

          Text(
            text = item.timeDisplay,
            style = MaterialTheme.typography.bodySmall,
            color = IvyTextMuted,
            fontSize = 11.sp
          )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = message,
          style = MaterialTheme.typography.bodySmall,
          color = IvyTextSecondary,
          lineHeight = 18.sp
        )
      }

      if (!item.isRead) {
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
          shape = CircleShape,
          color = IvyUrgentRed,
          modifier = Modifier
            .size(8.dp)
            .align(Alignment.CenterVertically)
        ) {}
      }
    }
  }
}

package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGoldContainer
import com.example.ui.theme.IvyGoldDark
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyGreenContainer
import com.example.ui.theme.IvyImportantContainer
import com.example.ui.theme.IvyImportantOrange
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyDark
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyOnGoldContainer
import com.example.ui.theme.IvyOnGreenContainer
import com.example.ui.theme.IvyOnNavyContainer
import com.example.ui.theme.IvyOnUrgentContainer
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.IvyUrgentContainer
import com.example.ui.theme.IvyUrgentRed

@Composable
fun IvyTopHeader(
  parentName: String,
  childrenText: String,
  isAmharic: Boolean,
  unreadCount: Int,
  isAdmin: Boolean = false,
  onLanguageToggle: () -> Unit,
  onNotificationsClick: () -> Unit,
  onAccountClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = IvyNavy,
    shadowElevation = 3.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier
          .weight(1f)
          .then(if (onAccountClick != null) Modifier.clickable { onAccountClick() } else Modifier)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (isAdmin) {
              if (isAmharic) "ሰላም፣ አስተዳዳሪ 👑" else "IVY Administration 👑"
            } else {
              if (isAmharic) "እንደምን አደሩ፣ $parentName 👋" else "Welcome, $parentName 👋"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          if (isAdmin) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = IvyGold
            ) {
              Text(
                text = "ADMIN",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = IvyNavy,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (isAdmin) {
              if (isAmharic) "የአይቪ አስተዳደር ኮንሶል" else "Official IVY Admin Console"
            } else {
              if (isAmharic) "ከአይቪ (IVY) ጋር እንደተገናኙ ይቆዩ" else "Stay connected with IVY"
            },
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.85f)
          )
          if (!isAdmin && childrenText.isNotBlank()) {
            Text(
              text = " • $childrenText",
              style = MaterialTheme.typography.bodySmall,
              color = IvyGold,
              fontWeight = FontWeight.Medium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Account Switch / Login Pill
        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onAccountClick?.invoke() }
            .testTag("account_login_button"),
          color = if (isAdmin) IvyGold else Color.White.copy(alpha = 0.15f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
              contentDescription = "Account",
              tint = if (isAdmin) IvyNavy else Color.White,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isAdmin) "Admin" else if (isAmharic) "መለያ" else "Login",
              color = if (isAdmin) IvyNavy else Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Language Toggle Pill
        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onLanguageToggle() }
            .testTag("language_toggle_button"),
          color = IvyGold.copy(alpha = 0.2f),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyGold)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Translate,
              contentDescription = "Language",
              tint = IvyGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isAmharic) "አማርኛ" else "English",
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Notification Bell Icon
        IconButton(
          onClick = onNotificationsClick,
          modifier = Modifier.testTag("notification_bell_button")
        ) {
          BadgedBox(
            badge = {
              if (unreadCount > 0) {
                Badge(
                  containerColor = IvyUrgentRed,
                  contentColor = Color.White
                ) {
                  Text("$unreadCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          ) {
            Icon(
              imageVector = if (unreadCount > 0) Icons.Default.Notifications else Icons.Outlined.Notifications,
              contentDescription = "Notifications",
              tint = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
fun CategoryFilterChips(
  categories: List<String>,
  selectedCategory: String,
  isAmharic: Boolean,
  onCategorySelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    categories.forEach { category ->
      val isSelected = selectedCategory.equals(category, ignoreCase = true)
      val displayLabel = getCategoryLabel(category, isAmharic)

      FilterChip(
        selected = isSelected,
        onClick = { onCategorySelected(category) },
        label = {
          Text(
            text = displayLabel,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = IvyNavy,
          selectedLabelColor = Color.White,
          containerColor = Color.White,
          labelColor = IvyTextPrimary
        ),
        border = FilterChipDefaults.filterChipBorder(
          enabled = true,
          selected = isSelected,
          borderColor = if (isSelected) IvyNavy else IvyBorder
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.testTag("category_chip_${category.lowercase().replace(" ", "_")}")
      )
    }
  }
}

fun getCategoryLabel(category: String, isAmharic: Boolean): String {
  return when (category) {
    "All" -> if (isAmharic) "ሁሉም" else "All"
    "Celebrations" -> if (isAmharic) "🎉 በዓላት" else "🎉 Celebrations"
    "Closures" -> if (isAmharic) "🏫 የእረፍት ቀናት" else "🏫 Closures"
    "Events" -> if (isAmharic) "📅 ክስተቶች" else "📅 Events"
    "Reminders" -> if (isAmharic) "🔔 ማሳሰቢያዎች" else "🔔 Reminders"
    "What to Bring" -> if (isAmharic) "👕 ምን ይዘው ይምጡ" else "👕 What to Bring"
    "Meals" -> if (isAmharic) "🍎 ምግቦች" else "🍎 Meals"
    "Activities" -> if (isAmharic) "📚 እንቅስቃሴዎች" else "📚 Activities"
    "Important" -> if (isAmharic) "📢 አስፈላጊ" else "📢 Important"
    "General" -> if (isAmharic) "📝 አጠቃላይ" else "📝 General"
    else -> category
  }
}

@Composable
fun PriorityBadge(priority: String, isAmharic: Boolean, modifier: Modifier = Modifier) {
  when (priority.uppercase()) {
    "URGENT" -> {
      Surface(
        color = IvyUrgentContainer,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isAmharic) "🚨 አስቸኳይ" else "🚨 URGENT",
            color = IvyOnUrgentContainer,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
    "IMPORTANT" -> {
      Surface(
        color = IvyImportantContainer,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isAmharic) "📌 አስፈላጊ" else "📌 IMPORTANT",
            color = IvyImportantOrange,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun AnnouncementCard(
  announcement: AnnouncementEntity,
  isAmharic: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val title = if (isAmharic && announcement.titleAm.isNotBlank()) announcement.titleAm else announcement.titleEn
  val body = if (isAmharic && announcement.bodyAm.isNotBlank()) announcement.bodyAm else announcement.bodyEn

  Card(
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .testTag("announcement_card_${announcement.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(
      width = if (announcement.priority == "URGENT") 1.5.dp else 1.dp,
      color = if (announcement.priority == "URGENT") IvyUrgentRed else IvyBorder
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header: Category Badge, Priority Badge, Pinned Indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Category tag
          Surface(
            color = IvyNavyContainer.copy(alpha = 0.7f),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = getCategoryLabel(announcement.category, isAmharic).uppercase(),
              color = IvyNavy,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          if (announcement.priority != "NORMAL") {
            PriorityBadge(priority = announcement.priority, isAmharic = isAmharic)
          }
        }

        if (announcement.isPinned) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.PushPin,
              contentDescription = "Pinned",
              tint = IvyGold,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = if (isAmharic) "የተሰካ" else "Pinned",
              fontSize = 11.sp,
              color = IvyGold,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = IvyTextPrimary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Description snippet
      Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = IvyTextSecondary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Bottom Row: Date, Read indicator, Acknowledgment indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = announcement.publishDate,
          style = MaterialTheme.typography.bodySmall,
          color = IvyTextMuted
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (announcement.requiresAcknowledgment) {
            if (announcement.isAcknowledged) {
              Surface(
                color = IvyGreenContainer,
                shape = RoundedCornerShape(4.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = IvyGreen,
                    modifier = Modifier.size(12.dp)
                  )
                  Spacer(modifier = Modifier.width(3.dp))
                  Text(
                    text = if (isAmharic) "ተረጋግጧል" else "Acknowledged",
                    fontSize = 10.sp,
                    color = IvyOnGreenContainer,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            } else {
              Surface(
                color = IvyUrgentContainer,
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = if (isAmharic) "ማረጋገጫ ያስፈልጋል" else "Action Required",
                  fontSize = 10.sp,
                  color = IvyOnUrgentContainer,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }

          // Read status
          if (announcement.isRead) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Read",
                tint = IvyGreen,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = if (isAmharic) "ተነቧል" else "Read",
                style = MaterialTheme.typography.bodySmall,
                color = IvyGreen,
                fontWeight = FontWeight.Medium
              )
            }
          } else {
            Surface(
              shape = CircleShape,
              color = IvyUrgentRed,
              modifier = Modifier.size(8.dp)
            ) {}
          }
        }
      }
    }
  }
}

@Composable
fun PinnedAnnouncementCard(
  announcement: AnnouncementEntity,
  isAmharic: Boolean,
  onReadMore: () -> Unit,
  modifier: Modifier = Modifier
) {
  val title = if (isAmharic && announcement.titleAm.isNotBlank()) announcement.titleAm else announcement.titleEn
  val body = if (isAmharic && announcement.bodyAm.isNotBlank()) announcement.bodyAm else announcement.bodyEn

  Card(
    onClick = onReadMore,
    modifier = modifier
      .fillMaxWidth()
      .testTag("pinned_announcement_card"),
    colors = CardDefaults.cardColors(
      containerColor = if (announcement.priority == "URGENT") IvyUrgentContainer.copy(alpha = 0.35f) else IvyGoldContainer.copy(alpha = 0.35f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = RoundedCornerShape(14.dp),
    border = androidx.compose.foundation.BorderStroke(
      width = 1.5.dp,
      color = if (announcement.priority == "URGENT") IvyUrgentRed else IvyGold
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.PushPin,
            contentDescription = null,
            tint = if (announcement.priority == "URGENT") IvyUrgentRed else IvyGoldDark,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (announcement.priority == "URGENT") {
              if (isAmharic) "🚨 አስቸኳይ ማስታወቂያ" else "🚨 URGENT NOTICE"
            } else {
              if (isAmharic) "📌 አስፈላጊ ማስታወቂያ" else "📌 IMPORTANT NOTICE"
            },
            color = if (announcement.priority == "URGENT") IvyUrgentRed else IvyOnGoldContainer,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp
          )
        }

        if (announcement.requiresAcknowledgment && !announcement.isAcknowledged) {
          Surface(
            color = IvyUrgentRed,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = if (isAmharic) "ማረጋገጫ ይጠይቃል" else "Acknowledgment Needed",
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = IvyNavyDark
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = IvyTextSecondary,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = announcement.publishDate,
          style = MaterialTheme.typography.bodySmall,
          color = IvyTextSecondary,
          fontWeight = FontWeight.Medium
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (isAmharic) "ሙሉውን አንብብ →" else "Read More →",
            color = IvyNavy,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}

@Composable
fun WhatsNewCard(
  announcementsCount: Int,
  upcomingEventsCount: Int,
  isAmharic: Boolean,
  onDismiss: () -> Unit,
  onViewUpdates: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    color = IvyNavyContainer.copy(alpha = 0.5f),
    border = androidx.compose.foundation.BorderStroke(1.dp, IvyNavyContainer)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (isAmharic) "እንኳን ደህና ተመለሱ 👋" else "Welcome back 👋",
            fontWeight = FontWeight.Bold,
            color = IvyNavy,
            fontSize = 14.sp
          )
        }
        IconButton(
          onClick = onDismiss,
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Dismiss",
            tint = IvyTextMuted,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = if (isAmharic) "አዳዲስ መረጃዎች አሉዎት:" else "You have new official IVY updates:",
        fontSize = 12.sp,
        color = IvyTextSecondary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = if (isAmharic) "📢 $announcementsCount ማስታወቂያዎች" else "📢 $announcementsCount Announcements",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = IvyNavy
        )
        Text(
          text = if (isAmharic) "📅 $upcomingEventsCount ክስተቶች" else "📅 $upcomingEventsCount Upcoming Events",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = IvyNavy
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedButton(
        onClick = onViewUpdates,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = IvyNavy)
      ) {
        Text(
          text = if (isAmharic) "ሁሉንም አዘምነቶች ይመልከቱ" else "View Updates",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun EventCard(
  event: EventEntity,
  isAmharic: Boolean,
  onRsvpClick: () -> Unit,
  onCardClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val title = if (isAmharic && event.titleAm.isNotBlank()) event.titleAm else event.titleEn
  val description = if (isAmharic && event.descriptionAm.isNotBlank()) event.descriptionAm else event.descriptionEn

  Card(
    onClick = onCardClick,
    modifier = modifier
      .fillMaxWidth()
      .testTag("event_card_${event.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = IvyNavyContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(42.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = event.iconEmoji, fontSize = 22.sp)
            }
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = IvyTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "${event.displayDate} • ${event.time}",
              style = MaterialTheme.typography.bodySmall,
              color = IvyGoldDark,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = IvyTextSecondary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      if (event.whatToBringEn.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          color = IvyBackground,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (isAmharic) "🎒 ምን ይዘው ይምጡ፡ " else "🎒 What to bring: ",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )
            Text(
              text = if (isAmharic && event.whatToBringAm.isNotBlank()) event.whatToBringAm else event.whatToBringEn,
              fontSize = 11.sp,
              color = IvyTextSecondary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Add to calendar button
        OutlinedButton(
          onClick = { addEventToDeviceCalendar(context, event) },
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("add_to_calendar_${event.id}"),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = IvyNavy)
        ) {
          Icon(
            imageVector = Icons.Outlined.CalendarToday,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isAmharic) "ወደ ካሌንደር ጨምር" else "Add to Calendar",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        if (event.enableRSVP) {
          if (event.rsvpStatus != null) {
            Surface(
              color = if (event.rsvpStatus == "YES") IvyGreenContainer else IvyNavyContainer,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.clickable { onRsvpClick() }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = if (event.rsvpStatus == "YES") IvyGreen else IvyNavy,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (event.rsvpStatus == "YES") {
                    if (isAmharic) "እሳተፋለሁ (${event.rsvpGuestCount})" else "Attending (${event.rsvpGuestCount})"
                  } else {
                    if (isAmharic) "ምላሽ ተሰጥቷል" else "RSVP Sent"
                  },
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (event.rsvpStatus == "YES") IvyOnGreenContainer else IvyNavy
                )
              }
            }
          } else {
            Button(
              onClick = onRsvpClick,
              colors = ButtonDefaults.buttonColors(containerColor = IvyGreen),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("rsvp_button_${event.id}")
            ) {
              Text(
                text = if (isAmharic) "ምላሽ ይስጡ (RSVP)" else "RSVP",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }
      }
    }
  }
}

fun addEventToDeviceCalendar(context: Context, event: EventEntity) {
  try {
    val intent = Intent(Intent.ACTION_INSERT).apply {
      data = CalendarContract.Events.CONTENT_URI
      putExtra(CalendarContract.Events.TITLE, event.titleEn)
      putExtra(CalendarContract.Events.DESCRIPTION, event.descriptionEn)
      putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    // Graceful fallback if calendar provider isn't configured
  }
}

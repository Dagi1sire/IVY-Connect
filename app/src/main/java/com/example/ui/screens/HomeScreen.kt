package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.ui.components.AnnouncementCard
import com.example.ui.components.CategoryFilterChips
import com.example.ui.components.EventCard
import com.example.ui.components.PinnedAnnouncementCard
import com.example.ui.components.WhatsNewCard
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGoldContainer
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.ivyTextFieldColors

@Composable
fun HomeScreen(
  announcements: List<AnnouncementEntity>,
  events: List<EventEntity>,
  selectedCategory: String,
  searchQuery: String,
  isAmharic: Boolean,
  showWhatsNew: Boolean,
  onCategorySelected: (String) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onAnnouncementClick: (String) -> Unit,
  onEventClick: (String) -> Unit,
  onRsvpClick: (String) -> Unit,
  onDismissWhatsNew: () -> Unit,
  onViewAllNotices: () -> Unit,
  modifier: Modifier = Modifier
) {
  val categories = remember {
    listOf("All", "Celebrations", "Closures", "Events", "Reminders", "What to Bring", "Meals", "Activities", "Important", "General")
  }

  // Filter announcements by category and search
  val filteredAnnouncements = remember(announcements, selectedCategory, searchQuery, isAmharic) {
    announcements.filter { item ->
      val matchesCategory = selectedCategory == "All" || item.category.equals(selectedCategory, ignoreCase = true)
      val matchesSearch = searchQuery.isBlank() ||
        item.titleEn.contains(searchQuery, ignoreCase = true) ||
        item.titleAm.contains(searchQuery, ignoreCase = true) ||
        item.bodyEn.contains(searchQuery, ignoreCase = true) ||
        item.bodyAm.contains(searchQuery, ignoreCase = true) ||
        item.category.contains(searchQuery, ignoreCase = true)
      matchesCategory && matchesSearch
    }
  }

  // Section 47 Priority Ordering:
  // 1. Urgent pinned
  // 2. Important pinned
  // 3. Unread announcements
  // 4. Upcoming events
  // 5. Recent announcements
  val pinnedAnnouncement = remember(filteredAnnouncements) {
    filteredAnnouncements.firstOrNull { it.isPinned && it.priority == "URGENT" }
      ?: filteredAnnouncements.firstOrNull { it.isPinned }
  }

  val feedAnnouncements = remember(filteredAnnouncements, pinnedAnnouncement) {
    val nonPinned = filteredAnnouncements.filter { it.id != pinnedAnnouncement?.id }
    // Sort so unread come first, then by creation date
    nonPinned.sortedWith(compareByDescending<AnnouncementEntity> { !it.isRead }.thenByDescending { it.createdAt })
  }

  val upcomingEvent = remember(events) { events.firstOrNull() }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(IvyBackground)
      .testTag("home_screen_feed"),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Search Bar
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChanged,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("home_search_input"),
          placeholder = {
            Text(
              text = if (isAmharic) "ማስታወቂያዎችን ይፈልጉ..." else "Search announcements & notices...",
              fontSize = 14.sp,
              color = IvyTextMuted
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = IvyNavy,
              modifier = Modifier.size(20.dp)
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchQueryChanged("") }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "Clear",
                  tint = IvyTextSecondary,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium),
          shape = RoundedCornerShape(12.dp),
          colors = ivyTextFieldColors(containerColor = IvyBackground)
        )
      }
    }

    // "What's New" Banner (Section 46)
    if (showWhatsNew && searchQuery.isEmpty() && selectedCategory == "All") {
      item {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          WhatsNewCard(
            announcementsCount = announcements.size,
            upcomingEventsCount = events.size,
            isAmharic = isAmharic,
            onDismiss = onDismissWhatsNew,
            onViewUpdates = onViewAllNotices
          )
        }
      }
    }

    // Category Filter Chips
    item {
      CategoryFilterChips(
        categories = categories,
        selectedCategory = selectedCategory,
        isAmharic = isAmharic,
        onCategorySelected = onCategorySelected
      )
    }

    // Pinned Announcement (Section 5)
    if (pinnedAnnouncement != null && searchQuery.isEmpty() && selectedCategory == "All") {
      item {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          PinnedAnnouncementCard(
            announcement = pinnedAnnouncement,
            isAmharic = isAmharic,
            onReadMore = { onAnnouncementClick(pinnedAnnouncement.id) }
          )
        }
      }
    }

    // Upcoming Event Spotlight
    if (upcomingEvent != null && searchQuery.isEmpty() && selectedCategory == "All") {
      item {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = IvyNavy,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isAmharic) "ቀጣይ ክስተት" else "Upcoming Event",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          EventCard(
            event = upcomingEvent,
            isAmharic = isAmharic,
            onRsvpClick = { onRsvpClick(upcomingEvent.id) },
            onCardClick = { onEventClick(upcomingEvent.id) }
          )
        }
      }
    }

    // Latest Announcements Section Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.Campaign,
            contentDescription = null,
            tint = IvyNavy,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isAmharic) "የቅርብ ጊዜ ማስታወቂያዎች" else "Latest Announcements",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = IvyNavy
          )
        }

        Text(
          text = "${filteredAnnouncements.size} " + if (isAmharic) "ማስታወቂያዎች" else "notices",
          style = MaterialTheme.typography.bodySmall,
          color = IvyTextSecondary,
          fontWeight = FontWeight.Medium
        )
      }
    }

    // Feed Announcement Cards
    if (feedAnnouncements.isEmpty() && pinnedAnnouncement == null) {
      item {
        // Empty State (Section 44)
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          color = Color.White,
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(text = "🌱", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = if (isAmharic) "ሁሉም ተጠናቋል" else "You're all caught up",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (isAmharic) "በአሁኑ ጊዜ ምንም አዲስ ማስታወቂያ የለም።" else "There are no announcements matching your filter right now.",
              style = MaterialTheme.typography.bodyMedium,
              color = IvyTextSecondary,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    } else {
      items(feedAnnouncements, key = { it.id }) { item ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          AnnouncementCard(
            announcement = item,
            isAmharic = isAmharic,
            onClick = { onAnnouncementClick(item.id) }
          )
        }
      }
    }
  }
}

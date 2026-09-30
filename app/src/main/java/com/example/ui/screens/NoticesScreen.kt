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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnnouncementEntity
import com.example.ui.components.AnnouncementCard
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.ivyTextFieldColors

@Composable
fun NoticesScreen(
  announcements: List<AnnouncementEntity>,
  isAmharic: Boolean,
  onAnnouncementClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var searchQuery by remember { mutableStateOf("") }
  var selectedAudience by remember { mutableStateOf("ALL") }

  val tabs = listOf(
    if (isAmharic) "ሁሉም ማስታወቂያዎች" else "All Notices",
    if (isAmharic) "አስፈላጊና አስቸኳይ" else "Important & Urgent",
    if (isAmharic) "ማረጋገጫ የሚሹ" else "Action Required",
    if (isAmharic) "የማህደር ሰነዶች" else "Archived"
  )

  val audiences = listOf(
    "ALL" to (if (isAmharic) "ሁሉም ክፍሎች" else "All Groups"),
    "PRESCHOOL" to (if (isAmharic) "ቅድመ-ትምህርት" else "Preschool"),
    "TODDLER" to (if (isAmharic) "ታዳጊ ህፃናት" else "Toddler"),
    "INFANT" to (if (isAmharic) "ጨቅላ ህፃናት" else "Infant")
  )

  val filteredList = remember(announcements, selectedTab, searchQuery, selectedAudience) {
    announcements.filter { item ->
      val matchesTab = when (selectedTab) {
        0 -> !item.isArchived
        1 -> (item.priority == "URGENT" || item.priority == "IMPORTANT") && !item.isArchived
        2 -> item.requiresAcknowledgment && !item.isArchived
        3 -> item.isArchived
        else -> true
      }

      val matchesAudience = selectedAudience == "ALL" || item.audience.equals("ALL", ignoreCase = true) || item.audience.equals(selectedAudience, ignoreCase = true)

      val matchesSearch = searchQuery.isBlank() ||
        item.titleEn.contains(searchQuery, ignoreCase = true) ||
        item.titleAm.contains(searchQuery, ignoreCase = true) ||
        item.bodyEn.contains(searchQuery, ignoreCase = true) ||
        item.bodyAm.contains(searchQuery, ignoreCase = true) ||
        item.category.contains(searchQuery, ignoreCase = true)

      matchesTab && matchesAudience && matchesSearch
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(IvyBackground)
      .testTag("notices_screen")
  ) {
    // Top Bar Search & Filters
    Surface(
      color = Color.White,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)) {
        // Search Input
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("notices_search_input"),
          placeholder = {
            Text(
              text = if (isAmharic) "በማስታወሻዎች ውስጥ ይፈልጉ..." else "Search notices archive...",
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
              IconButton(onClick = { searchQuery = "" }) {
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
          shape = RoundedCornerShape(10.dp),
          colors = ivyTextFieldColors(containerColor = IvyBackground)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Audience filter chips
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(audiences) { (code, label) ->
            val isSelected = selectedAudience == code
            FilterChip(
              selected = isSelected,
              onClick = { selectedAudience = code },
              label = { Text(text = label, fontSize = 12.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = IvyNavy,
                selectedLabelColor = Color.White,
                containerColor = IvyBackground,
                labelColor = IvyNavy
              ),
              border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = isSelected,
                borderColor = if (isSelected) IvyNavy else IvyBorder
              ),
              shape = RoundedCornerShape(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Tabs
        ScrollableTabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color.White,
          contentColor = IvyNavy,
          edgePadding = 16.dp,
          indicator = { tabPositions ->
            SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = IvyNavy
            )
          }
        ) {
          tabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = {
                Text(
                  text = title,
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 13.sp
                )
              }
            )
          }
        }
      }
    }

    // List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (filteredList.isEmpty()) {
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
              Text(text = "📢", fontSize = 36.sp)
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = if (isAmharic) "ምንም ማስታወቂያ አልተገኘም" else "No notices found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (isAmharic) "የፍለጋ ቃሉን ወይም ማጣሪያውን ይቀይሩ።" else "Try adjusting your search query or audience filter.",
                style = MaterialTheme.typography.bodySmall,
                color = IvyTextSecondary
              )
            }
          }
        }
      } else {
        items(filteredList, key = { it.id }) { announcement ->
          AnnouncementCard(
            announcement = announcement,
            isAmharic = isAmharic,
            onClick = { onAnnouncementClick(announcement.id) }
          )
        }
      }
    }
  }
}

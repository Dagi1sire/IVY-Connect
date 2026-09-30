package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EventEntity
import com.example.ui.components.EventCard
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

@Composable
fun CalendarScreen(
  events: List<EventEntity>,
  isAmharic: Boolean,
  onEventClick: (String) -> Unit,
  onRsvpClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var viewMode by remember { mutableStateOf("LIST") } // "LIST" or "MONTH"
  var selectedDay by remember { mutableIntStateOf(10) } // Default selected day: Sept 10

  val eventDays = mapOf(
    10 to IvyGold, // Celebration
    11 to IvyUrgentRed, // Center Closure
    12 to IvyUrgentRed, // Center Closure
    13 to IvyUrgentRed, // Center Closure
    20 to IvyGreen, // Sports Day
    27 to IvyNavy // Parent Meeting
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(IvyBackground)
      .testTag("calendar_screen")
  ) {
    // Top Bar with View Mode Toggle
    Surface(
      color = Color.White,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (isAmharic) "የአይቪ የክስተቶች ካሌንደር" else "IVY Event Calendar",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )
            Text(
              text = if (isAmharic) "መስከረም 2019 / September 2026" else "September 2026",
              style = MaterialTheme.typography.bodySmall,
              color = IvyTextSecondary,
              fontWeight = FontWeight.Medium
            )
          }

          // Toggle List / Month
          Surface(
            color = IvyBackground,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
          ) {
            Row(modifier = Modifier.padding(4.dp)) {
              Surface(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { viewMode = "LIST" }
                  .testTag("calendar_list_view_btn"),
                color = if (viewMode == "LIST") IvyNavy else Color.Transparent
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.FormatListBulleted,
                    contentDescription = "List View",
                    tint = if (viewMode == "LIST") Color.White else IvyTextSecondary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = if (isAmharic) "ዝርዝር" else "List",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (viewMode == "LIST") Color.White else IvyTextSecondary
                  )
                }
              }

              Surface(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { viewMode = "MONTH" }
                  .testTag("calendar_month_view_btn"),
                color = if (viewMode == "MONTH") IvyNavy else Color.Transparent
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Month View",
                    tint = if (viewMode == "MONTH") Color.White else IvyTextSecondary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = if (isAmharic) "ወር" else "Month",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (viewMode == "MONTH") Color.White else IvyTextSecondary
                  )
                }
              }
            }
          }
        }
      }
    }

    if (viewMode == "MONTH") {
      // Month Calendar Grid View (Section 14)
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              // Month Header
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "September 2026",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = IvyNavy
                )
                Row {
                  IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
                  }
                  IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Days of week
              val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
              Row(modifier = Modifier.fillMaxWidth()) {
                daysOfWeek.forEach { day ->
                  Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = IvyTextMuted,
                    fontSize = 12.sp
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Calendar Grid Days 1 to 30 (September starts on Tuesday, index 1)
              val totalDays = 30
              val startOffset = 1 // Tuesday
              var currentDay = 1

              for (row in 0 until 5) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                ) {
                  for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    if (cellIndex < startOffset || currentDay > totalDays) {
                      Spacer(modifier = Modifier.weight(1f))
                    } else {
                      val dayNum = currentDay
                      val isSelected = selectedDay == dayNum
                      val eventColor = eventDays[dayNum]

                      Box(
                        modifier = Modifier
                          .weight(1f)
                          .height(42.dp)
                          .clip(RoundedCornerShape(8.dp))
                          .background(
                            if (isSelected) IvyNavyContainer else Color.Transparent
                          )
                          .border(
                            width = if (isSelected) 1.5.dp else 0.dp,
                            color = if (isSelected) IvyNavy else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                          )
                          .clickable { selectedDay = dayNum },
                        contentAlignment = Alignment.Center
                      ) {
                        Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center
                        ) {
                          Text(
                            text = "$dayNum",
                            fontSize = 13.sp,
                            fontWeight = if (isSelected || eventColor != null) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) IvyNavy else IvyTextPrimary
                          )

                          if (eventColor != null) {
                            Surface(
                              shape = CircleShape,
                              color = eventColor,
                              modifier = Modifier.size(5.dp)
                            ) {}
                          } else {
                            Spacer(modifier = Modifier.height(5.dp))
                          }
                        }
                      }
                      currentDay++
                    }
                  }
                }
              }

              // Legend
              Spacer(modifier = Modifier.height(16.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                LegendItem(color = IvyGold, label = if (isAmharic) "በዓላት" else "Celebration")
                LegendItem(color = IvyUrgentRed, label = if (isAmharic) "እረፍት" else "Closure")
                LegendItem(color = IvyGreen, label = if (isAmharic) "ስፖርት" else "Sports Day")
                LegendItem(color = IvyNavy, label = if (isAmharic) "ስብሰባ" else "Meeting")
              }
            }
          }
        }

        // Filtered events for selected day or upcoming
        item {
          val selectedDateEvents = events.filter {
            it.date.endsWith("-$selectedDay") || (selectedDay in 11..13 && it.id == "evt_2")
          }

          Column {
            Text(
              text = if (selectedDateEvents.isNotEmpty()) {
                if (isAmharic) "በመስከረም $selectedDay ያሉ ክስተቶች" else "Events for September $selectedDay, 2026"
              } else {
                if (isAmharic) "በዚህ ቀን የተመዘገበ ክስተት የለም" else "No scheduled events on September $selectedDay"
              },
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = IvyNavy,
              modifier = Modifier.padding(bottom = 8.dp)
            )

            if (selectedDateEvents.isEmpty()) {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
              ) {
                Text(
                  text = if (isAmharic) "ምንም ክስተት የለም። መደበኛ የትምህርትና የእንክብካቤ ሰዓት ነው።" else "No special events on this date. Standard daycare hours apply.",
                  modifier = Modifier.padding(16.dp),
                  style = MaterialTheme.typography.bodyMedium,
                  color = IvyTextSecondary
                )
              }
            } else {
              selectedDateEvents.forEach { event ->
                EventCard(
                  event = event,
                  isAmharic = isAmharic,
                  onRsvpClick = { onRsvpClick(event.id) },
                  onCardClick = { onEventClick(event.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
              }
            }
          }
        }
      }
    } else {
      // Upcoming List View (Section 14)
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          Text(
            text = if (isAmharic) "የሚመጡ ዝግጅቶችና የእረፍት ቀናት" else "UPCOMING EVENTS & CLOSURES",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = IvyNavy,
            letterSpacing = 1.sp
          )
        }

        items(events, key = { it.id }) { event ->
          EventCard(
            event = event,
            isAmharic = isAmharic,
            onRsvpClick = { onRsvpClick(event.id) },
            onCardClick = { onEventClick(event.id) }
          )
        }
      }
    }
  }
}

@Composable
private fun LegendItem(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Surface(
      shape = CircleShape,
      color = color,
      modifier = Modifier.size(8.dp)
    ) {}
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = label, fontSize = 11.sp, color = IvyTextSecondary)
  }
}

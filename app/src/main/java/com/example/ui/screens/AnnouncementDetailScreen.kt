package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.AnnouncementEntity
import com.example.ui.components.PriorityBadge
import com.example.ui.components.getCategoryLabel
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGoldContainer
import com.example.ui.theme.IvyGoldDark
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyGreenContainer
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyDark
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyOnGoldContainer
import com.example.ui.theme.IvyOnGreenContainer
import com.example.ui.theme.IvyOnUrgentContainer
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.IvyUrgentContainer
import com.example.ui.theme.IvyUrgentRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailScreen(
  announcement: AnnouncementEntity,
  isAmharicDefault: Boolean,
  onBackClick: () -> Unit,
  onAcknowledgeClick: () -> Unit,
  onRsvpSubmit: (String, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isAmharicView by remember(isAmharicDefault) { mutableStateOf(isAmharicDefault) }
  var showPhotoDialog by remember { mutableStateOf<Int?>(null) }
  var showAttachmentDownloaded by remember { mutableStateOf<String?>(null) }

  // RSVP Form Local State
  var selectedRsvpOption by remember(announcement.rsvpStatus) {
    mutableStateOf(announcement.rsvpStatus ?: "YES")
  }
  var rsvpCount by remember(announcement.rsvpGuestCount) {
    mutableIntStateOf(if (announcement.rsvpGuestCount > 0) announcement.rsvpGuestCount else 1)
  }
  var rsvpSubmitted by remember(announcement.rsvpStatus) {
    mutableStateOf(announcement.rsvpStatus != null)
  }

  val displayTitle = if (isAmharicView && announcement.titleAm.isNotBlank()) announcement.titleAm else announcement.titleEn
  val displayBody = if (isAmharicView && announcement.bodyAm.isNotBlank()) announcement.bodyAm else announcement.bodyEn

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = getCategoryLabel(announcement.category, isAmharicView),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("announcement_detail_back_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        actions = {
          // Language toggle inside detail
          Surface(
            modifier = Modifier
              .padding(end = 12.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable { isAmharicView = !isAmharicView },
            color = Color.White.copy(alpha = 0.2f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isAmharicView) "English" else "አማርኛ",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
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
        .testTag("announcement_detail_content"),
      contentPadding = PaddingValues(bottom = 60.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Cover Image (Section 9)
      if (announcement.coverDrawableRes != null) {
        item {
          Image(
            painter = painterResource(id = announcement.coverDrawableRes),
            contentDescription = "Cover Image",
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp),
            contentScale = ContentScale.Crop
          )
        }
      }

      // Title & Metadata Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            // Priority and Date Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (announcement.priority != "NORMAL") {
                PriorityBadge(priority = announcement.priority, isAmharic = isAmharicView)
              } else {
                Surface(
                  color = IvyNavyContainer,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = announcement.category.uppercase(),
                    color = IvyNavy,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Text(
                text = announcement.publishDate,
                style = MaterialTheme.typography.bodySmall,
                color = IvyTextMuted
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
              text = displayTitle,
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = IvyNavyDark,
              lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (isAmharicView) "ዒላማ ወላጆች፡ " else "Audience: ",
                fontSize = 12.sp,
                color = IvyTextMuted
              )
              Text(
                text = announcement.audience,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = IvyNavy
              )
            }
          }
        }
      }

      // Main Body Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = displayBody,
              style = MaterialTheme.typography.bodyLarge,
              color = IvyTextPrimary,
              lineHeight = 26.sp
            )

            // Event Link with "Add to Calendar"
            if (announcement.eventDate != null) {
              Spacer(modifier = Modifier.height(16.dp))
              Surface(
                color = IvyNavyContainer.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.CalendarMonth,
                      contentDescription = null,
                      tint = IvyNavy,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Text(
                        text = if (isAmharicView) "የክስተት ቀንና ሰዓት" else "Event Date & Time",
                        fontSize = 11.sp,
                        color = IvyTextSecondary,
                        fontWeight = FontWeight.SemiBold
                      )
                      Text(
                        text = "${announcement.eventDate} • ${announcement.eventTime}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = IvyNavy
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(10.dp))

                  Button(
                    onClick = {
                      val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = CalendarContract.Events.CONTENT_URI
                        putExtra(CalendarContract.Events.TITLE, announcement.titleEn)
                        putExtra(CalendarContract.Events.DESCRIPTION, announcement.bodyEn)
                      }
                      context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IvyNavy)
                  ) {
                    Text(
                      text = if (isAmharicView) "ወደ ካሌንደር አስገባ" else "Add to Device Calendar",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Celebration Photo Gallery (Section 10)
      if (announcement.category.equals("Celebrations", ignoreCase = true)) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = if (isAmharicView) "📸 የበዓሉ ፎቶዎች" else "📸 Celebration Gallery",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Spacer(modifier = Modifier.height(10.dp))

              val gallery = listOf(
                R.drawable.img_celebration,
                R.drawable.img_campus,
                R.drawable.img_app_icon
              )

              LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(gallery) { drawableRes ->
                  Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = "Gallery item",
                    modifier = Modifier
                      .size(100.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .clickable { showPhotoDialog = drawableRes },
                    contentScale = ContentScale.Crop
                  )
                }
              }
            }
          }
        }
      }

      // Attachments Section (Section 11)
      if (announcement.attachmentsJson.isNotBlank()) {
        val parts = announcement.attachmentsJson.split(":")
        val attachmentName = parts.getOrNull(0) ?: "Document.pdf"
        val attachmentSize = parts.getOrNull(1) ?: "250 KB"

        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = if (isAmharicView) "የተያያዙ ሰነዶች (Attachments)" else "Attachments & Downloads",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Spacer(modifier = Modifier.height(10.dp))

              Surface(
                color = IvyBackground,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { showAttachmentDownloaded = attachmentName }
                  .testTag("attachment_download_btn")
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    color = IvyUrgentContainer,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.size(36.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = IvyUrgentRed,
                        modifier = Modifier.size(20.dp)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = attachmentName,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold,
                      color = IvyNavy
                    )
                    Text(
                      text = "$attachmentSize • PDF Document",
                      fontSize = 11.sp,
                      color = IvyTextSecondary
                    )
                  }
                  Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = "Download",
                    tint = IvyNavy,
                    modifier = Modifier.size(22.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Acknowledgment System (Section 12 - CRITICAL)
      if (announcement.requiresAcknowledgment) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
              .testTag("acknowledgment_box"),
            colors = CardDefaults.cardColors(
              containerColor = if (announcement.isAcknowledged) IvyGreenContainer.copy(alpha = 0.5f) else IvyUrgentContainer.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(
              width = 1.5.dp,
              color = if (announcement.isAcknowledged) IvyGreen else IvyUrgentRed
            )
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = if (announcement.isAcknowledged) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                  contentDescription = null,
                  tint = if (announcement.isAcknowledged) IvyGreen else IvyUrgentRed,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = if (announcement.isAcknowledged) {
                    if (isAmharicView) "ማረጋገጫዎ ተመዝግቧል" else "Notice Confirmed & Acknowledged"
                  } else {
                    if (isAmharicView) "የወላጅ ማረጋገጫ ያስፈልጋል" else "Parent Acknowledgment Required"
                  },
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = if (announcement.isAcknowledged) IvyOnGreenContainer else IvyOnUrgentContainer
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = if (announcement.isAcknowledged) {
                  val timeStr = announcement.acknowledgedAt?.let {
                    SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault()).format(Date(it))
                  } ?: "Recently"
                  if (isAmharicView) "ይህን ማስታወቂያ ማንበብዎን በ $timeStr አረጋግጠዋል።" else "You confirmed reading this notice on $timeStr."
                } else {
                  if (isAmharicView) "ይህ ማስታወቂያ በማዕከሉ ደህንነትና አሰራር ምክንያት የወላጅ ማረጋገጫ ይጠይቃል። እባክዎ ከታች ያለውን ቁልፍ በመጫን ማንበብዎን ያረጋግጡ።" else "This notice requires official parent acknowledgment for center safety and operational compliance. Please confirm below once you have read the details."
                },
                fontSize = 13.sp,
                color = if (announcement.isAcknowledged) IvyOnGreenContainer else IvyTextPrimary,
                lineHeight = 20.sp
              )

              if (!announcement.isAcknowledged) {
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                  onClick = onAcknowledgeClick,
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("acknowledge_confirm_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = IvyGreen),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (isAmharicView) "ማስታወቂያውን አንብቤያለሁ (አረጋግጣለሁ)" else "I HAVE READ THIS NOTICE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                  )
                }
              }
            }
          }
        }
      }

      // RSVP System (Section 13)
      if (announcement.enableRSVP) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
              .testTag("rsvp_section_card"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Text(
                text = if (isAmharicView) "የተሳትፎ ምላሽ (RSVP)" else "Event RSVP Response",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = if (isAmharicView) "ልጅዎ በዚህ ዝግጅት ላይ ይገኛል?" else "Will your child attend this event?",
                style = MaterialTheme.typography.bodyMedium,
                color = IvyTextSecondary
              )

              Spacer(modifier = Modifier.height(12.dp))

              // RSVP Options: YES / NO / NOT SURE
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                RsvpOptionPill(
                  label = if (isAmharicView) "አዎ (እገኛለሁ)" else "Yes, Attending",
                  isSelected = selectedRsvpOption == "YES",
                  selectedColor = IvyGreen,
                  onClick = { selectedRsvpOption = "YES" },
                  modifier = Modifier.weight(1f)
                )

                RsvpOptionPill(
                  label = if (isAmharicView) "አልችልም" else "Can't Attend",
                  isSelected = selectedRsvpOption == "NO",
                  selectedColor = IvyUrgentRed,
                  onClick = { selectedRsvpOption = "NO" },
                  modifier = Modifier.weight(1f)
                )

                RsvpOptionPill(
                  label = if (isAmharicView) "እርግጠኛ አይደለሁም" else "Not Sure",
                  isSelected = selectedRsvpOption == "NOT_SURE",
                  selectedColor = IvyGoldDark,
                  onClick = { selectedRsvpOption = "NOT_SURE" },
                  modifier = Modifier.weight(1f)
                )
              }

              if (selectedRsvpOption == "YES") {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (isAmharicView) "የተሳታፊዎች ቁጥር፡" else "Attending Attendees / Guests:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = IvyTextPrimary
                  )
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                      onClick = { if (rsvpCount > 1) rsvpCount-- },
                      modifier = Modifier.size(32.dp)
                    ) {
                      Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text(
                      text = "$rsvpCount",
                      fontWeight = FontWeight.Bold,
                      fontSize = 16.sp,
                      color = IvyNavy,
                      modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    IconButton(
                      onClick = { if (rsvpCount < 6) rsvpCount++ },
                      modifier = Modifier.size(32.dp)
                    ) {
                      Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                onClick = {
                  onRsvpSubmit(selectedRsvpOption, rsvpCount)
                  rsvpSubmitted = true
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("submit_rsvp_button"),
                colors = ButtonDefaults.buttonColors(containerColor = IvyNavy),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text(
                  text = if (rsvpSubmitted) {
                    if (isAmharicView) "ምላሽ ተቀይሯል ✓" else "Update RSVP ✓"
                  } else {
                    if (isAmharicView) "ምላሽ ላክ" else "Submit RSVP"
                  },
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }

  // Full Screen Photo Dialog
  if (showPhotoDialog != null) {
    Dialog(onDismissRequest = { showPhotoDialog = null }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Black
      ) {
        Column(horizontalAlignment = Alignment.End) {
          IconButton(onClick = { showPhotoDialog = null }) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
          Image(
            painter = painterResource(id = showPhotoDialog!!),
            contentDescription = "Full photo",
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp),
            contentScale = ContentScale.Fit
          )
        }
      }
    }
  }

  // Download simulation dialog
  if (showAttachmentDownloaded != null) {
    AlertDialog(
      onDismissRequest = { showAttachmentDownloaded = null },
      icon = {
        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = IvyGreen, modifier = Modifier.size(36.dp))
      },
      title = {
        Text(
          text = if (isAmharicView) "ሰነድ ወርዷል" else "Attachment Downloaded",
          fontWeight = FontWeight.Bold,
          color = IvyNavy
        )
      },
      text = {
        Text(
          text = if (isAmharicView) "ሰነድ '$showAttachmentDownloaded' ወደ ስልክዎ በተሳካ ሁኔታ ወርዷል።" else "File '$showAttachmentDownloaded' has been saved to your device.",
          color = IvyTextSecondary
        )
      },
      confirmButton = {
        Button(
          onClick = { showAttachmentDownloaded = null },
          colors = ButtonDefaults.buttonColors(containerColor = IvyNavy)
        ) {
          Text("OK")
        }
      }
    )
  }
}

@Composable
private fun RsvpOptionPill(
  label: String,
  isSelected: Boolean,
  selectedColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() },
    color = if (isSelected) selectedColor else IvyBackground,
    border = androidx.compose.foundation.BorderStroke(
      width = 1.dp,
      color = if (isSelected) selectedColor else IvyBorder
    ),
    shape = RoundedCornerShape(8.dp)
  ) {
    Box(
      modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = if (isSelected) Color.White else IvyTextPrimary,
        maxLines = 1
      )
    }
  }
}

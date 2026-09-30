package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.R
import com.example.data.model.IvyResource
import com.example.data.model.ParentProfile
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGoldContainer
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyGreenContainer
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary

@Composable
fun IvyResourcesScreen(
  resources: List<IvyResource>,
  parentProfile: ParentProfile,
  isAmharic: Boolean,
  onLanguageToggle: () -> Unit,
  onNotificationPreferenceChange: (String, Boolean) -> Unit,
  onOpenAdminDashboard: (() -> Unit)? = null,
  onLogout: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedResource by remember { mutableStateOf<IvyResource?>(null) }
  var showDownloadSuccessDialog by remember { mutableStateOf<String?>(null) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(IvyBackground)
      .testTag("ivy_resources_screen"),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Hero: IVY Childcare Services Campus
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = IvyNavy)
      ) {
        Column {
          Image(
            painter = painterResource(id = R.drawable.img_campus),
            contentDescription = "IVY Childcare Center Campus",
            modifier = Modifier
              .fillMaxWidth()
              .height(160.dp),
            contentScale = ContentScale.Crop
          )

          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "IVY Childcare Services",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (isAmharic) "እንደተገናኙ ይቆዩ። በመረጃ ይበልፅጉ።" else "Stay Connected. Stay Informed.",
              style = MaterialTheme.typography.bodyMedium,
              color = IvyGold,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Quick Contact Buttons (Section 21)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              ContactButton(
                icon = Icons.Default.Call,
                label = if (isAmharic) "ይደውሉ" else "Call",
                onClick = {
                  val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+251116639000"))
                  context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
              )

              ContactButton(
                icon = Icons.Default.Email,
                label = if (isAmharic) "ኢሜይል" else "Email",
                onClick = {
                  val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:info@ivychildcare.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Parent Inquiry - IVY Childcare")
                  }
                  context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
              )

              ContactButton(
                icon = Icons.Default.LocationOn,
                label = if (isAmharic) "ካርታ" else "Map",
                onClick = {
                  val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:9.0054,38.7636?q=IVY+Childcare+Bole+Addis+Ababa"))
                  context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }

    // Operating Hours Info Card
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = IvyGreenContainer,
              shape = CircleShape,
              modifier = Modifier.size(40.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.Schedule,
                  contentDescription = null,
                  tint = IvyGreen,
                  modifier = Modifier.size(22.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (isAmharic) "የማዕከሉ የስራ ሰዓት" else "Operating Hours",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Text(
                text = if (isAmharic) "ከሰኞ እስከ አርብ፡ ከጠዋቱ 1:30 - ምሽቱ 11:30" else "Monday – Friday: 7:30 AM – 5:30 PM",
                style = MaterialTheme.typography.bodySmall,
                color = IvyTextSecondary
              )
              Text(
                text = if (isAmharic) "ቅዳሜና እሁድ፡ ዝግ ነው" else "Saturday – Sunday: Closed",
                style = MaterialTheme.typography.bodySmall,
                color = IvyTextMuted
              )
            }
          }
        }
      }
    }

    // Parent Profile & Children (Section 23 & 24)
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (isAmharic) "የወላጅ መረጃ" else "My Family Profile",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                color = IvyNavy,
                shape = CircleShape,
                modifier = Modifier.size(44.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(text = "👩", fontSize = 22.sp)
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = parentProfile.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = IvyTextPrimary
                )
                Text(
                  text = "${parentProfile.phone} • ${parentProfile.email}",
                  fontSize = 12.sp,
                  color = IvyTextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = IvyBorder)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = if (isAmharic) "የተመዘገቡ ልጆች" else "Enrolled Children",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = IvyTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            parentProfile.children.forEach { child ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = child.avatarEmoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "${child.name} — ${child.department}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IvyNavy
                  )
                  Text(
                    text = child.className,
                    fontSize = 11.sp,
                    color = IvyTextSecondary
                  )
                }
              }
            }
          }
        }
      }
    }

    // Parent Resources Section (Section 22)
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
          text = if (isAmharic) "የወላጆች መመሪያዎችና ሰነዶች" else "Parent Policies & Resources",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = IvyNavy
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = if (isAmharic) "አስፈላጊ መመሪያዎችን፣ የጤና ፖሊሲዎችን እና የትምህርት ሰሌዳዎችን ይመልከቱ" else "Official guidelines, health policies, handbooks, and schedules",
          style = MaterialTheme.typography.bodySmall,
          color = IvyTextSecondary
        )
      }
    }

    items(resources, key = { it.id }) { resource ->
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
          onClick = { selectedResource = resource },
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = IvyNavyContainer,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.size(38.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(text = resource.icon, fontSize = 20.sp)
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (isAmharic && resource.titleAm.isNotBlank()) resource.titleAm else resource.titleEn,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = IvyTextPrimary
              )
              Text(
                text = if (isAmharic && resource.summaryAm.isNotBlank()) resource.summaryAm else resource.summaryEn,
                fontSize = 12.sp,
                color = IvyTextSecondary,
                maxLines = 1
              )
            }
            Icon(
              imageVector = Icons.Outlined.ChevronRight,
              contentDescription = null,
              tint = IvyTextMuted,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // App Preferences (Language & Notifications - Sections 34 & 48)
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (isAmharic) "የመተግበሪያ ምርጫዎች" else "App Settings & Preferences",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Language Switcher Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = if (isAmharic) "የመተግበሪያ ቋንቋ" else "App Language",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = IvyTextPrimary
                )
                Text(
                  text = if (isAmharic) "አማርኛ (Amharic)" else "English",
                  fontSize = 12.sp,
                  color = IvyTextSecondary
                )
              }

              OutlinedButton(
                onClick = onLanguageToggle,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = IvyNavy)
              ) {
                Text(text = if (isAmharic) "ወደ English ቀይር" else "Switch to አማርኛ", fontSize = 12.sp)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = IvyBorder)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = if (isAmharic) "የማሳወቂያ ምርጫዎች" else "Notification Channels",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = IvyTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            NotificationPrefRow(
              title = if (isAmharic) "አጠቃላይ ማስታወቂያዎች" else "Announcements",
              enabled = parentProfile.notifyAnnouncements,
              onToggle = { onNotificationPreferenceChange("announcements", it) }
            )

            NotificationPrefRow(
              title = if (isAmharic) "ክስተቶችና ዝግጅቶች" else "Events",
              enabled = parentProfile.notifyEvents,
              onToggle = { onNotificationPreferenceChange("events", it) }
            )

            NotificationPrefRow(
              title = if (isAmharic) "ማሳሰቢያዎች" else "Reminders",
              enabled = parentProfile.notifyReminders,
              onToggle = { onNotificationPreferenceChange("reminders", it) }
            )

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (isAmharic) "🚨 አስቸኳይ ማስታወቂያዎች" else "🚨 Urgent Notices",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Text(
                text = if (isAmharic) "ሁልጊዜ በርቷል" else "ALWAYS ON",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = IvyGreen
              )
            }
          }
        }
      }
    }

    // Staff / Admin Dashboard Portal (Only visible if admin)
    if (parentProfile.isAdmin && onOpenAdminDashboard != null) {
      item {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          Card(
            onClick = onOpenAdminDashboard,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("open_admin_dashboard_card"),
            colors = CardDefaults.cardColors(containerColor = IvyNavy),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              modifier = Modifier.padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = IvyGold,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = IvyNavy,
                    modifier = Modifier.size(24.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = if (isAmharic) "የአይቪ አስተዳዳሪ ዳሽቦርድ" else "IVY Communication Admin",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Text(
                  text = if (isAmharic) "ማስታወቂያዎችን ለመፍጠር እና ስታቲስቲክስ ለማየት" else "Create notices, track read rates & manage calendar",
                  fontSize = 11.sp,
                  color = Color.White.copy(alpha = 0.8f)
                )
              }
              Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = IvyGold,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }

    // Sign Out Button for authenticated parents
    if (onLogout != null) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("parent_logout_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = IvyNavy
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, IvyNavy.copy(alpha = 0.3f))
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Logout,
              contentDescription = "Sign Out",
              tint = IvyNavy,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isAmharic) "ከመለያዎ ይውጡ" else "Sign Out",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = IvyNavy
            )
          }
        }
      }
    }
  }

  // Detail Dialog for Resources
  if (selectedResource != null) {
    val res = selectedResource!!
    AlertDialog(
      onDismissRequest = { selectedResource = null },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = res.icon, fontSize = 24.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isAmharic && res.titleAm.isNotBlank()) res.titleAm else res.titleEn,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = IvyNavy
          )
        }
      },
      text = {
        Column {
          Text(
            text = if (isAmharic && res.contentAm.isNotBlank()) res.contentAm else res.contentEn,
            style = MaterialTheme.typography.bodyMedium,
            color = IvyTextPrimary,
            lineHeight = 22.sp
          )
          if (res.documentName != null) {
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
              color = IvyBackground,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  showDownloadSuccessDialog = res.documentName
                }
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.FileDownload,
                  contentDescription = "Download",
                  tint = IvyNavy,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = res.documentName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = IvyNavy
                  )
                  Text(
                    text = if (isAmharic) "ሰነዱን ያውርዱ" else "Download Document",
                    fontSize = 11.sp,
                    color = IvyGreen,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { selectedResource = null }) {
          Text(text = if (isAmharic) "ዝጋ" else "Close", color = IvyNavy, fontWeight = FontWeight.Bold)
        }
      }
    )
  }

  // Simulated Download confirmation dialog
  if (showDownloadSuccessDialog != null) {
    val filename = showDownloadSuccessDialog!!
    AlertDialog(
      onDismissRequest = { showDownloadSuccessDialog = null },
      icon = {
        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = IvyGreen, modifier = Modifier.size(36.dp))
      },
      title = {
        Text(text = if (isAmharic) "ሰነዱ ወርዷል" else "Download Complete", fontWeight = FontWeight.Bold, color = IvyNavy)
      },
      text = {
        Text(
          text = if (isAmharic) "ሰነድ '$filename' ወደ መሳሪያዎ በተሳካ ሁኔታ ወርዷል።" else "The document '$filename' is saved to your downloads folder.",
          color = IvyTextSecondary
        )
      },
      confirmButton = {
        Button(
          onClick = { showDownloadSuccessDialog = null },
          colors = ButtonDefaults.buttonColors(containerColor = IvyNavy)
        ) {
          Text("OK")
        }
      }
    )
  }
}

@Composable
private fun ContactButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() },
    color = Color.White.copy(alpha = 0.15f),
    shape = RoundedCornerShape(8.dp)
  ) {
    Row(
      modifier = Modifier.padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = IvyGold,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = label,
        color = Color.White,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
private fun NotificationPrefRow(
  title: String,
  enabled: Boolean,
  onToggle: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = title, fontSize = 13.sp, color = IvyTextPrimary)
    Switch(
      checked = enabled,
      onCheckedChange = onToggle,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = IvyGreen,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = IvyBorder
      )
    )
  }
}

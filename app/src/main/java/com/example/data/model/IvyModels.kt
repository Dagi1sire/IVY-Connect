package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.R

enum class Priority {
  NORMAL,
  IMPORTANT,
  URGENT
}

enum class Audience(val labelEn: String, val labelAm: String) {
  ALL("All Parents", "ለሁሉም ወላጆች"),
  PRESCHOOL("Preschool", "ቅድመ-ትምህርት ቤት"),
  TODDLER("Toddler", "ታዳጊ ህፃናት"),
  INFANT("Infant", "ጨቅላ ህፃናት"),
  DEVELOPMENTAL("Developmental Daycare", "የእድገት ማቆያ"),
  SPECIAL_NEEDS("Special Needs Department", "የልዩ ፍላጎት ክፍል")
}

data class IvyAttachment(
  val name: String,
  val size: String,
  val type: String = "PDF"
)

data class ChildInfo(
  val name: String,
  val department: String,
  val className: String,
  val avatarEmoji: String = "👧"
)

data class ParentProfile(
  val username: String = "",
  val name: String = "",
  val phone: String = "",
  val email: String = "",
  val children: List<ChildInfo> = emptyList(),
  val language: String = "en", // "en" or "am"
  val notifyAnnouncements: Boolean = true,
  val notifyEvents: Boolean = true,
  val notifyReminders: Boolean = true,
  val notifyCelebrations: Boolean = true,
  val notifyUrgent: Boolean = true,
  val isAdmin: Boolean = false
)

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
  @PrimaryKey val username: String,
  val password: String,
  val role: String, // "ADMIN" or "PARENT"
  val fullName: String,
  val phone: String = "",
  val email: String = "",
  val childrenNames: String = "",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "announcements")
data class AnnouncementEntity(
  @PrimaryKey val id: String,
  val titleEn: String,
  val titleAm: String,
  val bodyEn: String,
  val bodyAm: String,
  val category: String,
  val priority: String, // NORMAL, IMPORTANT, URGENT
  val coverDrawableRes: Int? = null,
  val audience: String = "ALL",
  val isPinned: Boolean = false,
  val requiresAcknowledgment: Boolean = false,
  val enableRSVP: Boolean = false,
  val eventDate: String? = null,
  val eventTime: String? = null,
  val isRead: Boolean = false,
  val isAcknowledged: Boolean = false,
  val acknowledgedAt: Long? = null,
  val rsvpStatus: String? = null, // "YES", "NO", "NOT_SURE"
  val rsvpGuestCount: Int = 1,
  val publishDate: String,
  val createdAt: Long = System.currentTimeMillis(),
  val isArchived: Boolean = false,
  val attachmentsJson: String = "",
  val galleryDrawablesJson: String = ""
)

@Entity(tableName = "events")
data class EventEntity(
  @PrimaryKey val id: String,
  val titleEn: String,
  val titleAm: String,
  val descriptionEn: String,
  val descriptionAm: String,
  val date: String, // e.g. "2026-09-20"
  val displayDate: String, // e.g. "Friday, September 20, 2026"
  val time: String, // e.g. "8:00 AM – 1:00 PM"
  val location: String = "IVY Childcare Services, Main Campus",
  val eventType: String, // "Celebration", "Center Closure", "Sports Day", "Parent Meeting", "Special Activity"
  val iconEmoji: String = "🎉",
  val whatToBringEn: String = "", // comma separated
  val whatToBringAm: String = "",
  val enableRSVP: Boolean = true,
  val rsvpDeadline: String? = null,
  val rsvpStatus: String? = null, // "YES", "NO", "NOT_SURE"
  val rsvpGuestCount: Int = 1,
  val coverDrawableRes: Int? = null
)

@Entity(tableName = "notifications")
data class IvyNotificationEntity(
  @PrimaryKey val id: String,
  val type: String, // "URGENT", "ANNOUNCEMENT", "EVENT_REMINDER", "RSVP_REMINDER", "ACK_REMINDER"
  val titleEn: String,
  val titleAm: String,
  val messageEn: String,
  val messageAm: String,
  val timeDisplay: String,
  val groupTime: String, // "TODAY", "YESTERDAY", "EARLIER"
  val isRead: Boolean = false,
  val targetAnnouncementId: String? = null,
  val targetEventId: String? = null,
  val timestamp: Long = System.currentTimeMillis()
)

data class IvyResource(
  val id: String,
  val titleEn: String,
  val titleAm: String,
  val icon: String,
  val category: String,
  val summaryEn: String,
  val summaryAm: String,
  val contentEn: String,
  val contentAm: String,
  val documentName: String? = null
)

package com.example.data.local

import com.example.R
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.data.model.IvyNotificationEntity
import com.example.data.model.IvyResource
import com.example.data.model.UserAccountEntity

object IvySampleData {

  fun getInitialUserAccounts(): List<UserAccountEntity> = listOf(
    UserAccountEntity(
      username = "Dagi97",
      password = "Dagi9714",
      role = "ADMIN",
      fullName = "Administrator",
      phone = "",
      email = "Dagi.dt3@gmail.com",
      childrenNames = ""
    )
  )

  fun getInitialAnnouncements(): List<AnnouncementEntity> = emptyList()

  fun getInitialEvents(): List<EventEntity> = emptyList()

  fun getInitialNotifications(): List<IvyNotificationEntity> = emptyList()

  fun getIvyResources(): List<IvyResource> = listOf(
    IvyResource(
      id = "res_1",
      titleEn = "Parent Handbook & Guidelines",
      titleAm = "የወላጆች መመሪያ ደንብ",
      icon = "📘",
      category = "Policies",
      summaryEn = "Complete guide to IVY Childcare rules, drop-off/pick-up, and ethics.",
      summaryAm = "የአይቪ ህፃናት ማቆያ ዝርዝር ደንቦች እና መመሪያዎች።",
      contentEn = "Welcome to IVY Childcare Services. Our mission is to nurture curiosity, empathy, and early confidence in a safe, inspiring environment.\n\n1. Hours of Operation: 7:30 AM to 5:30 PM, Monday through Friday.\n2. Drop-off & Pick-up: All authorized individuals must present identification or registered parent badge.\n3. Communication: All official notices and schedules are published through IVY Parent Connect.",
      contentAm = "ወደ አይቪ የህፃናት ማቆያ እንኳን በደህና መጡ። ተልእኮአችን ህፃናት በፍቅርና በደህንነት እንዲያድጉ ማድረግ ነው።\n\n1. የስራ ሰዓት፡ ከሰኞ እስከ አርብ ከጠዋቱ 1:30 እስከ ምሽቱ 11:30።\n2. ልጆችን መረከብ፡ የተመዘገቡ ወላጆች ወይም ህጋዊ ወኪሎች ብቻ ልጆችን መረከብ ይችላሉ።",
      documentName = "IVY_Parent_Handbook_2026.pdf"
    ),
    IvyResource(
      id = "res_2",
      titleEn = "Health & Sick-Day Policy",
      titleAm = "የጤና እና የህመም ቀናት ፖሊሲ",
      icon = "🩺",
      category = "Policies",
      summaryEn = "Guidelines regarding illnesses, fevers, and return-to-care protocols.",
      summaryAm = "ህፃናት ሲታመሙ መከተል የሚገባቸው የጤና ጥንቃቄዎች።",
      contentEn = "To protect the health of all children and educators:\n\n• Fevers: Children must be fever-free for 24 hours without medication before returning.\n• Contagious Symptoms: Persistent coughing, rash, or upset stomach require pediatrician clearance.\n• Medication: On-duty registered nurse administers doctor-prescribed medication with signed parent consent forms.",
      contentAm = "የህፃናትን ጤና ለመጠበቅ ልጅዎ ትኩሳት ወይም ተላላፊ ምልክቶች ሲኖሩት በቤት እንዲያርፍ እንጠይቃለን።",
      documentName = "Health_and_Sick_Policy.pdf"
    ),
    IvyResource(
      id = "res_3",
      titleEn = "What to Bring to IVY Checklist",
      titleAm = "ለአይቪ የሚያስፈልጉ ዕቃዎች ዝርዝር",
      icon = "🎒",
      category = "Supplies",
      summaryEn = "Daily and seasonal checklist of required clothing and personal care items.",
      summaryAm = "በየቀኑና በየወቅቱ ለልጅዎ የሚያስፈልጉ ነገሮች ዝርዝር።",
      contentEn = "Please prepare:\n\n1. Two full changes of weather-appropriate clothes labeled with permanent marker.\n2. Indoor non-slip slippers or warm socks.\n3. Reusable leak-proof water flask.\n4. Diapers & wipes for Infant and Toddler groups.\n5. Sun hat or seasonal rain poncho.",
      contentAm = "የሚዘጋጁ ነገሮች፡\n\n1. ስም የተጻፈባቸው 2 ጥንድ መለወጫ ልብሶች\n2. ምቹ የቤት ውስጥ ጫማ\n3. የውሃ መጠጫ\n4. ዳይፐር እና እርጥብ ሶፍት",
      documentName = "What_To_Bring_Checklist.pdf"
    ),
    IvyResource(
      id = "res_4",
      titleEn = "Daily Childcare & Preschool Routine",
      titleAm = "የእለት ተእለት የጊዜ ሰሌዳ",
      icon = "🕐",
      category = "Curriculum",
      summaryEn = "Hour-by-hour schedule of learning, circle time, meals, and nap periods.",
      summaryAm = "የትምህርት፣ የጨዋታ፣ የምግብ እና የእረፍት የዕለት ሰዓቶች።",
      contentEn = "07:30 – 08:30 | Gentle Arrival & Free Montessori Play\n08:30 – 09:15 | Morning Circle, Song & Weather Greeting\n09:15 – 09:45 | Morning Organic Snack & Fresh Fruit\n09:45 – 11:15 | Core Exploration: Sensory, Language & Creative Arts\n11:15 – 12:00 | Outdoor Nature Playground & Motor Games\n12:00 – 12:45 | Wholesome Nutritious Lunch\n12:45 – 02:30 | Restful Nap / Quiet Story Listening\n02:30 – 03:15 | Afternoon Snack\n03:15 – 04:30 | Music, Movement & STEM Discovery\n04:30 – 05:30 | Free Play & Parent Pick-up",
      contentAm = "ከጠዋቱ 1:30 ጀምሮ እስከ ምሽቱ 11:30 ድረስ ያለውን የተሟላ የትምህርት፣ የምግብና የጨዋታ ሰዓት ይመልከቱ።",
      documentName = "Daily_Routine_Schedule.pdf"
    ),
    IvyResource(
      id = "res_5",
      titleEn = "Tuition & Fee Information",
      titleAm = "የክፍያና ፋይናንስ መረጃዎች",
      icon = "💳",
      category = "Finance",
      summaryEn = "Billing cycles, payment methods, bank accounts, and holiday adjustments.",
      summaryAm = "የክፍያ ጊዜ፣ የባንክ ሂሳቦች እና ተዛማጅ መመሪያዎች።",
      contentEn = "Tuition is payable by the 5th of each calendar month. Bank transfer and mobile payment receipts should be forwarded with student reference ID.\n\nBank Accounts:\n• Commercial Bank of Ethiopia: 1000123456789 (IVY Childcare)\n• Awash Bank: 01410123456700",
      contentAm = "ወርሃዊ ክፍያ በየወሩ እስከ 5ኛው ቀን መከፈል ይኖርበታል።\nየንግድ ባንክ ሂሳብ ቁጥር፡ 1000123456789",
      documentName = "Tuition_Policy_2026.pdf"
    ),
    IvyResource(
      id = "res_6",
      titleEn = "Emergency Contacts & Campus Safety",
      titleAm = "የአስቸኳይ ጊዜ ስልክ ቁጥሮች",
      icon = "🚑",
      category = "Safety",
      summaryEn = "Direct campus lines, pediatric on-call doctor, and security desk.",
      summaryAm = "የማዕከሉ ዋና አስተዳደር፣ የህፃናት ሀኪም እና የደህንነት ስልኮች።",
      contentEn = "Campus Reception: +251 11 663 9000\nDirector's Desk: +251 91 122 3344\nCampus Nurse: +251 91 155 6677\nAddis Ababa Emergency: 911 / 907\nCampus Location: Bole Sub-city, Near Atlas / Olympia, Addis Ababa, Ethiopia",
      contentAm = "ዋና ሪሴፕሽን፡ +251 11 663 9000\nዳይሬክተር፡ +251 91 122 3344\nነርስ፡ +251 91 155 6677",
      documentName = null
    )
  )
}

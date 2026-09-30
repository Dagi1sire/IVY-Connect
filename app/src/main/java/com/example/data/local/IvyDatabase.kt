package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AnnouncementEntity
import com.example.data.model.EventEntity
import com.example.data.model.IvyNotificationEntity
import com.example.data.model.UserAccountEntity

@Database(
  entities = [
    AnnouncementEntity::class,
    EventEntity::class,
    IvyNotificationEntity::class,
    UserAccountEntity::class
  ],
  version = 3,
  exportSchema = false
)
abstract class IvyDatabase : RoomDatabase() {

  abstract fun ivyDao(): IvyDao

  companion object {
    @Volatile
    private var INSTANCE: IvyDatabase? = null

    fun getDatabase(context: Context): IvyDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          IvyDatabase::class.java,
          "ivy_parent_connect.db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}

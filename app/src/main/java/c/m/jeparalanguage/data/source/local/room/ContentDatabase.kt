package c.m.jeparalanguage.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import c.m.jeparalanguage.data.source.local.entity.ContentEntity

@Database(
    entities = [ContentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao
}
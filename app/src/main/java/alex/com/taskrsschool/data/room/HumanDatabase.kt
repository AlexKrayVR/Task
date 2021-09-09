package alex.com.taskrsschool.data.room

import alex.com.taskrsschool.data.DATABASE_VERSION
import alex.com.taskrsschool.domain.model.Human
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Human::class], version = DATABASE_VERSION)
abstract class HumanDatabase : RoomDatabase() {
    abstract fun humanDao(): HumanDao
}
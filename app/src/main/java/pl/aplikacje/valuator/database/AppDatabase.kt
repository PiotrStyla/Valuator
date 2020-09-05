package pl.aplikacje.valuator.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemsInDatabase::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
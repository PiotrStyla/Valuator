package pl.aplikacje.valuator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CarPhotoInDatabase::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): CarPhotoDatabaseDao //will be used in repository

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "car_photo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
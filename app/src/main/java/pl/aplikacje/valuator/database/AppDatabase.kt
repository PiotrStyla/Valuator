package pl.aplikacje.valuator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [CarPhotoInDatabase::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): CarPhotoDatabaseDao //will be used in repository

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "car_photo_database"
                ).addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.itemDao())
                }
            }
        }
        suspend fun populateDatabase(wordDao: CarPhotoDatabaseDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = CarPhotoInDatabase( 1,"aaa","Toyota", "Avensis","2012")

            wordDao.insert(word)


//            word = CarPhotoInDatabase()
//            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }
}
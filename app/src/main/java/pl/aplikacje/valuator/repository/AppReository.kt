package pl.aplikacje.valuator.repository

import androidx.lifecycle.LiveData
import pl.aplikacje.valuator.database.CarPhotoInDatabase
import pl.aplikacje.valuator.database.CarPhotoDatabaseDao

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AppReository(private val itemDao: CarPhotoDatabaseDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allItems: LiveData<List<CarPhotoInDatabase>> = itemDao.getAll()
    val latestItem: LiveData<List<CarPhotoInDatabase>> = itemDao.getLatest()
    val recordId: Long = 0

    fun delete (car: CarPhotoInDatabase){
        itemDao.delete(car)
    }



    suspend fun insert(car: CarPhotoInDatabase) {
        val recordId = itemDao.insert(car)
    }
}
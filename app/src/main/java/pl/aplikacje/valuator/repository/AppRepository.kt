package pl.aplikacje.valuator.repository

import pl.aplikacje.valuator.database.CarPhotoDatabaseDao
import pl.aplikacje.valuator.database.CarPhotoInDatabase

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AppRepository(private val itemDao: CarPhotoDatabaseDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.

    fun delete(car: CarPhotoInDatabase) {
        itemDao.delete(car)
    }

    suspend fun insert(car: CarPhotoInDatabase): Long {
        return itemDao.insert(car)
    }

    suspend fun getCarById(recordId: Int): CarPhotoInDatabase {
        return itemDao.getById(recordId)
    }

    suspend fun getAllCarRecords(): List<CarPhotoInDatabase> {
        return itemDao.getAll()
    }

}
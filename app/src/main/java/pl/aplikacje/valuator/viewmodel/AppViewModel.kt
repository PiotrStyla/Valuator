package pl.aplikacje.valuator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.aplikacje.valuator.database.AppDatabase
import pl.aplikacje.valuator.database.CarPhotoInDatabase
import pl.aplikacje.valuator.repository.AppRepository

class AppViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private var _allCarData = MutableLiveData<List<CarPhotoInDatabase>>()
    val allCarData: LiveData<List<CarPhotoInDatabase>> = _allCarData

    private val _latestCarData = MutableLiveData<CarPhotoInDatabase>()
    val latestCarData: LiveData<CarPhotoInDatabase> = _latestCarData

    init {
        val itemsDao = AppDatabase.getDatabase(application, viewModelScope).itemDao()
        repository = AppRepository(itemsDao)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(car: CarPhotoInDatabase): Long {
        var insertedId = -1L

        runBlocking {
            val insertJob = async { repository.insert(car) }
            insertedId = insertJob.await()
        }

        return insertedId
    }

    fun delete(car: CarPhotoInDatabase) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(car)
    }

    fun getAllCarRecords() {
        viewModelScope.launch {
            val cars = repository.getAllCarRecords()
            _allCarData.postValue(cars)
        }
    }

    fun getCarDataById(recordId: Int) {
        viewModelScope.launch {
            val car = repository.getCarById(recordId)
            _latestCarData.postValue(car)
        }
    }

}
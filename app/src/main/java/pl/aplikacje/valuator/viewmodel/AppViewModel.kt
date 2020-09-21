package pl.aplikacje.valuator.viewmodel

import android.app.Application
import android.content.ClipData
import android.view.ViewConfiguration.get
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.aplikacje.valuator.database.AppDatabase
import pl.aplikacje.valuator.database.AppDatabase.Companion.getDatabase
import pl.aplikacje.valuator.database.CarPhotoInDatabase
import pl.aplikacje.valuator.model.Mmg
import pl.aplikacje.valuator.repository.AppReository

class AppViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: AppReository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPositions: LiveData<List<CarPhotoInDatabase>>
    val latestPosition: LiveData<List<CarPhotoInDatabase>>
    val id: Long
        get() = repository.recordId


    init {
        val itemsDao = AppDatabase.getDatabase(application, viewModelScope).itemDao()
        repository = AppReository(itemsDao)
        allPositions = repository.allItems
        latestPosition = repository.latestItem
        val id = repository.recordId
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(car: CarPhotoInDatabase) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(car)
    }

    fun delete(car: CarPhotoInDatabase) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(car)
    }

}
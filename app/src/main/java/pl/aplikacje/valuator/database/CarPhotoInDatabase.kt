package pl.aplikacje.valuator.database

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.aplikacje.valuator.viewmodel.AppViewModel


@Entity(tableName = "cars_photo_history")
data class CarPhotoInDatabase(
    var savedUri: String?,
    var make_name: String,
    var model_name: String,
    var years: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface CarPhotoDatabaseDao {

    @Query("SELECT * FROM cars_photo_history")
    fun getAll(): LiveData<List<CarPhotoInDatabase>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(car: CarPhotoInDatabase): Long

    @Delete
    fun delete(car: CarPhotoInDatabase)


    @Query("DELETE FROM cars_photo_history")
    suspend fun deleteAll()

    @Query("SELECT * FROM cars_photo_history ORDER BY UID DESC LIMIT 1")
    fun getLatest(): LiveData<List<CarPhotoInDatabase>>

//    @Query("SELECT * FROM cars_photo_history where UID = <recordId> ")
//    fun getLatest(): LiveData<List<CarPhotoInDatabase>>

}
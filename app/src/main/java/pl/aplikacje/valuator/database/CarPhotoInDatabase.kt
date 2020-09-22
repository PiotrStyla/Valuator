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
    suspend fun getAll(): List<CarPhotoInDatabase>  //room_trial_2 suspend added

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(car: CarPhotoInDatabase): Long

    @Delete
    fun delete(car: CarPhotoInDatabase)


    @Query("DELETE FROM cars_photo_history")
    suspend fun deleteAll()

    //room_trial_2 WHERE should have been capitalized letters; :id taken from fun getById where
    //it came from repository and model fr
    @Query("SELECT * FROM cars_photo_history WHERE uid=:id ")
    suspend fun getById(id: Int): CarPhotoInDatabase



}
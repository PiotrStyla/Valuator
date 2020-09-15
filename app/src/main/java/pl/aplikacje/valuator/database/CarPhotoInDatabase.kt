package pl.aplikacje.valuator.database

import androidx.lifecycle.LiveData
import androidx.room.*


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
    suspend fun insert(car: CarPhotoInDatabase)

    @Delete
    fun delete(car: CarPhotoInDatabase)


    @Query("DELETE FROM cars_photo_history")
    suspend fun deleteAll()

}
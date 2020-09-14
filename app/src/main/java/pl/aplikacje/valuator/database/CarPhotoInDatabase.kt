package pl.aplikacje.valuator.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Entity(tableName = "cars_photo_history")
data class CarPhotoInDatabase(

    @PrimaryKey(autoGenerate = true) val uid: Int,

    @ColumnInfo val savedUri: String,

    @ColumnInfo val make_name: String,

    @ColumnInfo val model_name: String,

    @ColumnInfo val years: String

)

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
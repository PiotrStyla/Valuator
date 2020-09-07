package pl.aplikacje.valuator.database

import android.media.Image
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.android.synthetic.main.item_view.view.*
import pl.aplikacje.valuator.MainFragment
import java.io.File




@Entity(tableName = "cars_photo_history")
class CarPhotoInDatabase(

    @PrimaryKey(autoGenerate = true) val uid: Int,

    @ColumnInfo val savedUri: String,

    @ColumnInfo val make_name: String,

    @ColumnInfo val model_name: String,

    @ColumnInfo val years: String

)

@Dao
interface ItemDao {

    @Query("SELECT * FROM cars_photo_history")
    fun getAll(): LiveData<List<CarPhotoInDatabase>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: CarPhotoInDatabase)

    @Delete
    fun delete(item: CarPhotoInDatabase)


    @Query("DELETE FROM cars_photo_history")
    suspend fun deleteAll()

}
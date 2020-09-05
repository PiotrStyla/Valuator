package pl.aplikacje.valuator.database

import android.net.Uri
import androidx.room.*


@Entity
data class ItemsInDatabase (

    @PrimaryKey(autoGenerate = true) val uid: Int,

    @ColumnInfo val make_name: String,

    @ColumnInfo val model_name: String,

    @ColumnInfo val years: String

)

@Dao
interface ItemDao {

    @Query("SELECT * FROM ItemsInDatabase")
    fun getAll(): List<ItemsInDatabase>

    @Insert
    fun insertAll(vararg newCarAdded: ItemsInDatabase)

    @Delete
    fun delete(carDeleted: ItemsInDatabase)


}
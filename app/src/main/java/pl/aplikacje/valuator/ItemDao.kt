package pl.aplikacje.valuator

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.aplikacje.valuator.database.ItemsInDatabase


@Dao
interface ItemDao {

    @Query("SELECT * FROM ItemsInDatabase")
    fun getAll(): List<ItemsInDatabase>

    @Insert
    fun insertAll(vararg newCarAdded: ItemsInDatabase)

    @Delete
    fun delete(carDeleted: ItemsInDatabase)


}
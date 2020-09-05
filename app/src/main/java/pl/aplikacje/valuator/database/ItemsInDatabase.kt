package pl.aplikacje.valuator.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ItemsInDatabase (

    @PrimaryKey(autoGenerate = true) val uid: Int,

    @ColumnInfo val photoFile :   Uri,

    @ColumnInfo val make_name: String,

    @ColumnInfo val model_name: String,

    @ColumnInfo val years: String

)
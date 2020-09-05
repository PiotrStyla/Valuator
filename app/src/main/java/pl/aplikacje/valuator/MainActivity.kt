package pl.aplikacje.valuator

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import pl.aplikacje.valuator.database.AppDatabase
import pl.aplikacje.valuator.database.ItemsInDatabase
import pl.aplikacje.valuator.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        doAsync {
//
//            val db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java, "database-name"
//            ).build()
//
//            db.itemDao().insertAll(
//                ItemsInDatabase
//                    (uid = 0, "make_name", "model_name", "years")
//            )
//
//            val carsHistory = db.itemDao().getAll()
//
//
//
//            uiThread {
//
//
//            }
//        }
    }
}

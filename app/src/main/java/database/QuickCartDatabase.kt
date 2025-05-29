package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import models.Item
import models.ShopHistory

@Database(
    entities = [Item::class, ShopHistory::class],
    //Version holds the increments for the database modifications
    version = 2,
    exportSchema = false
)
abstract class QuickCartDatabase : RoomDatabase() {
    //Access points for daos
    abstract fun itemDao(): ItemDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: QuickCartDatabase? = null

        //Return instance of the database
        fun getDatabase(context: Context): QuickCartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuickCartDatabase::class.java,
                    "quick_cart_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.quickcart

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import android.content.Context

@Database(
    entities = [Item::class, ShopHistory::class],
    //Version holds the increments for the database modifications
    version = 2,
    exportSchema = false
)
abstract class QuickCartDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: QuickCartDatabase? = null

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
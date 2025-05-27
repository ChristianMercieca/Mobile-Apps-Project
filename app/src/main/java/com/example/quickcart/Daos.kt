package com.example.quickcart

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Set daos to be used, with their respective functions and queries

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY id DESC")
    fun getAllItems(): Flow<List<Item>>

    //Creates new item, but if item already exists, replace it instead
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("DELETE FROM items")
    suspend fun clearAllItems()

}

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertShop(shop: ShopHistory): Long

    @Query("SELECT * FROM shop_history ORDER BY date DESC")
    fun getAllShops(): Flow<List<ShopHistory>>

    @Query("UPDATE shop_history SET receiptUri = :receiptUri WHERE id = :shopId")
    suspend fun attachReceipt(shopId: Int, receiptUri: String)

    @Query("DELETE FROM shop_history")
    suspend fun clearAllHistory()
}
package com.example.quickcart

import androidx.room.Entity
import androidx.room.PrimaryKey

//Shop History table
@Entity(tableName = "shop_history")
data class ShopHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val items: String,
    val receiptUri: String? = null
)
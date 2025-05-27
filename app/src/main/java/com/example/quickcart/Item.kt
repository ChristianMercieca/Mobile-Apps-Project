package com.example.quickcart

import androidx.room.Entity
import androidx.room.PrimaryKey

//Item table
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int,
    val category: String,
    val isChecked: Boolean = false
)

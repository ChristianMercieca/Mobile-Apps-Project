package com.example.quickcart

import androidx.annotation.DrawableRes

sealed class NavigationItem(val route: String, val label: String, @DrawableRes val icon: Int) {
    //Set the navigation items for the screens, text and the icons for them
    object ShoppingList : NavigationItem("shopping_list", "Shopping", R.drawable.shopping_cart_icon)
    object Receipts : NavigationItem("receipts", "Receipts", R.drawable.bookmark_icon)
    object AddItem : NavigationItem("add_item", "Add", R.drawable.plus_circle_icon)
    object History : NavigationItem("history", "History", R.drawable.globe_icon)
}
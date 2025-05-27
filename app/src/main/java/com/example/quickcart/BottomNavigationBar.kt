package com.example.quickcart

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        listOf(
            //Set list of items in bar
            NavigationItem.ShoppingList,
            NavigationItem.AddItem,
            NavigationItem.Receipts,
            NavigationItem.History
        ).forEach { item ->
            NavigationBarItem(
                //On click any item, route to its designated page
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                label = { Text(item.label) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                }
            )
        }
    }
}
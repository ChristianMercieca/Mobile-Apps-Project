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

    //Show nav bar
    NavigationBar {
        listOf(
            //List of items in bar
            NavigationItem.ShoppingList,
            NavigationItem.AddItem,
            NavigationItem.Receipts,
            NavigationItem.History
        ).forEach { item ->
            //Create a button for each screen
            NavigationBarItem(
                selected = currentRoute == item.route,
                //On click any item, route to its designated page
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                //Set text label under icon
                label = { Text(item.label) },
                //Set the icon to show
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
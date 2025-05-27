package screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import android.view.View
import com.example.quickcart.BottomNavigationBar
import com.example.quickcart.NavigationItem
import screens.SettingsFragment
import viewModel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: ShoppingListViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickCart") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("settings")
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.ShoppingList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            //Set the screens in order in the bottom nav bar
            composable(NavigationItem.ShoppingList.route) {
                ShoppingListScreen(viewModel = viewModel)
            }
            composable(NavigationItem.AddItem.route) {
                AddItemScreen(viewModel = viewModel)
            }
            composable(NavigationItem.Receipts.route) {
                ReceiptsScreen(viewModel = viewModel)
            }
            composable(NavigationItem.History.route) {
                HistoryListScreen(viewModel = viewModel, navController = navController)
            }
            //For shop history entry details
            composable("shopDetail/{shopId}") { backStackEntry ->
                val shopId = backStackEntry.arguments?.getString("shopId")?.toIntOrNull()
                if (shopId != null) {
                    ShopDetailScreen(shopId, viewModel)
                }
            }

            //Set the settings fragment icon
            composable("settings") {
                AndroidView(factory = { context ->
                    FragmentContainerView(context).apply {
                        id = View.generateViewId()
                        (context as FragmentActivity).supportFragmentManager.commit {
                            replace(this@apply.id, SettingsFragment())
                        }
                    }
                })
            }
        }
    }
}
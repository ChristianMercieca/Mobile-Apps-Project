package com.example.quickcart

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.quickcart.ui.theme.QuickCartTheme

class MainActivity : FragmentActivity() {
    private var toggleDarkMode: ((Boolean) -> Unit)? = null
    private var _darkModeState: MutableState<Boolean>? = null

    fun setDarkMode(enabled: Boolean) {
        toggleDarkMode?.invoke(enabled)
    }

    fun isDarkModeEnabled(): Boolean = _darkModeState?.value == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isDarkMode = remember { mutableStateOf(false) }
            _darkModeState = isDarkMode
            toggleDarkMode = {isDarkMode.value = it}
            QuickCartTheme(darkTheme = isDarkMode.value) {
                //Initialise database
                val context = applicationContext
                val db = QuickCartDatabase.getDatabase(context)
                val itemDao = db.itemDao()
                val historyDao = db.historyDao()

                val factory = ShoppingListViewModelFactory(itemDao, historyDao)
                val viewModel: ShoppingListViewModel = viewModel(factory = factory)
                val navController = rememberNavController()

                //Load main screen including routes
                MainScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}
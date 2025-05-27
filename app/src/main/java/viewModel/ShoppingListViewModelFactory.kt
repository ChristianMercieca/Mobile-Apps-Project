package viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import database.HistoryDao
import database.ItemDao

//Create instance of ShoppingViewModel
class ShoppingListViewModelFactory(
    private val itemDao: ItemDao,
    private val historyDao: HistoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //Check correct ViewModel class is called
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            //Typecast it as generic T
            return ShoppingListViewModel(itemDao, historyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
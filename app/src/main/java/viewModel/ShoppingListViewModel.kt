package viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import database.HistoryDao
import models.Item
import database.ItemDao
import models.ShopHistory
import com.google.gson.Gson
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val itemDao: ItemDao,
    private val historyDao: HistoryDao
) : ViewModel() {

    val shoppingItems = itemDao.getAllItems()
        .map { items -> items.sortedByDescending { it.id } }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    //Set a new Item based on parameters retrieved
    fun addItem(name: String, category: String, quantity: Int) {
        val newItem = Item(
            name = name,
            category = category,
            quantity = quantity,
            isChecked = false
        )
        viewModelScope.launch {
            //Insert the new Item record into the database
            itemDao.insertItem(newItem)
        }
    }

    fun updateItemChecked(item: Item) {
        viewModelScope.launch {
            //Room replaces item with same ID
            itemDao.insertItem(item)
        }
    }

    fun finishShopping(onComplete: (ShopHistory) -> Unit) {
        viewModelScope.launch {
            val currentItems = shoppingItems.value
            if (currentItems.isEmpty()) return@launch

            val serializedItems = Gson().toJson(currentItems)
            val shop = ShopHistory(items = serializedItems)
            val newId = historyDao.insertShop(shop)
            val fullShop = shop.copy(id = newId.toInt())
            onComplete(fullShop)
        }
    }

    fun attachReceiptToShop(shopId: Int, receiptUri: String) {
        viewModelScope.launch {
            historyDao.attachReceipt(shopId, receiptUri)
        }
    }

    fun clearShoppingList() {
        viewModelScope.launch {
            itemDao.clearAllItems()
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            itemDao.clearAllItems()
            historyDao.clearAllHistory()
        }
    }

    val shopHistory = historyDao.getAllShops()
}
package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import models.Item
import viewModel.ShoppingListViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(shopId: Int, viewModel: ShoppingListViewModel) {
    val historyList by viewModel.shopHistory.collectAsState(initial = emptyList())
    val shop = historyList.find { it.id == shopId }

    if (shop == null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Shop not found", style = MaterialTheme.typography.headlineSmall)
        }
        return
    }

    //Deserialize items JSON string into a list of Item objects
    val items: List<Item> = remember(shop.items) {
        Gson().fromJson(shop.items, Array<Item>::class.java).toList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Previous Shop") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text("Purchased Items:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            //Show items that were checked
            items.filter { it.isChecked }.forEach {
                Text("- ${it.name} (Qty: ${it.quantity})")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Not Purchased Items:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            //Show items that were not checked
            items.filter { !it.isChecked }.forEach {
                Text("- ${it.name} (Qty: ${it.quantity})")
            }

            Spacer(modifier = Modifier.height(24.dp))

            //Check if shop had a receipt, and show it if so
            if (!shop.receiptUri.isNullOrEmpty()) {
                Text("Receipt Image:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(shop.receiptUri),
                    contentDescription = "Receipt Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text("No receipt attached.", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
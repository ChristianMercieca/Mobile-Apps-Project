package com.example.quickcart

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptsScreen(viewModel: ShoppingListViewModel) {
    val historyList = viewModel.shopHistory.collectAsState(initial = emptyList()).value
    val selectedReceiptUri = remember { mutableStateOf<String?>(null) }

    val receipts = historyList
        .mapNotNull { it.receiptUri }
        .distinct()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Receipts") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                //Show message if there are no receipts
                if (receipts.isEmpty()) {
                    Text("No receipts available.")
                } else {
                    LazyVerticalGrid(
                        //Else set grid
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        //Populate grid with retrieved receipt images
                        items(receipts) { receiptUri ->
                            Image(
                                painter = rememberAsyncImagePainter(receiptUri),
                                contentDescription = "Receipt",
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedReceiptUri.value = receiptUri
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            if (selectedReceiptUri.value != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedReceiptUri.value),
                        contentDescription = "Full Receipt",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = { selectedReceiptUri.value = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            }
        }
    }
}
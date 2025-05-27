package screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import java.io.File
import android.widget.Toast
import models.Item
import models.ShopHistory
import viewModel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    val items by viewModel.shoppingItems.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val showReceiptDialog = remember { mutableStateOf(false) }
    val savedShop = remember { mutableStateOf<ShopHistory?>(null) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            coroutineScope.launch {
                savedShop.value?.let { shop ->
                    viewModel.attachReceiptToShop(shop.id, it.toString())
                }
            }
        }
    }

    val photoUri = remember { mutableStateOf<Uri?>(null) }

    //Launch camera, and save picture taken
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri.value?.let { uri ->
                savedShop.value?.let { shop ->
                    coroutineScope.launch {
                        viewModel.attachReceiptToShop(shop.id, uri.toString())
                    }
                }
            }
        }
    }

    //Ask user for permission to open camera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        //If granted permission, launch camera
        if (isGranted) {
            val photoFile = File(
                context.cacheDir,
                "receipt_${System.currentTimeMillis()}.jpg"
            )
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            photoUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    ShoppingItemRow(item = item, onCheckedChange = { isChecked ->
                        viewModel.updateItemChecked(item.copy(isChecked = isChecked))
                    })
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.finishShopping { shop ->
                            savedShop.value = shop
                            showReceiptDialog.value = true
                        }
                        //Remove items from shopping list
                        viewModel.clearShoppingList()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finish Shopping")
            }

            if (showReceiptDialog.value) {
                //Show pop up box after clicking finish shopping button
                AlertDialog(
                    onDismissRequest = { showReceiptDialog.value = false },
                    title = { Text("Attach Receipt?") },
                    text = { Text("Would you like to take a receipt image for this shopping trip?") },
                    confirmButton = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    showReceiptDialog.value = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("No")
                            }

                            Button(
                                onClick = {
                                    showReceiptDialog.value = false
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CAMERA
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        //Save photo as file
                                        val photoFile = File(
                                            context.cacheDir,
                                            "receipt_${System.currentTimeMillis()}.jpg"
                                        )
                                        //Save uri
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            photoFile
                                        )
                                        photoUri.value = uri
                                        cameraLauncher.launch(uri)
                                    } else {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Take a Picture")
                            }

                            Button(
                                onClick = {
                                    showReceiptDialog.value = false
                                    //Launch device gallery
                                    imagePickerLauncher.launch("image/*")
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Choose from Gallery")
                            }
                        }
                    },
                    dismissButton = {}
                )
            }
        }
    }
}

@Composable
fun ShoppingItemRow(item: Item, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "${item.category} — Qty: ${item.quantity}")
        }
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
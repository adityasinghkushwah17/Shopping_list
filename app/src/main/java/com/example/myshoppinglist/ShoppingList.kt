package com.example.myshoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Shoppingitem(val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList() {
    var sitems by remember { mutableStateOf(listOf<Shoppingitem>()) }

    var showDialog by remember { mutableStateOf(false) }
    var itemname by remember { mutableStateOf("") }
    var itemquantity by remember { mutableStateOf("1") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(50))
            ) {
                Text("Add Item", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(sitems) { item ->
                    if (item.isEditing) {
                        ShoppingitemEditor(item = item,
                            onEditComplete = { editedName, editedQuantity ->
                                sitems = sitems.map { if (it.id == item.id) it.copy(name = editedName, quantity = editedQuantity, isEditing = false) else it }
                            })
                    } else {
                        ShoppingitemList(item = item, onEditClick = {
                            // finding out which item we are editing and changing its isEditing boolean to true
                            sitems = sitems.map { if (it.id == item.id) it.copy(isEditing = true) else it }
                        },
                            onDeleteClick = {
                                sitems = sitems.filterNot { it.id == item.id }
                            })
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (itemname.isNotBlank()) {
                                val newitem = Shoppingitem(
                                    id = sitems.size + 1,
                                    name = itemname,
                                    quantity = itemquantity.toInt()
                                )
                                sitems = sitems + newitem
                                showDialog = false
                                itemname = ""
                            }
                        },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .padding(8.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(50))
                    ) {
                        Text(text = "Add", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { showDialog = false },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .padding(8.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(50))
                    ) {
                        Text(text = "Cancel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            title = { Text("Add Shopping item", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = itemname,
                        onValueChange = { itemname = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter item Name") })
                    OutlinedTextField(value = itemquantity,
                        onValueChange = { itemquantity = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter Quantity") })
                }
            })
    }
}

@Composable
fun ShoppingitemEditor(item: Shoppingitem, onEditComplete: (String, Int) -> Unit) {
    var editedname by remember { mutableStateOf(item.name) }
    var editedquantity by remember { mutableStateOf(item.quantity.toString()) }
    var isedited by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp), Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(value = editedname,
                onValueChange = { editedname = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            BasicTextField(value = editedquantity,
                onValueChange = { editedquantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isedited = false
            onEditComplete(editedname, editedquantity.toIntOrNull() ?: 1)
        },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(8.dp)
                .shadow(4.dp, shape = RoundedCornerShape(50))
        ) {
            Text(text = "Save", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ShoppingitemList(item: Shoppingitem,
                     onEditClick: () -> Unit, // lambda function
                     onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20)
            )
            .shadow(4.dp, shape = RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(8.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Qty: ${item.quantity}",
            modifier = Modifier.padding(8.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = { onEditClick() }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = { onDeleteClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

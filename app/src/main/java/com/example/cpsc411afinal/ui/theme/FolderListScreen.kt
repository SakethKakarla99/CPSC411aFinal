package com.example.cpsc411afinal.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cpsc411afinal.data.local.FolderEntity
import com.example.cpsc411afinal.viewmodel.FolderListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderListScreen(
    vm: FolderListViewModel,
    onBack: () -> Unit,
    onOpenFolder: (Long) -> Unit
) {
    val folders by vm.visibleFolders.collectAsState()
    val query by vm.query.collectAsState()
    var sortMenuOpen by remember { mutableStateOf(false) }


    var showAddDialog by remember { mutableStateOf(false) }
    var folderToDelete by remember { mutableStateOf<FolderEntity?>(null) }

    if (showAddDialog) {
        AddFolderDialog(
            onDismiss = { showAddDialog = false },
            onCreate = { name ->
                vm.addFolder(name)
                showAddDialog = false
            }
        )
    }

    folderToDelete?.let { f ->
        ConfirmDeleteDialog(
            title = "Delete folder?",
            body = "This will delete the folder and all notes inside it.",
            onDismiss = { folderToDelete = null },
            onConfirm = {
                vm.deleteFolder(f)
                folderToDelete = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Folders") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    Box {
                        TextButton(onClick = { sortMenuOpen = true }) { Text("Sort") }
                        DropdownMenu(
                            expanded = sortMenuOpen,
                            onDismissRequest = { sortMenuOpen = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Name (A→Z)") },
                                onClick = { vm.setSort(com.example.cpsc411afinal.viewmodel.FolderSort.NAME_AZ); sortMenuOpen = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Name (Z→A)") },
                                onClick = { vm.setSort(com.example.cpsc411afinal.viewmodel.FolderSort.NAME_ZA); sortMenuOpen = false }
                            )
                        }
                    }
                }
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = vm::setQuery,
                label = { Text("Search folders") },
                singleLine = true,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            if (folders.isEmpty()) {
                val msg = if (query.trim().isEmpty()) {
                    "Tap + to create your first folder."
                } else {
                    "No results for your search."
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Nothing here.", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text(msg)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(folders, key = { it.id }) { folder ->
                        FolderRow(
                            folder = folder,
                            onOpen = { onOpenFolder(folder.id) },
                            onDelete = { folderToDelete = folder }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }

    }
}

@Composable
private fun FolderRow(
    folder: FolderEntity,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(folder.name, style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onDelete) { Text("Delete") }
    }
}

@Composable
private fun AddFolderDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    val nameError = name.trim().isEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Folder") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Folder name") },
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("Name cannot be empty.")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(name) },
                enabled = !nameError
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun ConfirmDeleteDialog(
    title: String,
    body: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(body) },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Delete") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}



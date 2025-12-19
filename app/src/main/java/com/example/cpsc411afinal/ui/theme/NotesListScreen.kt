package com.example.cpsc411afinal.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cpsc411afinal.data.local.NoteEntity
import com.example.cpsc411afinal.viewmodel.NoteSort
import com.example.cpsc411afinal.viewmodel.NotesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    vm: NotesListViewModel,
    folderName: String,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (NoteEntity) -> Unit
) {
    val notes by vm.visibleNotes.collectAsState()
    val query by vm.query.collectAsState()
    val sort by vm.sort.collectAsState()

    var noteToDelete by remember { mutableStateOf<NoteEntity?>(null) }
    var sortMenuOpen by remember { mutableStateOf(false) }

    noteToDelete?.let { n ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("Delete note?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                Button(onClick = {
                    vm.delete(n)
                    noteToDelete = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(folderName) },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
                actions = {
                    Box {
                        TextButton(onClick = { sortMenuOpen = true }) { Text("Sort") }
                        DropdownMenu(
                            expanded = sortMenuOpen,
                            onDismissRequest = { sortMenuOpen = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Date (Newest)") },
                                onClick = { vm.setSort(NoteSort.DATE_NEWEST); sortMenuOpen = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Date (Oldest)") },
                                onClick = { vm.setSort(NoteSort.DATE_OLDEST); sortMenuOpen = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Title (A→Z)") },
                                onClick = { vm.setSort(NoteSort.TITLE_AZ); sortMenuOpen = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Title (Z→A)") },
                                onClick = { vm.setSort(NoteSort.TITLE_ZA); sortMenuOpen = false }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = { FloatingActionButton(onClick = onAdd) { Text("+") } }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Search box
            OutlinedTextField(
                value = query,
                onValueChange = vm::setQuery,
                label = { Text("Search notes") },
                singleLine = true,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            if (notes.isEmpty()) {
                val msg = if (query.trim().isEmpty()) {
                    "No notes yet. Tap + to add your first note."
                } else {
                    "No results for your search."
                }

                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Nothing here.", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text(msg)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(notes, key = { it.id }) { note ->
                        NoteRow(
                            note = note,
                            onEdit = { onEdit(note) },
                            onDelete = { noteToDelete = note }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteRow(
    note: NoteEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(note.title, style = MaterialTheme.typography.titleMedium)
            if (note.content.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = note.content.take(80) + if (note.content.length > 80) "..." else "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Row {
            TextButton(onClick = onEdit) { Text("Edit") }
            TextButton(onClick = onDelete) { Text("Delete") }
        }
    }
}





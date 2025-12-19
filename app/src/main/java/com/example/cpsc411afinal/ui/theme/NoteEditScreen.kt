package com.example.cpsc411afinal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cpsc411afinal.viewmodel.NoteEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    title: String,
    vm: NoteEditViewModel,
    onBack: () -> Unit
) {
    val s by vm.ui.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = s.title,
                onValueChange = vm::setTitle,
                label = { Text("Title") },
                isError = s.titleError != null,
                supportingText = { s.titleError?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = s.content,
                onValueChange = vm::setContent,
                label = { Text("Content") },
                minLines = 6,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { vm.save(onDone = onBack) },
                enabled = !s.saving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (s.saving) {
                    CircularProgressIndicator(strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Saving...")
                } else {
                    Text("Save")
                }
            }
        }
    }
}



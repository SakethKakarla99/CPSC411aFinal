package com.example.cpsc411afinal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cpsc411afinal.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    vm: ProfileViewModel,
    onBack: () -> Unit,
    onSignOut: () -> Unit
) {
    val s by vm.ui.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
            Text("Email", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(s.email, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(18.dp))

            OutlinedTextField(
                value = s.name,
                onValueChange = vm::setName,
                label = { Text("Display name") },
                isError = s.nameError != null,
                supportingText = { s.nameError?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            s.savedMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = vm::save,
                enabled = !s.saving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (s.saving) {
                    CircularProgressIndicator(strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Saving...")
                } else {
                    Text("Save name")
                }
            }

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign out")
            }
        }
    }
}




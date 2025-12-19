package com.example.cpsc411afinal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cpsc411afinal.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel,
    onGoProfile: () -> Unit,
    onGoFolders: () -> Unit,
    onSignOut: () -> Unit
)
 {
    val state by vm.ui.collectAsState()
     val displayName = state.displayName.ifBlank { "User" }


     Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    TextButton(onClick = onGoProfile) {
                        Text("Profile")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "You’re logged in as ${state.displayName} ",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onGoFolders,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("My Folders")
            }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign out")
            }

        }
    }
}


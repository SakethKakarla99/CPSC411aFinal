package com.example.cpsc411afinal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cpsc411afinal.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun SignupScreen(
    vm: AuthViewModel,
    onGoLogin: () -> Unit
) {
    val s by vm.ui.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Sign Up") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = s.email,
                onValueChange = vm::setEmail,
                label = { Text("Email") },
                isError = s.emailError != null,
                supportingText = { s.emailError?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = s.password,
                onValueChange = vm::setPassword,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = s.passwordError != null,
                supportingText = { s.passwordError?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            s.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = vm::signUp,
                enabled = !s.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (s.loading) {
                    CircularProgressIndicator(strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Creating...")
                } else {
                    Text("Create account")
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onGoLogin,
                enabled = !s.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to login")
            }
        }
    }
}
package com.app.citypulse.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContraseñaOlvidada(navController: NavHostController) {
    var emailState by remember { mutableStateOf("") }
    var confirmEmailState by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isEmailMatch by remember { mutableStateOf(true) }
    val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Recuperar Contraseña") }) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = emailState,
                onValueChange = { newValue ->
                    emailState = newValue.trim()
                    isEmailValid = emailRegex.matches(emailState)
                },
                label = { Text("Correo Electrónico") },
                isError = !isEmailValid,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isEmailValid) {
                Text(
                    text = "Correo no válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmEmailState,
                onValueChange = { newValue ->
                    confirmEmailState = newValue.trim()
                    isEmailMatch = confirmEmailState == emailState
                },
                label = { Text("Confirmar Correo Electrónico") },
                isError = !isEmailMatch,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isEmailMatch) {
                Text(
                    text = "Los correos no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val email = emailState
                    if (email.isNotEmpty() && isEmailValid && isEmailMatch) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(navController.context, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                } else {
                                    Toast.makeText(navController.context, "Error al enviar el correo de recuperación", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(navController.context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Correo de Recuperación")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigateUp() // Navega de vuelta a la pantalla anterior (login)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray // Puedes cambiar el color si lo deseas
                )
            ) {
                Text("Volver al Login")
            }
        }
    }
}
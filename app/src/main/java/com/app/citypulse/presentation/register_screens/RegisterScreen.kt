package com.app.citypulse.presentation.register_screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.R
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    innerPadding: PaddingValues
) {
    val backgroundImage = if (isSystemInDarkTheme()) R.drawable.hotelvelabarna else R.drawable.dubai

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 32.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_screen),
                    contentDescription = "Logo de la app",
                    modifier = Modifier
                        .size(140.dp)
                        .statusBarsPadding()
                )

                Text("CityPulse",
                    color = Color.White,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center)

                Text(
                    "¡Una aplicación que te ayudará a encontrar eventos cerca de ti!",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.White.copy(alpha = 0.8f)),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo Electrónico") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        TextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Repetir Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(
                                errorMessage,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        Log.d("RegisterScreen", "Verificando correo: $email")

                        val missingFields = mutableListOf<String>()
                        if (email.isBlank()) missingFields.add("Correo electrónico")
                        if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) missingFields.add("Correo electrónico inválido")
                        if (password.length < 6) missingFields.add("Contraseña (mínimo 6 caracteres)")
                        if (password != confirmPassword) missingFields.add("Las contraseñas no coinciden")

                        if (missingFields.isNotEmpty()) {
                            errorMessage = "Faltan los siguientes campos: ${missingFields.joinToString(", ")}"
                            return@Button
                        }

                        viewModel.checkIfUserExists(email) { exists ->
                            Log.d("RegisterScreen", "Correo $email ya registrado: $exists")

                            if (exists) {
                                errorMessage = "El correo electrónico ya está registrado."
                            } else {
                                viewModel.setTempUserData(email, password)
                                navController.navigate("register2")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Siguiente")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Aquí agregamos el botón "¿Ya tienes cuenta? Inicia sesión"
                TextButton(
                    onClick = {
                        navController.navigate("login")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
                ) {
                    Text(
                        "¿Ya tienes cuenta? Inicia sesión",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

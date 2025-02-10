package com.app.citypulse.presentation.register_screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.app.citypulse.R
import com.app.citypulse.data.dataUsers.AccountType
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun RegisterScreen2(navController: NavController, viewModel: AuthViewModel) {
    val backgroundImage = if (isSystemInDarkTheme()) R.drawable.hotelvelabarna else R.drawable.dubai

    var userType by remember { mutableStateOf<AccountType?>(null) }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var documentId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(viewModel.getTempUserData()["email"] as? String ?: "") }
    var gender by remember { mutableStateOf<String>("") }
    var fiscalAddress by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Título centrado
                Text(
                    "Completa tu perfil",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Botón para volver atrás
                if (userType != null) {
                    Button(
                        onClick = {
                            userType = null
                        },
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 16.dp)
                    ) {
                        Text("Volver")
                    }
                }

                // Botones para seleccionar tipo de usuario
                if (userType == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = {
                                userType = AccountType.Persona
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Soy una persona")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                userType = AccountType.Organizador
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Soy un organizador")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                userType = AccountType.Asociacion
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Soy una asociación")
                        }
                    }
                } else {
                    // Formulario para "persona"
                    if (userType == AccountType.Persona) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(value = surname, onValueChange = { surname = it }, label = { Text("Apellidos") })
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(value = age, onValueChange = { age = it }, label = { Text("Edad") })
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(value = documentId, onValueChange = { documentId = it }, label = { Text("DNI") })
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(onClick = { gender = "Masculino" }, modifier = Modifier.weight(1f)) {
                                    Text("Masculino")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { gender = "Femenino" }, modifier = Modifier.weight(1f)) {
                                    Text("Femenino")
                                }
                            }
                        }
                    } else if (userType == AccountType.Organizador || userType == AccountType.Asociacion) {
                        // Formulario para "organizador" o "asociación"
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(value = documentId, onValueChange = { documentId = it }, label = { Text("ID o NIF") })
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(value = fiscalAddress, onValueChange = { fiscalAddress = it }, label = { Text("Dirección fiscal") })
                        }
                    }
                }

                // Validación de campos
                val isAgeValid = age.toIntOrNull() != null
                val isFormValid = when (userType) {
                    AccountType.Persona -> name.isNotBlank() && surname.isNotBlank() && isAgeValid && documentId.isNotBlank() && gender.isNotBlank()
                    AccountType.Organizador, AccountType.Asociacion -> name.isNotBlank() && documentId.isNotBlank() && fiscalAddress.isNotBlank()
                    else -> false
                }

                // Botón de navegación
                if (userType != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Llamada corregida con userType incluido
                    Button(
                        onClick = {
                            if (isFormValid) {
                                val ageInt = age.toIntOrNull() ?: 0
                                viewModel.registerCompleteUser(
                                    name,
                                    surname,
                                    ageInt,
                                    documentId,
                                    gender,
                                    fiscalAddress,
                                    userType ?: AccountType.Persona// Asegura que nunca sea null
                                ) { isRegistered ->
                                    if (isRegistered) {
                                        navController.navigate("map")
                                    } else {
                                        errorMessage = "Ocurrió un error al registrar el usuario."
                                    }
                                }
                            } else {
                                errorMessage = "Por favor, completa todos los campos correctamente."
                            }
                        },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Siguiente")
                    }

                }

                // Mostrar mensaje de error si existe
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                TextButton(
                    onClick = {
                        // Aquí va la navegación al login
                        navController.navigate("login")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "¿Ya tienes cuenta? Inicia sesión",
                        color = Color.White.copy(alpha = 0.8f), // Letras claras pero no mucho
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

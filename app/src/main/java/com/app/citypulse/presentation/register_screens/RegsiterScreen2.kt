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
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

// Función para validar el DNI
fun isValidDNI(dni: String): Boolean {
    val dniPattern = Regex("^[0-9]{8}[A-Za-z]$")
    return dni.matches(dniPattern)
}

// Función para validar el NIF
fun isValidNIF(nif: String): Boolean {
    val nifPattern = Regex("^[A-Za-z]{1}[0-9]{8}$")
    return nif.matches(nifPattern)
}

@Composable
fun RegisterScreen2(navController: NavController, viewModel: AuthViewModel) {
    val backgroundImage = if (isSystemInDarkTheme()) R.drawable.hotelvelabarna else R.drawable.dubai

    var userType by remember { mutableStateOf<AccountType?>(null) }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
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
                            TextField(value = documentId, onValueChange = { documentId = it }, label = { Text("DNI (00000000X)") })
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = { gender = "Masculino" },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (gender == "Masculino") Color.Blue else Color.Gray
                                    )
                                ) {
                                    Text("Masculino", color = Color.White)
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { gender = "Femenino" },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (gender == "Femenino") Color.Magenta else Color.Gray
                                    )
                                ) {
                                    Text("Femenino", color = Color.White)
                                }
                            }
                        }
                    } else if (userType == AccountType.Organizador || userType == AccountType.Asociacion) {
                        // Formulario para "organizador" o "asociación"
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(value = documentId, onValueChange = { documentId = it }, label = { Text("NIF (X00000000)") })
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(value = fiscalAddress, onValueChange = { fiscalAddress = it }, label = { Text("Dirección fiscal") })
                        }
                    }
                }

                // Validación de campos
                val isDocumentIdValid = when (userType) {
                    AccountType.Persona -> isValidDNI(documentId)
                    AccountType.Organizador, AccountType.Asociacion -> isValidNIF(documentId)
                    else -> false
                }

                val missingFields = mutableListOf<String>()

                val isFormValid = when (userType) {
                    AccountType.Persona -> {
                        missingFields.clear()
                        if (name.isBlank()) missingFields.add("Nombre")
                        if (surname.isBlank()) missingFields.add("Apellido")
                        if (documentId.isBlank()) missingFields.add("Documento de identidad")
                        if (gender.isBlank()) missingFields.add("Género")
                        if (!isDocumentIdValid) missingFields.add("Documento de identidad inválido")
                        missingFields.isEmpty()
                    }
                    AccountType.Organizador, AccountType.Asociacion -> {
                        missingFields.clear()
                        if (name.isBlank()) missingFields.add("Nombre")
                        if (documentId.isBlank()) missingFields.add("Documento de identidad")
                        if (fiscalAddress.isBlank()) missingFields.add("Dirección fiscal")
                        if (!isDocumentIdValid) missingFields.add("Documento de identidad inválido")
                        missingFields.isEmpty()
                    }
                    else -> false
                }

                // Botón de navegación
                if (userType != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (isFormValid) {
                                // Obtener el UID de la sesión actual (usando Firebase Auth como ejemplo)
                                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""  // Si no hay uid, lo dejamos vacío

                                // Crear un objeto UserItem con todos los datos
                                val userItem = UserItem(
                                    name = name,
                                    surname = surname,
                                    email = email,  // Asegúrate de tener el campo 'email' definido
                                    documentId = documentId,
                                    gender = gender,
                                    userType = userType ?: AccountType.Persona,
                                    valoracion = null,  // Si lo tienes, pasa el valor
                                    uid = uid,  // Asegúrate de tener el campo 'uid' definido
                                    google = "no",  // Asegúrate de tener el campo 'google' definido
                                    friends = mutableListOf() // Asegúrate de tener el campo 'friends' definido
                                )


                                viewModel.registerCompleteUser(
                                    userItem = userItem,  // Pasar el objeto completo
                                    fiscalAddress = fiscalAddress  // Asegúrate de que 'fiscalAddress' también se esté pasando correctamente
                                ) { isRegistered ->
                                    if (isRegistered) {
                                        navController.navigate("main_screen")  // Navegar a la siguiente pantalla si es exitoso
                                    } else {
                                        errorMessage = "Ocurrió un error al registrar el usuario."
                                    }
                                }
                            } else {
                                errorMessage = "Faltan los siguientes campos: ${missingFields.joinToString(", ")}"
                            }
                        },
                        // Aquí puedes agregar otras configuraciones del botón si lo necesitas
                    ) {
                        Text("Siguiente")  // El texto que aparece en el botón
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
                        navController.navigate("login")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "¿Ya tienes cuenta? Inicia sesión",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

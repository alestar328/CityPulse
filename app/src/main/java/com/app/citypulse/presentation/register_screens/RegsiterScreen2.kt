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
import java.util.Locale

@Composable
fun RegisterScreen2(navController: NavController, viewModel: AuthViewModel) {
    val backgroundImage = if (isSystemInDarkTheme()) R.drawable.hotelvelabarna else R.drawable.dubai

    var userType by remember { mutableStateOf<AccountType?>(null) }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var documentId by remember { mutableStateOf("") }
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
                    modifier = Modifier.padding(bottom = 32.dp))

                if (userType == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AccountType.values().forEach { type ->
                            Button(onClick = { userType = type }, modifier = Modifier.fillMaxWidth()) {
                                Text("Soy ${type.name.lowercase(Locale.getDefault())}")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                } else {
                    RegisterForm(userType!!, name, surname, age, documentId, gender, fiscalAddress, onFormChange = { n, s, a, d, g, f ->
                        name = n; surname = s; age = a; documentId = d; gender = g; fiscalAddress = f
                    })

                    val isFormValid = validateForm(userType!!, name, surname, age, documentId, gender, fiscalAddress)

                    Button(onClick = {
                        val ageInt = age.toIntOrNull() ?: 0
                        val user = viewModel.getTempUserData()?.copy(
                            name = name, surname = surname, age = ageInt, documentId = documentId,
                            gender = gender, fiscalAddress = fiscalAddress, userType = userType!!
                        )
                        if (user != null) {
                            viewModel.registerUser(user) { isRegistered ->
                                if (isRegistered) navController.navigate("map_screen")
                                else errorMessage = "Error al registrar usuario."
                            }
                        }
                    }, enabled = isFormValid, modifier = Modifier.fillMaxWidth()) { Text("Siguiente") }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}


@Composable
fun RegisterForm(
    userType: AccountType,
    name: String,
    surname: String,
    age: String,
    documentId: String,
    gender: String,
    fiscalAddress: String,
    onFormChange: (String, String, String, String, String, String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = name,
            onValueChange = { onFormChange(it, surname, age, documentId, gender, fiscalAddress) },
            label = { Text("Nombre") })
        if (userType == AccountType.Persona) {
            TextField(
                value = surname,
                onValueChange = { onFormChange(name, it, age, documentId, gender, fiscalAddress) },
                label = { Text("Apellidos") })
            TextField(
                value = age,
                onValueChange = {
                    onFormChange(
                        name,
                        surname,
                        it,
                        documentId,
                        gender,
                        fiscalAddress
                    )
                },
                label = { Text("Edad") })
            GenderSelection(gender) { g ->
                onFormChange(
                    name,
                    surname,
                    age,
                    documentId,
                    g,
                    fiscalAddress
                )
            }
        }
        TextField(
            value = documentId,
            onValueChange = { onFormChange(name, surname, age, it, gender, fiscalAddress) },
            label = { Text("Documento") })
        if (userType != AccountType.Persona) {
            TextField(
                value = fiscalAddress,
                onValueChange = { onFormChange(name, surname, age, documentId, gender, it) },
                label = { Text("Dirección fiscal") })
        }
    }
}

@Composable
fun GenderSelection(selectedGender: String, onSelect: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = { onSelect("Masculino") }) { Text("Masculino") }
        Button(onClick = { onSelect("Femenino") }) { Text("Femenino") }
    }
}

fun validateForm(
    userType: AccountType,
    name: String,
    surname: String,
    age: String,
    documentId: String,
    gender: String,
    fiscalAddress: String
): Boolean {
    return when (userType) {
        AccountType.Persona -> name.isNotBlank() && surname.isNotBlank() && age.toIntOrNull() != null && documentId.isNotBlank() && gender.isNotBlank()
        else -> name.isNotBlank() && documentId.isNotBlank() && fiscalAddress.isNotBlank()
    }
}


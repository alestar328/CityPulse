package com.app.citypulse.presentation.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.app.citypulse.R
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.unit.sp
import com.app.citypulse.data.dataUsers.AccountType
import com.app.citypulse.data.dataUsers.UserItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    val backgroundImage = if (isSystemInDarkTheme()) R.drawable.hotelvelabarna else R.drawable.dubai

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity

    // Nuevo launcher para manejar el resultado de la actividad de Google Sign-In
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)

            if (account != null) {
                // Extraer la información de la cuenta de Google
                val idToken = account.idToken
                if (idToken != null) {
                    // Usar el idToken para autenticar con Firebase
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(activity!!) { authTask ->
                            if (authTask.isSuccessful) {
                                // El usuario está autenticado con Firebase
                                val firebaseUser = FirebaseAuth.getInstance().currentUser
                                val name = account.displayName?.split(" ")?.get(0) ?: "Nombre"
                                val surname = account.displayName?.split(" ")?.getOrElse(1) { "Apellido" } ?: "Apellido"
                                val email = account.email ?: "email@default.com"
                                val documentId = null // Si tienes un campo para el documento del usuario
                                val userType = AccountType.Persona // El tipo de cuenta que desees asignar
                                val password = generatePassword() // Función para generar una contraseña automáticamente
                                val gender = null // Género si lo tienes

                                // Crear un objeto UserItem
                                val user = UserItem(
                                    name = name,
                                    surname = surname,
                                    age = 30,
                                    email = email,
                                    documentId = documentId,
                                    userType = userType,
                                    valoracion = null,
                                    password = password,
                                    gender = gender,
                                )

                                // Guardar el usuario en la base de datos o en el ViewModel
                                viewModel.saveUser(user) { success ->
                                    if (success) {
                                        // Si el usuario se guardó correctamente, navega al mapa
                                        navController.navigate("main_screen") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        loginError = true
                                    }
                                }
                            } else {
                                loginError = true
                            }
                        }
                } else {
                    loginError = true
                }
            }
        } catch (e: ApiException) {
            loginError = true
        }
    }

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
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_screen),
                    contentDescription = "Logo de la app",
                    modifier = Modifier
                        .size(140.dp)
                )

                Text("CityPulse", color = Color.White, fontSize = 40.sp, textAlign = TextAlign.Center)

                Text("¡Encuentra eventos cerca de ti!", color = Color.White, fontSize = 22.sp, textAlign = TextAlign.Center)

                // Formulario para iniciar sesión con correo y contraseña
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo Electrónico") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (loginError) {
                            Text(
                                "Error al iniciar sesión. Verifique sus credenciales.",
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
                // Botón de inicio de sesión con correo y contraseña
                Button(
                    onClick = {
                        viewModel.login(email, password) { success ->
                            if (success) {
                                navController.navigate("main_screen") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                loginError = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para iniciar sesión con Google
                Button(
                    onClick = {
                        // Verificamos si el usuario ya está autenticado con Google
                        val account = GoogleSignIn.getLastSignedInAccount(context)
                        if (account != null) {
                            // Si ya hay una cuenta, deslogueamos
                            activity?.let { activity ->
                                val googleSignInClient = viewModel.getGoogleSignInClient(activity)
                                googleSignInClient.signOut().addOnCompleteListener {
                                    val signInIntent = googleSignInClient.signInIntent
                                    googleSignInLauncher.launch(signInIntent) // Lanza el Intent de Google
                                }
                            }
                        } else {
                            // Si no hay ninguna cuenta autenticada, iniciamos sesión directamente
                            activity?.let { activity ->
                                val googleSignInClient = viewModel.getGoogleSignInClient(activity)
                                val signInIntent = googleSignInClient.signInIntent
                                googleSignInLauncher.launch(signInIntent) // Lanza el Intent de Google
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.googlelogo),
                        contentDescription = "Logo de Google",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iniciar sesión con Google", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Botón para ir a la pantalla de registro
                Button(
                    onClick = { navController.navigate("register") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
                ) {
                    Text(
                        "¿No tienes cuenta? Regístrate aquí",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Función para generar una contraseña automáticamente
private fun generatePassword(): String {
    return "defaultPassword123" // Ejemplo
}

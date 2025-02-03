package com.app.citypulse.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.app.citypulse.R

@Preview
@Composable
fun LoginScreen() {
    val backgroundImage = if (isSystemInDarkTheme()) {
        R.drawable.hotelvelabarna // Imagen para modo oscuro
    } else {
        R.drawable.dubai // Imagen para modo claro
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Contenedor principal para el contenido de la pantalla
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                // Logo en la parte superior
                Image(
                    painter = painterResource(id = R.drawable.splash_screen), // Logo
                    contentDescription = "Logo de la app",
                    modifier = Modifier
                        .width(230.dp)
                        .height(230.dp)
                        .padding(top = 20.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Título Login en blanco
                Text(
                    text = "CityPulse",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                )

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    "¡Una aplicación que te ayudara a encontrar eventos cerca de ti!",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Campo de correo
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de contraseña
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /*logica */},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF333333)
                            ),

                ){
                    Text(
                        "¿Olvido su contraseña?",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,)
                }

                Spacer(modifier = Modifier.height(16.dp))


                // Botón de inicio de sesión
                Button(
                    onClick = { /* Lógica para iniciar sesión */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón de inicio de sesión con Google
                Button(
                    onClick = { /* Lógica para iniciar sesión con Google */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                        Image(
                            painter = painterResource(id = R.drawable.googlelogo), // Logo de Google
                            contentDescription = "Logo de Google",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciar sesión con Google", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(56.dp))

                Button(
                    onClick = { /*logica */},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF333333)
                    ),

                    ){
                    Text(
                        "¿No tienes cuenta? Registrate Aqui",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,)
                }
            }
        }
    }
}

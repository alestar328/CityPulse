package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.presentation.components.ActionBox
import com.app.citypulse.presentation.components.ButtonBar
import com.app.citypulse.presentation.components.PersonalScoreBar
import com.app.citypulse.presentation.components.PhotoContainer
import com.app.citypulse.presentation.components.ProfileHeader
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel
) {

    var user by remember { mutableStateOf<UserItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }

// Obtener datos del usuario
    LaunchedEffect(Unit) {
        viewModel.getUserData { fetchedUser ->
            user = fetchedUser
            isLoading = false
            println("🔥 Datos del usuario obtenidos: $fetchedUser") // Agrega este log

        }
    }
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(horizontal = 16.dp), // Margen lateral
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(user)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Estrella",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
            PersonalScoreBar()
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado entre botones
            ) {
                ButtonBar("Mis Amigos", backgroundColor = Color.White)
                ButtonBar("Mis Descuentos", backgroundColor = Color.White)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, // Espaciado entre ActionBoxes
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ActionBox(
                        icon = Icons.Default.Check,
                        "Eventos asistidos",
                        modifier = Modifier
                    ) {
                        navController.navigate("assisted_events")
                    }

                    ActionBox(
                        icon = Icons.Default.Favorite,
                        "Eventos guardados",
                        modifier = Modifier
                    ) {
                        navController.navigate("assisted_events")
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround, // Espaciado entre ActionBoxes
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PhotoContainer { }
                    PhotoContainer { }
                    PhotoContainer { }
                }
                ButtonBar("Cerrar Sesión", backgroundColor = Color.Red)
            }
        }

        }
    }

    /*@Preview
    @Composable
    fun ProfileScreenPreview() {
        ProfileScreen(navController = NavController(LocalContext.current))
    }*/

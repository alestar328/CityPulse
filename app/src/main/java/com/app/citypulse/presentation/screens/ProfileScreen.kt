package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.presentation.components.ActionBox
import com.app.citypulse.presentation.components.ButtonBar
import com.app.citypulse.presentation.components.PersonalScoreBar
import com.app.citypulse.presentation.components.PhotoContainer


import com.app.citypulse.presentation.viewmodel.AuthViewModel


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel
) {
    var user by remember { mutableStateOf<UserItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Llamamos a loadUserData (asegúrate de tenerla implementada en tu AuthViewModel)
    LaunchedEffect(Unit) {
        viewModel.loadUserData { fetchedUser ->
            user = fetchedUser
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            // Encabezado integrado (antes era ProfileHeader separado)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = user?.name?.plus(" ")?.plus(user?.surname ?: "") ?: "Nombre Apellido",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Text(
                        text = user?.email ?: "Correo no disponible",
                        fontSize = 16.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user?.userType?.name ?: "Tipo Usuario",
                        fontSize = 14.sp,
                        color = Color(0xFFBBDEFB)
                    )
                }
            }
            // Reseña con 5 estrellas
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
            // Componente para mostrar puntaje personal (ya existente)
            PersonalScoreBar()
            Spacer(modifier = Modifier.height(12.dp))
            // Botones y cajas de acción
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ButtonBar("Mis Amigos", backgroundColor = Color.White, onClick = {  } )


                ButtonBar("Mis Descuentos", backgroundColor = Color.White, onClick = {  })


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ActionBox(
                        icon = Icons.Default.Check,
                        title = "Eventos asistidos",
                        modifier = Modifier,
                        onClick = { navController.navigate("assisted_events") }
                    )
                    ActionBox(
                        icon = Icons.Default.Favorite,
                        title = "Eventos guardados",
                        modifier = Modifier,
                        onClick = { navController.navigate("assisted_events") }
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PhotoContainer { }
                    PhotoContainer { }
                    PhotoContainer { }
                }
                ButtonBar("Cerrar Sesión", backgroundColor = Color.Red, onClick = { viewModel.logout() })


            }
        }
    }
}
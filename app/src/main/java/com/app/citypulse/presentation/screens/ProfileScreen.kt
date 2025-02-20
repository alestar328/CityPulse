package com.app.citypulse.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import android.net.Uri
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
    var selectedImageUri0 by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri1 by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri2 by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri3 by remember { mutableStateOf<Uri?>(null) }

    val launcher0 = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri0 = uri }

    val launcher1 = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri1 = uri }

    val launcher2 = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri2 = uri }

    val launcher3 = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri3 = uri }



    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para obtener contenido de tipo imagen
    val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            // Actualizamos el estado con la URI seleccionada
            selectedImageUri = uri
    }
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
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileHeader(
                    user = user!!,
                    selectedImageUri = selectedImageUri,
                    onClick = {
                        // Al hacer click se abre la galería para elegir una foto de perfil
                        galleryLauncher.launch("image/*")
                    }
                )
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
                    PhotoContainer (
                        selectedImageUri = selectedImageUri1,
                        onClick = {
                            // Al hacer click se abre la galería
                            launcher1.launch("image/*")

                        }
                    )
                    PhotoContainer (
                        selectedImageUri = selectedImageUri2,
                        onClick = {
                            // Al hacer click se abre la galería
                            launcher2.launch("image/*")
                        }
                    )
                    PhotoContainer (
                        selectedImageUri = selectedImageUri3,
                        onClick = {
                            // Al hacer click se abre la galería
                            launcher3.launch("image/*")
                        }
                    )
                }
                ButtonBar("Cerrar Sesión", backgroundColor = Color.Red, onClick = { viewModel.logout() })


            }
        }
    }
}
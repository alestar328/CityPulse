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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import com.app.citypulse.presentation.components.ActionBox
import com.app.citypulse.presentation.components.ButtonBar
import com.app.citypulse.presentation.components.PersonalScoreBar
import com.app.citypulse.presentation.components.PhotoContainer
import com.app.citypulse.presentation.components.ProfileHeader
import com.app.citypulse.presentation.ui.theme.YellowLight
import com.app.citypulse.presentation.viewmodel.AuthViewModel


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel,
    innerPadding: PaddingValues
) {
    val user by viewModel.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val gallery by viewModel.galleryUrls.collectAsState()

    var pendingProfileUri by remember { mutableStateOf<Uri?>(null) }
    val pendingGalleryUris = remember { mutableStateListOf<Uri?>(null, null, null) }



    LaunchedEffect(user) {
        if (user != null) {
            viewModel.loadGallery()
        }
        isLoading = false
    }

    val profileImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            pendingProfileUri = uri
        }
    }


    val launcher1 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pendingGalleryUris[0] = uri
    }
    val launcher2 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pendingGalleryUris[1] = uri
    }
    val launcher3 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pendingGalleryUris[2] = uri
    }





    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
    } else {
        user?.let { currentUser ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Encabezado integrado (antes era ProfileHeader separado)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileHeader(
                        user = currentUser,
                        pendingProfileUri =  pendingProfileUri,
                        onClick = { profileImageLauncher.launch("image/*") }
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
                // Botones y cajas de acción
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("friends")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text("Mis Amigos")
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ActionBox(
                            icon = Icons.Default.Check,
                            title = "Próximos eventos",
                            modifier = Modifier,
                            onClick = { navController.navigate("assisted_events") }
                        )
                        ActionBox(
                            icon = Icons.Default.Favorite,
                            title = "Eventos de interés",
                            modifier = Modifier,
                            onClick = { navController.navigate("saved_events") }
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(3) { index ->
                            PhotoContainer(
                                url       = gallery.getOrNull(index),
                                localUri  = pendingGalleryUris[index],
                                modifier  = Modifier.padding(4.dp),
                                onClick   = { when(index) {
                                    0 -> launcher1.launch("image/*")
                                    1 -> launcher2.launch("image/*")
                                    2 -> launcher3.launch("image/*")
                                }},
                                onDelete  = { pendingGalleryUris[index] = null }
                            )
                        }

                    }
                    ButtonBar(
                        text = "Subir fotos",
                        backgroundColor = YellowLight,
                        onClick = {
                            // 1) Preparamos lista de (uri, uploadFunction)
                            val uploads = mutableListOf<Pair<Uri, (Uri, (Boolean,String?)->Unit) -> Unit>>().apply {
                                pendingProfileUri?.let { uri ->
                                    add(uri to viewModel::uploadProfileImage)
                                }
                                pendingGalleryUris.forEach { uri ->
                                    uri?.let { add(it to viewModel::uploadGalleryImage) }
                                }
                            }

                            if (uploads.isEmpty()) return@ButtonBar

                            // 2) Contadores
                            var done = 0
                            var allSuccess = true
                            val total = uploads.size

                            // 3) Disparar todas
                            uploads.forEach { (uri, fn) ->
                                fn(uri) { success, _ ->
                                    if (!success) allSuccess = false
                                    done++
                                    if (done == total) {
                                        // Aquí ya terminaron todas
                                        val msg = if (allSuccess) "Imágenes subidas" else "Error al subir imágenes"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        Log.d("ProfileScreen", msg)
                                    }
                                }
                            }

                            repeat(pendingGalleryUris.size) { idx ->
                                pendingGalleryUris[idx] = null
                            }
                        }
                    )
                    ButtonBar(
                        "Cerrar Sesión",
                        backgroundColor = Color.Red,
                        onClick = {
                            navController.navigate("login") {
                                popUpTo("profile") { inclusive = true }
                            }
                            viewModel.logout {
                                navController.navigate("login") } }
                    )
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se pudieron cargar los datos del usuario",
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
        }
    }

}




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
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.platform.LocalContext
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.presentation.components.ActionBox
import com.app.citypulse.presentation.components.ButtonBar
import com.app.citypulse.presentation.components.PersonalScoreBar
import com.app.citypulse.presentation.components.PhotoContainer
import com.app.citypulse.presentation.components.ProfileHeader
import com.app.citypulse.presentation.ui.theme.YellowLight
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel,
    innerPadding: PaddingValues
) {
    var user by remember { mutableStateOf<UserItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    var pendingProfileUri by remember { mutableStateOf<Uri?>(null) }
    var pendingGalleryUris by remember {
        mutableStateOf(listOf<Uri?>(null, null, null))
    }

    fun uploadImageToFirebase(
        user: UserItem,
        uri: Uri,
        isProfilePicture: Boolean,
        photoIndex: Int? = null, // Índice para diferenciar las fotos de PhotoContainer
        onSuccess: (String) -> Unit
    ) {
        val storageRef = Firebase.storage.reference
        val userId = user.uid ?: "anonymous"
        val fileName = when {
            isProfilePicture -> "profile.jpg"
            photoIndex != null -> "photo_$photoIndex.jpg"
            else -> "unknown.jpg"
        }

        // Carpeta del usuario en Firebase Storage
        val imagePath = "profile_pictures/$userId/$fileName"
        val imageRef = storageRef.child(imagePath)
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener {
                println("⚠️ Error al subir la imagen: ${it.message}")
            }
    }


    val profileImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            pendingProfileUri = uri
        }
    }


    // Launchers para cada una de las 3 fotos adicionales
    val galleryLauncher1 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // Copiamos la lista y cambiamos solo el primer elemento
            pendingGalleryUris = pendingGalleryUris.toMutableList().also { it[0] = uri }
        }
    }
    // Launchers para cada una de las 3 fotos adicionales
    val galleryLauncher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            pendingGalleryUris = pendingGalleryUris.toMutableList().also { it[1] = uri }
        }
    }

    // Launchers para cada una de las 3 fotos adicionales
    val galleryLauncher3 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            pendingGalleryUris = pendingGalleryUris.toMutableList().also { it[2] = uri }
        }
    }


    // Llamamos a loadUserData (asegúrate de tenerla implementada en tu AuthViewModel)
    LaunchedEffect(Unit) {
        viewModel.loadUserData { fetchedUser ->
            if (fetchedUser != null) {
                user = fetchedUser
                pendingProfileUri = null

                // Si `galleryPictureUrls` está vacío, aseguramos que haya 3 espacios nulos
                pendingGalleryUris = fetchedUser.galleryPictureUrls
                    .map { Uri.parse(it) }
                    .toMutableList()
                    .apply {
                        while (size < 3) add(null)  // Asegurar 3 elementos siempre
                    }
            }
            isLoading = false
        }
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
                        pendingProfileUri = pendingProfileUri,  // le pasamos la URI pendiente
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
                            title = "Eventos asistidos",
                            modifier = Modifier,
                            onClick = { navController.navigate("assisted_events") }
                        )
                        ActionBox(
                            icon = Icons.Default.Favorite,
                            title = "Eventos guardados",
                            modifier = Modifier,
                            onClick = { navController.navigate("saved_events") }
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PhotoContainer(
                            url = pendingGalleryUris[0]?.toString() ?: user?.galleryPictureUrls?.getOrNull(0),
                            localUri = pendingGalleryUris[0],
                            onClick = { galleryLauncher1.launch("image/*") },
                            onDelete = { pendingGalleryUris = pendingGalleryUris.toMutableList().also { it[0] = null } }
                        )
                        PhotoContainer(
                            url = pendingGalleryUris[1]?.toString() ?: user?.galleryPictureUrls?.getOrNull(1),
                            localUri = pendingGalleryUris[1],
                            onClick = { galleryLauncher2.launch("image/*") },
                            onDelete = { pendingGalleryUris = pendingGalleryUris.toMutableList().also { it[1] = null } }
                        )
                        PhotoContainer(
                            url = pendingGalleryUris[2]?.toString() ?: user?.galleryPictureUrls?.getOrNull(2),
                            localUri = pendingGalleryUris[2],
                            onClick = { galleryLauncher3.launch("image/*") },
                            onDelete = { pendingGalleryUris = pendingGalleryUris.toMutableList().also { it[2] = null } }
                        )

                    }
                    ButtonBar(
                        text = "Subir fotos",
                        backgroundColor = YellowLight,
                        onClick = {
                            if (user != null) {
                                val currentUser = user!!

                                // 📸 Subir foto de perfil si hay una nueva
                                if (pendingProfileUri != null) {
                                    uploadImageToFirebase(
                                        currentUser,
                                        pendingProfileUri!!,
                                        isProfilePicture = true
                                    ) { url ->
                                        viewModel.updateProfilePictureUrl(url) { success ->
                                            if (success) {
                                                user = user!!.copy(profilePictureUrl = url)
                                            }
                                        }
                                        Toast.makeText(
                                            context,
                                            "Foto de perfil subida",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    pendingProfileUri = null
                                }

                                // 🖼️ Subir las 3 fotos de la galería
                                val updatedGallery = currentUser.galleryPictureUrls.toMutableList()
                                pendingGalleryUris.forEachIndexed { index, uri ->
                                    if (uri != null) {
                                        uploadImageToFirebase(
                                            currentUser,
                                            uri,
                                            isProfilePicture = false,
                                            photoIndex = index + 1
                                        ) { url ->
                                            viewModel.addGalleryPictureUrl(url) { success ->
                                                if (success) {
                                                    updatedGallery.add(url)
                                                    user =
                                                        user!!.copy(galleryPictureUrls = updatedGallery)
                                                }
                                            }
                                        }
                                    }
                                }

                                // Limpiar las URIs después de subir
                                pendingGalleryUris = listOf(null, null, null)
                            }
                        }
                    )
                    ButtonBar(
                        "Cerrar Sesión",
                        backgroundColor = Color.Red,
                        onClick = { viewModel.logout { navController.navigate("login") } }
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




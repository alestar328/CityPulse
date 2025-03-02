package com.app.citypulse.presentation.screens

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.widget.Button
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.presentation.components.ActionBox
import com.app.citypulse.presentation.components.ButtonBar
import com.app.citypulse.presentation.components.PersonalScoreBar
import com.app.citypulse.presentation.components.PhotoContainer
import com.app.citypulse.presentation.components.ProfileHeader

import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel,
    innerPadding: PaddingValues
) {
    var user by remember { mutableStateOf<UserItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedImageUri0 by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri1 by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri2 by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri3 by remember { mutableStateOf<Uri?>(null) }

    val isUploading = remember{ mutableStateOf(false) }
    val context = LocalContext.current
    val img : Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.ic_menu_report_image)
    val bitmap = remember { mutableStateOf(img) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        if(it != null){
            bitmap.value = it
        }
    }

// Launcher para obtener contenido de tipo imagen
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        if(Build.VERSION.SDK_INT < 26){
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        } else{
            val source = it?.let{
                it1 -> ImageDecoder.createSource(context.contentResolver, it1)
            }
            bitmap.value = source?.let{
                it1 -> ImageDecoder.decodeBitmap(it1)
            }!!
        }
    }







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
                    user = user!!,
                    selectedImageUri = selectedImageUri,
                    onClick = { galleryLauncher.launch("image/*") }
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
                        onClick = { navController.navigate("assisted_events") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PhotoContainer (
                        bitmap = bitmap.value,
                        selectedImageUri = selectedImageUri1,
                        onClick = { launcher1.launch("image/*") },
                        onDelete = { selectedImageUri1 = null }

                    )
                    PhotoContainer (
                        bitmap = bitmap.value,
                        selectedImageUri = selectedImageUri2,
                        onClick = { launcher2.launch("image/*") },
                        onDelete = { selectedImageUri2 = null }

                    )
                    PhotoContainer (
                        bitmap = bitmap.value,
                        selectedImageUri = selectedImageUri3,
                        onClick = { launcher3.launch("image/*") },
                        onDelete = { selectedImageUri3 = null }
                    )
                }
                }
                ButtonBar("Subir fotos", backgroundColor = Color.Blue,
                    onClick = {
                        isUploading.value = true
                        user?.let { currentUser  ->
                            uploadImageToFirebase(currentUser, bitmap.value, context) { success, url ->
                                isUploading.value = false
                                if (success && url != null) {
                                    // Aquí, por ejemplo, se añade la URL a una lista temporal en el ViewModel:
                                    viewModel.addTempPhotoUrl(url)
                                    Toast.makeText(context, "Subida exitosa", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Fallo al cargar fotos", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                )

                ButtonBar("Cerrar Sesión", backgroundColor = Color.Red, onClick = { viewModel.logout() })


            }
        }
    }


fun uploadImageToFirebase(
    user: UserItem,
    bitmap: Bitmap,
    context: Context,
    callback: (Boolean, String?) -> Unit
) {
    val storageRef = Firebase.storage.reference
    val imageName = "images/${user.uid ?: "anonymous"}_${System.currentTimeMillis()}.jpg"
    val imageRef = storageRef.child(imageName)

    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageData = baos.toByteArray()

    imageRef.putBytes(imageData)
        .addOnSuccessListener {
            // Una vez la imagen se sube, obtenemos su URL de descarga
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Aquí retornamos true junto con la URL
                callback(true, uri.toString())
            }.addOnFailureListener {
                // Error al obtener la URL
                callback(false, null)
            }
        }
        .addOnFailureListener {
            // Error al subir la imagen
            callback(false, null)
        }
}
fun deleteImageFromFirebase(imageUrl: String, callback: (Boolean) -> Unit) {
    val storageRef = Firebase.storage.getReferenceFromUrl(imageUrl)
    storageRef.delete()
        .addOnSuccessListener { callback(true) }
        .addOnFailureListener { callback(false) }
}
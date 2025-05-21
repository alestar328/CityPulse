package com.app.citypulse.presentation.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.data.model.EventUiModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.app.citypulse.R
import com.app.citypulse.data.dataUsers.SubcatItem
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.presentation.components.CardValueStars
import com.app.citypulse.presentation.components.ReviewEventSection
import com.app.citypulse.presentation.ui.theme.TurkBlue
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.presentation.viewmodel.UserViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    event: EventUiModel,
    subCategory: SubcatItem?,
    navController: NavController,
    userViewModel: UserViewModel,
    isCreator: Boolean,
    eventViewModel: EventViewModel
) {
    var rating by remember { mutableIntStateOf(event.valoracion) }
    var profilePicUrl by remember { mutableStateOf<String?>(null) }

    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var showReviewSection by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableIntStateOf(0) }

    LaunchedEffect(event.idRealizador) {
        userViewModel.fetchUserProfilePicture(event.idRealizador) { url ->
            profilePicUrl = url
        }
    }
    LaunchedEffect(selectedRating) {
        if (selectedRating > 0) {
            kotlinx.coroutines.delay(250) // Delay tras "soltar"
            showReviewSection = true
        }
    }
    val avatarUrl = profilePicUrl ?: ""

    Log.d("EventDetailsScreen", "Subcategory: $subCategory")

    if (showReviewSection) {
        ReviewEventSection(
            placeName = event.nombre,
            avatarUrl = avatarUrl,
            initialRating = selectedRating,
            userName = "Tu nombre aquí", // o recógelo desde el userViewModel
            onBack = { showReviewSection = false },
            onPublish = { rating, reviewText ->
                showReviewSection = false
                println("⭐ Publicado: $rating estrellas, texto: $reviewText")
            }
        )
    }  else {
    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Detalles del evento") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TurkBlue,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White
            )
        )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .background(Color(0xFFFFF59D)),
                contentPadding = PaddingValues(
                    bottom = 60.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    if (event.galleryPictureUrls.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .height(400.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp)
                        ) {
                            items(event.galleryPictureUrls.size) { idx ->
                                val url = event.galleryPictureUrls[idx]
                                AsyncImage(
                                    model = url,
                                    contentDescription = "Imagen del evento $idx",
                                    modifier = Modifier
                                        .height(400.dp)
                                        .fillParentMaxWidth(0.8f)    // ocupa el 80% del ancho del padre
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay imágenes", color = Color.DarkGray)
                        }
                    }
                }
                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFF59D)),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "${event.fechaInicio} - ${event.fechaFin}",
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = event.nombre,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = event.nomOrg,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.categoria.displayName ?: event.categoria.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = event.subcategoria,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Gray // Ajusta el color según tus necesidades
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.lugar,
                            fontSize = 20.sp
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Text(text = "Aforo:", fontSize = 20.sp) //Organizador
                            Text(text = event.aforo.toString(), fontSize = 20.sp)

                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Precio:", fontSize = 18.sp) //Organizador
                            Text(
                                text = String.format(Locale.getDefault(), "%.2f €", event.precio),
                                fontSize = 18.sp
                            )

                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = event.descripcion, fontSize = 20.sp)
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = subCategory?.description ?: "Sin descripción", fontSize = 20.sp)
                    }
                }
                item {
                    CardValueStars(
                        avatarUrl = avatarUrl,
                        rating = rating,
                        onRatingChanged = { newRating ->
                            rating = newRating
                            selectedRating = newRating

                        }
                    )
                }
                if (isCreator) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botón "Borrar"
                            OutlinedButton(
                                onClick = { showDeleteConfirmationDialog = true },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, Color(0xFF1976D2)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Borrar")
                            }

                            // Botón "Aplicar"
                            Button(
                                onClick = {
                                    navController.navigate("edit_event/${event.id}")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1976D2),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Editar")
                            }
                        }
                    }
                }
            }
        }
        if (showDeleteConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmationDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que quieres eliminar este evento? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = {
                        eventViewModel.deleteEvent(event.id, navController)
                        showDeleteConfirmationDialog = false
                    }) {
                        Text("Sí, borrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteConfirmationDialog = false
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

}


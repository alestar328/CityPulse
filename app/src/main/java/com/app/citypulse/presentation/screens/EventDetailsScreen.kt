package com.app.citypulse.presentation.screens

import androidx.compose.foundation.lazy.items
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.data.model.EventUiModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.citypulse.data.dataUsers.SubcatItem
import com.app.citypulse.data.model.Comment
import com.app.citypulse.presentation.components.CardReviewEvent
import com.app.citypulse.presentation.components.CardValueStars
import com.app.citypulse.presentation.components.ReviewEventSection
import com.app.citypulse.presentation.ui.theme.TurkBlue
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.CommentsViewModel
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
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var showReviewSection by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableIntStateOf(0) }
    val loggedInAvatarUrl = currentUser?.profilePictureUrl.orEmpty()
    val userAvatar = currentUser?.profilePictureUrl.orEmpty()
    val userNameStr = "${currentUser?.name.orEmpty()} ${currentUser?.surname.orEmpty()}"
    val commentsViewModel: CommentsViewModel = viewModel()
    var commentToEdit by remember { mutableStateOf<Comment?>(null) }
    var selectedText   by remember { mutableStateOf("") }
    var commentToDeleteId by remember { mutableStateOf<String?>(null) }
    var showDeleteCommentDialog by remember { mutableStateOf(false) }
    val comments by commentsViewModel.comments.collectAsState(initial = emptyList())

    LaunchedEffect(event.idRealizador) {
        userViewModel.fetchUserProfilePicture(event.idRealizador) { url ->
            profilePicUrl = url
        }
    }
    LaunchedEffect(event.id) {
        commentsViewModel.fetchComments(event.id)
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
            avatarUrl = userAvatar,
            initialText = selectedText,  // texto previo en edición
            initialRating = selectedRating,
            userName = userNameStr, // o recógelo desde el userViewModel
            onBack = {
                showReviewSection = false
                commentToEdit = null
            },
            onPublish = { rating, reviewText ->
                if (commentToEdit != null) {
                    // — EDICIÓN —
                    commentsViewModel.updateComment(
                        eventId   = event.id,
                        commentId = commentToEdit!!.id,
                        newRating = rating,
                        newMessage = reviewText
                    ) { success ->
                        showReviewSection = false
                        commentToEdit     = null
                        if (success) commentsViewModel.fetchComments(event.id)
                    }
                } else {
                    // — CREACIÓN —
                    commentsViewModel.addComment(
                        eventId     = event.id,
                        organizerId = event.idRealizador,
                        rating      = rating,
                        message     = reviewText
                    ) { success ->
                        showReviewSection = false
                        if (success) commentsViewModel.fetchComments(event.id)
                    }
                }
            }
        )
    } else {
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
                        avatarUrl = loggedInAvatarUrl,
                        rating = rating,
                        onRatingChanged = { newRating ->
                            rating = newRating
                            selectedRating = newRating

                        }
                    )
                }
                items(comments) { comment ->
                    CardReviewEvent(
                        avatarUrl = comment.userProfileUrl.orEmpty(),
                        rating = comment.rating ?: 0,
                        userName = comment.userName,
                        userType = "", // Puedes reemplazar con datos reales si los tienes
                        commentText = comment.message.orEmpty(),
                        isCurrentUser =  comment.userId == currentUser?.uid,
                        isEventOwner = isCreator,
                        onRatingChanged = {},
                        onEdit = {
                            commentToEdit = comment
                            selectedRating  = comment.rating ?: 0
                            selectedText   = comment.message.orEmpty()
                            showReviewSection  = true
                        },
                        onDelete = {
                            commentToDeleteId = comment.id
                            showDeleteCommentDialog = true
                        },
                        onReview = { /* Agrega lógica de revisión para el creador */ }
                    )
                    Spacer(Modifier.height(24.dp))

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
                                Text("Borrar Evento")
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
                                Text("Editar Evento")
                            }
                        }
                    }
                }
            }


            if (showDeleteCommentDialog && commentToDeleteId != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteCommentDialog = false },
                    title = { Text("Borrar comentario") },
                    text = { Text("¿Seguro que quieres eliminar este comentario?") },
                    confirmButton = {
                        TextButton(onClick = {
                            commentsViewModel.deleteComment(
                                event.id,
                                commentToDeleteId!!
                            ) { success ->
                                showDeleteCommentDialog = false
                            }
                        }) {
                            Text("Borrar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteCommentDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
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


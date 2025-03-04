package com.app.citypulse.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.presentation.viewmodel.FriendsViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FriendsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FriendsViewModel
) {
    val friends by viewModel.friends.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Determinar si el usuario se autenticó con Google
    val isGoogleUser = currentUser?.providerData?.any { it.providerId == "google.com" } ?: false

    // Mostrar el email si es un usuario de Google, de lo contrario mostrar el UID
    val userIdentifier = if (isGoogleUser) {
        currentUser?.email ?: "No se pudo obtener el email"
    } else {
        currentUser?.uid ?: "No se pudo obtener el UID"
    }

    // Estado para controlar la visibilidad del diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Estado para almacenar el friendId que se va a eliminar
    var friendIdToDelete by remember { mutableStateOf<String?>(null) }

    // Mapa para almacenar los nombres de los amigos
    val friendNames = remember { mutableStateMapOf<String, String>() }

    // Logs para depuración
    LaunchedEffect(Unit) {
        Log.d("FriendsScreen", "Current user UID: ${currentUser?.uid}")
        Log.d("FriendsScreen", "Is Google user: $isGoogleUser")
        Log.d("FriendsScreen", "User identifier: $userIdentifier")
        Log.d("FriendsScreen", "Loading friends...")
        viewModel.loadFriends()
    }

    // Obtener los nombres de los amigos
    LaunchedEffect(friends) {
        friends.forEach { friendId ->
            if (!friendNames.containsKey(friendId)) {
                viewModel.getFriendName(friendId) { name ->
                    if (name != null) {
                        friendNames[friendId] = name
                    } else {
                        friendNames[friendId] = "Nombre no disponible"
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar un amigo
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                // Ocultar el diálogo si el usuario hace clic fuera de él
                showDeleteDialog = false
            },
            title = { Text("Eliminar amigo") },
            text = { Text("¿Estás seguro de que quieres eliminar a este amigo?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Ocultar el diálogo
                        showDeleteDialog = false

                        // Llamar a la función removeFriend si hay un friendId válido
                        friendIdToDelete?.let { friendId ->
                            viewModel.removeFriend(
                                friendUid = friendId,
                                currentUser = currentUser, // Pasamos el FirebaseUser actual
                                onSuccess = {
                                    Log.d("FriendsScreen", "Amigo eliminado: $friendId")
                                },
                                onError = { error ->
                                    Log.e("FriendsScreen", "Error al eliminar amigo: $error")
                                }
                            )
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Ocultar el diálogo sin eliminar al amigo
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón para volver al perfil
                FloatingActionButton(
                    onClick = { navController.navigate("profile") },
                    containerColor = Color(0xFF2196F3), // Color azul
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver al perfil"
                    )
                }

                // Botón para añadir amigo
                FloatingActionButton(
                    onClick = { navController.navigate("addfriend") },
                    containerColor = Color(0xFF4CAF50), // Color verde
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Añadir amigo"
                    )
                }
            }
        }
    ) { innerPadding ->
        // Resto del contenido de la pantalla
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Mostrar el identificador del usuario (email o UID)
            OutlinedTextField(
                value = userIdentifier,
                onValueChange = {},
                label = { Text(if (isGoogleUser) "MI EMAIL" else "MI ID") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Verificar si hay amigos en la lista
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (friends.isEmpty()) {
                    Text("No tienes amigos aún.", color = Color.Black)
                } else {
                    friends.forEach { friendId ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            // Información del amigo
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4CAF50)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Friend Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(verticalArrangement = Arrangement.Center) {
                                    // Mostrar el nombre del amigo si está disponible
                                    Text(
                                        text = friendNames[friendId] ?: "Cargando...",
                                        fontSize = 18.sp,
                                        color = Color.Black
                                    )
                                }
                            }

                            // Ícono de basura para eliminar
                            IconButton(
                                onClick = {
                                    // Mostrar el diálogo de confirmación
                                    friendIdToDelete = friendId
                                    showDeleteDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar amigo",
                                    tint = Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

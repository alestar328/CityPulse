package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.FriendsViewModel
import com.app.citypulse.data.NavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    navController: NavController,
    FriendsviewModel: FriendsViewModel,
    AuthviewModel: AuthViewModel
) {
    var friendUid by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) } // Mensaje de éxito
    val currentUserUid = AuthviewModel.getCurrentUserUid()
    val friendsList by FriendsviewModel.friends.collectAsState(initial = emptyList()) // Obtener amigos del ViewModel

    fun addFriend() {
        if (friendUid.isBlank()) {
            errorMessage = "El UID no puede estar vacío"
            successMessage = null
            return
        }
        if (friendUid == currentUserUid) {
            errorMessage = "No puedes agregarte a ti mismo"
            successMessage = null
            return
        }
        if (friendsList.contains(friendUid)) {
            errorMessage = "Este amigo ya está en la lista"
            successMessage = null
            return
        }

        FriendsviewModel.addFriend(friendUid) // Llamamos a una función en el ViewModel
        successMessage = "Amigo añadido con éxito" // Mensaje de éxito
        errorMessage = null
        friendUid = "" // Limpiar campo
    }

    // Barra de navegación inferior y botón flotante
    val navItemList = listOf(
        NavItem("Perfil", Icons.Filled.Person, 0),
        NavItem("Mapa", Icons.Filled.LocationOn, 0), // Ejemplo con 3 notificaciones en Mapa
        NavItem("Config", Icons.Filled.Settings, 0) // Ejemplo con 1 notificación en Config
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Añadir Amigo") })
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(navItem.label.lowercase()) // Usar el label como ruta
                        },
                        icon = {
                            if (navItem.badgeCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = Color.Red,
                                            contentColor = Color.White
                                        ) {
                                            Text(text = navItem.badgeCount.toString())
                                        }
                                    }
                                ) {
                                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                                }
                            } else {
                                Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                            }
                        },
                        label = { Text(navItem.label) }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addfriend") },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Añadir amigo")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = friendUid,
                onValueChange = { friendUid = it },
                label = { Text("UID del amigo") },
                isError = errorMessage != null,
                modifier = Modifier.fillMaxWidth()
            )
            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }
            successMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp) // Mostrar mensaje de éxito
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { addFriend() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar")
            }
        }
    }
}

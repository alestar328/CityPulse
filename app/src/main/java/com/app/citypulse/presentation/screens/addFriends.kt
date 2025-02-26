package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    navController: NavController,
    friendsViewModel: FriendsViewModel,
    authViewModel: AuthViewModel
) {
    var friendUid by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Obtener la lista de amigos
    val friendsList by friendsViewModel.friends.collectAsState(initial = emptyList())

    val navItemList = listOf(
        NavItem("Perfil", Icons.Filled.Person, 0),
        NavItem("Mapa", Icons.Filled.LocationOn, 0),
        NavItem("Config", Icons.Filled.Settings, 0)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Añadir Amigo") }) },
        bottomBar = {
            NavigationBar(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(navItem.label.lowercase())
                        },
                        icon = {
                            if (navItem.badgeCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = Color.Red, contentColor = Color.White) {
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
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
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

            errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp) }
            successMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp) }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Llamamos a addFriend con los parámetros necesarios
                    if (friendUid.isNotBlank()) {
                        friendsViewModel.addFriend(
                            friendUid = friendUid,
                            friendsList = friendsList,
                            onSuccess = {
                                successMessage = "Amigo agregado correctamente"
                                errorMessage = null
                            },
                            onError = {
                                errorMessage = it
                                successMessage = null
                            }
                        )
                    } else {
                        errorMessage = "Por favor ingresa un UID válido."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar")
            }
        }
    }
}

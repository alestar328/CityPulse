package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.*
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    navController: NavController,
    friendsViewModel: FriendsViewModel,
    authViewModel: AuthViewModel,
    innerPadding: PaddingValues
) {
    var friendUid by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) } // Mensaje de éxito
    val friendsList by friendsViewModel.friends.collectAsState(initial = emptyList())


    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Añadir Amigo") })
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

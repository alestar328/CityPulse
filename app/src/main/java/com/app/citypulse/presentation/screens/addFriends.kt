package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var successMessage by remember { mutableStateOf<String?>(null) }

    val currentUserUid = authViewModel.getCurrentUserUid()
    val friendsList by friendsViewModel.friends.collectAsState(initial = emptyList())

    fun addFriend() {
        when {
            friendUid.isBlank() -> {
                errorMessage = "El UID no puede estar vacío"
                successMessage = null
            }
            friendUid == currentUserUid -> {
                errorMessage = "No puedes agregarte a ti mismo"
                successMessage = null
            }
            friendsList.contains(friendUid) -> {
                errorMessage = "Este amigo ya está en la lista"
                successMessage = null
            }
            else -> {
                friendsViewModel.addFriend(friendUid)
                successMessage = "Amigo añadido con éxito"
                errorMessage = null
                friendUid = ""
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding),
        topBar = {
            TopAppBar(title = { Text("Añadir Amigo") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                Text(text = it, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
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
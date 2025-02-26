package com.app.citypulse.presentation.screens

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
import com.app.citypulse.navigation.NavItem
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.data.dataUsers.UserItem

@Composable
fun FriendsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel,
    currentUser: UserItem
) {
    val navitemList = listOf(
        NavItem("Perfil", Icons.Default.Person, 0),
        NavItem("Mapa", Icons.Default.LocationOn, 0),
        NavItem("Config", Icons.Default.Settings, 0)
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    val userId = viewModel.getCurrentUserUid()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth().background(Color.White)
            ) {
                navitemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            when (index) {
                                0 -> navController.navigate("profile")
                                1 -> navController.navigate("main_screen")
                                2 -> navController.navigate("settings")
                            }
                        },
                        icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.title) },
                        label = { Text(text = navItem.title) }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("addfriend")},
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(bottom = 16.dp)
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
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = userId,
                onValueChange = {},
                label = { Text("MI ID") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentUser.friends.forEach { friendId ->
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
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
                            Text(text = friendId, fontSize = 18.sp, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
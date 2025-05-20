package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewEventSection(
    placeName: String,
    avatarUrl: String?,
    userName: String,
    onBack: () -> Unit,
    onPublish: (rating: Int, reviewText: String) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(placeName, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2), // ajusta al color que uses
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Usuario + subtítulo
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (avatarUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar por defecto",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Avatar usuario",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(text = userName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Este contenido será público",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Estrellas editables
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { idx ->
                    val filled = idx < rating
                    Icon(
                        imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (filled) Color(0xFFFFD700) else Color.Gray,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { rating = idx + 1 }
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Campo de texto para la reseña
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = {
                    Text(
                        "Comparte detalles de tu experiencia en este lugar",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                maxLines = 5
            )

            Spacer(Modifier.weight(1f))

            // Botón Publicar al pie
            Button(
                onClick = { onPublish(rating, reviewText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Publicar", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    ReviewEventSection(
        placeName = "Braseria Sokystwo",
        avatarUrl = null,
        userName = "alejandro ormeño",
        onBack = {},
        onPublish = { rating, text -> /* ... */ }
    )
}
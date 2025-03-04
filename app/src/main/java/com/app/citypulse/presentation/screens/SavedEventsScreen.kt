package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SavedEventsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 🔹 Asegura que el contenido no quede oculto
        ) {
            // 🔹 Lista deslizable donde se mostrarán los eventos
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // 🔹 Ocupa todo el espacio disponible menos el del botón
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Aquí aparecerán los eventos guardados",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            // 🔹 Botón de "Volver", siempre visible en la parte inferior
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(horizontal = 16.dp, vertical = 32.dp) // 🔥 Aumentamos el padding inferior

            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}
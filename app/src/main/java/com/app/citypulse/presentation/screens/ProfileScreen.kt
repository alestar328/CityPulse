package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.presentation.components.ActionBox
import com.app.citypulse.presentation.components.ButtonBar
import com.app.citypulse.presentation.components.ProfileHeader

@Composable
fun ProfileScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxSize()
            .background(Color(0xFF1976D2))
            .padding(horizontal = 16.dp), // Margen lateral
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado entre botones
        ) {
            ButtonBar("Mis Amigos", backgroundColor = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, // Espaciado entre ActionBoxes
                modifier = Modifier.fillMaxWidth()
            ) {
                ActionBox(icon = Icons.Default.Check,"Eventos asistidos")

                ActionBox(icon = Icons.Default.Favorite,"Eventos guardados")

            }
            Spacer(modifier = Modifier.height(8.dp))
            ButtonBar("Mis Descuentos", backgroundColor = Color.White)
            ButtonBar("Cambiar Contraseña", backgroundColor = Color.White)
            ButtonBar("Cerrar Sesión", backgroundColor = Color.White)
            ButtonBar("Borrar Cuenta", backgroundColor = Color.Red)
        }

    }
}

@Preview
@Composable
fun ProfileScreenPreview(){
    ProfileScreen()
}

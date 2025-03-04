package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.presentation.components.SettingButton
import androidx.compose.foundation.layout.*


@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF9C27B0))
            .padding(horizontal = 16.dp)
            .padding(innerPadding),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Settings",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Sección Cuenta
        Text(text = "Cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Person, text = "Editar perfil")
            SettingButton(icon = Icons.Default.Notifications, text = "Modo de visualización")
            SettingButton(icon = Icons.Default.Lock, text = "Contraseña")
            SettingButton(
                icon = Icons.Default.Language,
                text = "Cambiar idioma",
                onClick = { navController.navigate("language_screen") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Ayuda
        Text(text = "Ayuda", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Info, text = "Información")
            SettingButton(icon = Icons.Default.Email, text = "Contactar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Cuenta (Eliminar cuenta)
        Text(text = "Cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Delete, text = "Eliminar cuenta")
        }
    }
}

@Composable
fun SettingSection(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
fun SettingButton(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Esto hace que el botón sea interactivo
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 18.sp, color = Color.Black)
    }
}

/*
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(

    )
}
*/

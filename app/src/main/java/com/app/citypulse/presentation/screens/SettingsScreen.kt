package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.presentation.components.SettingButton

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF9C27B0))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Settings",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)

        )
        Text(text = "Cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Person, text = "Editar perfil")
            SettingButton(icon = Icons.Default.Notifications, text = "Modo de visualización")
            SettingButton(icon = Icons.Default.Lock, text = "Contraseña")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Ayuda", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Info, text = "Información")
            SettingButton(icon = Icons.Default.Email, text = "Contactar")

        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Delete, text = "Eliminar cuenta")
        }
    }
}
@Composable
fun SettingSection(
    content: @Composable ColumnScope.() -> Unit) {
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
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
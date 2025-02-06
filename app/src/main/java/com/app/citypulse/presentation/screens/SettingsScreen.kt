package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.app.citypulse.presentation.components.SettingButton

@Composable
fun SettingsScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxSize()
            .background(Color(0xFF9C27B0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings Screen",
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        SettingButton(icon = Icons.Default.Person, text = "Edit Profile")
        SettingButton(icon = Icons.Default.Info, text = "Security")
        SettingButton(icon = Icons.Default.Notifications, text = "Notifications")
        SettingButton(icon = Icons.Default.Lock, text = "Privacy")
    }
}

@Preview
@Composable
fun SettingsScreenPreview(){
    SettingsScreen()
}
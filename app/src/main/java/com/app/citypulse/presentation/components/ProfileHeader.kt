package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Nombre Apellido", fontSize = 20.sp, color = Color.Black)
            Text(text = "Nombre Usuario", fontSize = 16.sp, color = Color.LightGray)

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Tipo Usuario", fontSize = 14.sp, color = Color(0xFFBBDEFB))


        }
    }
}

@Preview
@Composable
fun ProfileHeaderPreview() {
    ProfileHeader()
}
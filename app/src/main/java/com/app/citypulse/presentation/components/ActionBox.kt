package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionBox(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(165.dp)
            .height(110.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Icon(imageVector = icon, contentDescription = "Action Icon", tint = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1E1E))
        }
    }
}

@Composable
@Preview
fun ActionBoxPreview() {
    ActionBox(icon = Icons.Default.Star, title = "Astro-psychological report")
}

package com.app.citypulse.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
@Composable
fun CardValueStars(
    avatarUrl: String,
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Valorar y escribir una reseña",
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Subtítulo
        Text(
            text = "Comparte tu experiencia para ayudar a otros usuarios",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Avatar + estrellas
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (avatarUrl.isBlank()) {
                // Icono por defecto
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
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                // Avatar circular real
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar usuario",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            // 5 estrellas
            repeat(5) { index ->
                val filled = index < rating
                Icon(
                    imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    tint = if (filled) Color(0xFFFFD700) else Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onRatingChanged(index + 1) }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Preview
@Composable
fun CardValueStarsPreview() {
    var rating by remember { mutableStateOf(0) }
    CardValueStars(
        avatarUrl = "https://i.pravatar.cc/300",
        rating = rating,
        onRatingChanged = { rating = it }
    )
}
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
fun CardReviewEvent(
    avatarUrl: String,
    rating: Int,
    userName: String,
    userType: String,
    commentText: String,
    isCurrentUser: Boolean,
    isEventOwner: Boolean,
    onRatingChanged: (Int) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Título
        Text(text = userName, fontSize = 16.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(4.dp))
        // Subtítulo
        Text(text = userType, fontSize = 14.sp, color = Color.Gray)

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

        Text(
            text = commentText,
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(12.dp))
        // Botones condicionales
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            when {
                isCurrentUser -> {
                    Text(
                        text = "Editar",
                        modifier = Modifier.clickable(onClick = onEdit),
                        color = Color.Blue,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Borrar",
                        modifier = Modifier.clickable(onClick = onDelete),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                isEventOwner -> {
                    Text(
                        text = "Revisar",
                        modifier = Modifier.clickable(onClick = onReview),
                        color = Color.Blue,
                        fontSize = 14.sp
                    )
                }

                else -> {} // No se muestran botones
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}
@Preview(showBackground = true)
@Composable
fun CardReviewEventPreview() {
    var rating by remember { mutableStateOf(4) }

    CardReviewEvent(
        avatarUrl = "https://i.pravatar.cc/300",
        rating = rating,
        userName = "Tiffany Cachutt",
        userType = "Local Guide • 106 reseñas",
        commentText = "Es un bar pequeño, el *sabor* de la comida es auténtico venezolano y son muy majos, pero un poco lenta la atención.",
        isCurrentUser = false,
        isEventOwner = true,
        onRatingChanged = { rating = it },
        onEdit = { /* mock edit */ },
        onDelete = { /* mock delete */ },
        onReview = { /* mock review */ }
    )
}
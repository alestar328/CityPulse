package com.app.citypulse.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.app.citypulse.presentation.ui.theme.TurkBlue


@Composable
fun PhotoContainer(
    url: String?,          // Imagen guardada en Firestore (o nula)
    localUri: Uri?,        // Imagen local pendiente
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier

    ){
    Box(
        modifier = Modifier
            .size(width = 115.dp, height = 170.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .clickable{ onClick() }
    ) {
        when {
            localUri != null -> {
                Image(
                    painter = rememberAsyncImagePainter(model = localUri),
                    contentDescription = "Imagen local",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            !url.isNullOrEmpty() -> {
                Image(
                    painter = rememberAsyncImagePainter(model = url),
                    contentDescription = "Imagen Firestore",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                // Placeholder
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add photo",
                    tint = TurkBlue,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp)
                )
            }
        }
        // Botón de eliminar (solo si hay imagen)
        if (localUri != null || !url.isNullOrEmpty()) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = (-4).dp, y = 4.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
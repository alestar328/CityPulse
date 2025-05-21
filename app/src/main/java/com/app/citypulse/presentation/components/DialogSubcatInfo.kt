package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.app.citypulse.data.dataUsers.SubcatItem



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSubcatInfo(
    subcat: SubcatItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Información de Subcategoría",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Nombre
                Text(
                    text = "Nombre:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = subcat.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Categoría
                Text(
                    text = "Categoría:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = subcat.category.displayName ?: subcat.category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Descripción
                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = subcat.description.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Imagen
                Text(
                    text = "Imagen asociada:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(170.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    subcat.image?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "Imagen de ${subcat.name}",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Text(
                        text = "No hay imagen",
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Blue
                        )
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}

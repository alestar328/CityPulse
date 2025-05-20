package com.app.citypulse.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterSubcat(
    title: String,
    subcats: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Tomamos hasta 9 elementos y los agrupamos en filas de 3
        subcats.take(9)
            .chunked(3)
            .forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    rowItems.forEach { name ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            tonalElevation = 0.dp,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable { /* TODO: callback de selección */ }
                                .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                                .background(Color.White),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    // Si la última fila tiene menos de 3, rellenamos el espacio vacío
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
    }
}

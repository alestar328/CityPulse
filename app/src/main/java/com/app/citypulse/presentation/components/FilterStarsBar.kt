package com.app.citypulse.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun FilterStarsBar(
    modifier: Modifier = Modifier,
    options: List<Float> = listOf(3.5f, 4.0f, 4.5f),
    onSelectionChange: (Float?) -> Unit
) {
    var selected by remember { mutableStateOf<Float?>(null) }
    Column(modifier = modifier) {
        Text(
            text = "Calificación",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFBDBDBD)),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Row(Modifier.fillMaxSize()) {
                // construimos la lista incluyendo la opción "Cualquiera"
                val items: List<Float?> = listOf(null) + options
                items.forEachIndexed { index, value ->
                    val isSelected = selected == value
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(
                                when (index) {
                                    0 -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                                    items.lastIndex -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                                    else -> RectangleShape
                                }
                            )
                            .background(if (isSelected) Color(0xFF1976D2) else Color.Transparent)
                            .clickable {
                                selected = value
                                onSelectionChange(value)
                            }
                    ) {
                        if (value == null) {
                            Text(
                                text = "Cualquiera",
                                color = if (isSelected) Color.White else Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = String.format(Locale.getDefault(), "%.1f", value),
                                    color = if (isSelected) Color.White else Color.Black,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    if (index < items.lastIndex) {
                        Divider(
                            color = Color(0xFFBDBDBD),
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                    }
                }
            }
        }
    }
}
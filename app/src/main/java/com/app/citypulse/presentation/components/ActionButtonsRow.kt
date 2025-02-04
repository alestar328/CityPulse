package com.app.citypulse.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtonsRow(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly  // 🔹 Espaciado entre botones
    ) {
        ActionButton(
            text = "Asistiré",
            icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "Evento")},
            onClick = {}
        )
        ActionButton(
            text = "Entradas",
            icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Entradas")},
            onClick = {}

        )
        ActionButton(
            text = "Compartir",
            icon = { Icon(imageVector = Icons.Default.Share, contentDescription = "Compartir")},
            onClick = {}

        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActionButtonsRowPreview() {
    ActionButtonsRow()
}
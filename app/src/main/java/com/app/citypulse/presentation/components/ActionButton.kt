package com.app.citypulse.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFFE0E0E0),
    contentColor:    Color = Color.Black
){
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .height(35.dp)
            .widthIn(min = 70.dp, max = 170.dp), // 🔹 Tamaño mínimo y máximo controlado
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = backgroundColor,
            contentColor   = contentColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp), // 🔹 Separación entre ícono y texto

        ){
            icon?.invoke()
            Text(
                text = text,
                fontSize = 10.sp, // 🔹 Tamaño de texto más pequeño
                maxLines = 1, //  Evita que el texto salte a otra linea
                softWrap = false,
                color = contentColor

            )
        }
    }
}
@Preview
@Composable
fun ActionButtonPreview(){
    ActionButton(
        text = "Asistiré",
        icon = {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Evento")
        },
        onClick = {}
    )
}

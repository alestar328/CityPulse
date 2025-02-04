package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.citypulse.R

@Composable
fun GastroIcon(    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = Color.Gray, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp)
            .size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.gastroicon),
            contentDescription = "Fiesta",
            tint = Color.Black,
            modifier = Modifier.size(40.dp)
        )

    }

}

@Preview
@Composable
fun GastroIconPreview() {
    GastroIcon()
}

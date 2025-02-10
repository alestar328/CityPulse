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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.citypulse.R
import com.app.citypulse.presentation.screens.ui.theme.TurkBlue

@Composable
fun GastroIcon(    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(10.dp, shape = RoundedCornerShape(24.dp), ambientColor = Color.Black, spotColor = Color.Black.copy(alpha = 0.3f))
            .background(color = TurkBlue, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp)
            .size(45.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.gastroicon),
            contentDescription = "Fiesta",
            tint = Color.White,
            modifier = Modifier.size(35.dp)
        )

    }

}

@Preview
@Composable
fun GastroIconPreview() {
    GastroIcon()
}

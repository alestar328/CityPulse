package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.app.citypulse.presentation.ui.theme.TurkBlue
import com.app.citypulse.presentation.ui.theme.YellowLight

@Composable
fun CulturalIcon(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) YellowLight else Color.Gray
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 10.dp)
            .size(40.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.culturaicon),
            contentDescription = "Fiesta",
            tint = Color.Black,
            modifier = Modifier.size(25.dp)
        )

    }

}

@Preview
@Composable
fun CulturalIconPreview() {
    CulturalIcon(
        onClick ={},
        isSelected = false
    )
}

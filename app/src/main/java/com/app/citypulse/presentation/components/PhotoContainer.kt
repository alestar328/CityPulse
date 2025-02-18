package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.citypulse.presentation.screens.ui.theme.TurkBlue


@Composable
fun PhotoContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .size(width = 115.dp, height = 170.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ){
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

@Preview
@Composable
fun PhotoContainerPreview(){
    PhotoContainer(onClick = {})
}

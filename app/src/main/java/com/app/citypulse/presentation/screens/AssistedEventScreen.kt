package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.presentation.components.EventCard

@Composable
fun AssistedEventScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding( 16.dp)
    ) {
        Column {
            EventCard()
            EventCard()
            EventCard()
            EventCard()

        }
    }

}

@Preview
@Composable
fun AssistedEventScreenPreview(){
    AssistedEventScreen(navController = NavController(LocalContext.current))
}

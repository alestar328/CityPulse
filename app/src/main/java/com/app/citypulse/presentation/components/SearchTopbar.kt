package com.app.citypulse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.citypulse.data.enums.TipoCategoria

@Composable
fun SearchTopbar(
    modifier: Modifier = Modifier,
    selectedCategory: TipoCategoria,
    onCategorySelected: (TipoCategoria) -> Unit
){

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        SearcherBar()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GastroIcon(
                isSelected = (
                        selectedCategory == TipoCategoria.GASTRONOMICO),
                onClick = {
                    println("GastroIcon clicado. Categoría actual: $selectedCategory")
                    onCategorySelected(
                        if (selectedCategory == TipoCategoria.GASTRONOMICO) TipoCategoria.NONE
                        else TipoCategoria.GASTRONOMICO
                    )
                }
            )
            PartyIcon(
                isSelected = (
                        selectedCategory == TipoCategoria.FIESTA),
                onClick = {
                    println("PartyIcon clicado. Categoría actual: $selectedCategory")

                    onCategorySelected(
                        if (selectedCategory == TipoCategoria.FIESTA) TipoCategoria.NONE
                        else TipoCategoria.FIESTA
                    )
                }
            )
            CulturalIcon(
                isSelected = (
                        selectedCategory == TipoCategoria.CULTURAL),
                onClick = {
                    println("CulturalIcon clicado. Categoría actual: $selectedCategory")

                    onCategorySelected(
                        if (selectedCategory == TipoCategoria.CULTURAL) TipoCategoria.NONE
                        else TipoCategoria.CULTURAL
                    )
                }
            )
        }

    }

}



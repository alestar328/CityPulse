package com.app.citypulse.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.data.model.EventFilters
import com.app.citypulse.data.model.EventUiModel

@Composable
fun EventListView(
    events: List<EventUiModel>,
    filters: EventFilters,  // tu data class con categorías, subcats y rating
    navController: NavController,
    onSaved: (EventUiModel) -> Unit,
    onAssisted: (EventUiModel) -> Unit
) {
    // 1) Aplica aquí tu lógica de filtrado:
    val filtered = remember(events, filters) {
        events
            .filter { filters.category == TipoCategoria.NONE || it.categoria == filters.category }
            .filter { filters.subcategories.isEmpty() || filters.subcategories.contains(it.subcategoria) }
            .filter { filters.rating == null || it.valoracion >= filters.rating }
    }

    // 2) Renderiza la LazyColumn:
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filtered, key = { it.id }) { event ->
            EventOrganizerMapCard(
                nombre       = event.nombre,
                nomOrg       = event.nomOrg,
                categoria    = event.categoria.displayName ?: event.categoria.name,
                subcategoria = event.subcategoria,
                lugar        = event.lugar,
                fechaInicio  = event.fechaInicio,
                fechaFin     = event.fechaFin,
                precio       = event.precio,
                aforo        = event.aforo,
                eventId      = event.id,
                navController= navController,
                images       = event.galleryPictureUrls,
                onSaved      = { onSaved(event) },
                onAssisted   = { onAssisted(event) }
            )
        }
    }
}
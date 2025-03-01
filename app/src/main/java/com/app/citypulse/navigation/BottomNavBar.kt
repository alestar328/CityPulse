package com.app.citypulse.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.citypulse.presentation.screens.ui.theme.TurkBlue
import com.app.citypulse.presentation.screens.ui.theme.YellowLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(
    items: List<NavItem>,
    currentRoute: String?,
    onClick: (NavItem) -> Unit,
) {
    NavigationBar(
        containerColor = TurkBlue,
        modifier = Modifier.height(50.dp) // Altura reducida para un navbar más delgado
    ) {
        items.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = currentRoute == navigationItem.route,
                onClick = { onClick(navigationItem) },
                icon = {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy((-7).dp)  // Ajusta este espacio a tu gusto
                    ) {
                        BadgedBox(
                            badge = {
                                if (navigationItem.badgeCount != null) {
                                    Badge {
                                        Text(text = navigationItem.badgeCount.toString())
                                    }
                                } else if (navigationItem.hasBadgeDot) {
                                    Badge()
                                }
                            }) {
                            Icon(
                                imageVector = if (currentRoute == navigationItem.route) {
                                    navigationItem.selectedIcon
                                } else {
                                    navigationItem.unSelectedIcon
                                },
                                contentDescription = navigationItem.title,
                            )
                        }
                        Text(
                            text = navigationItem.title,
                            fontSize = 12.sp
                        )
                    }
                },
                // Dejamos label = {} vacío o en null para no duplicar el texto
                label = {},
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent, // quita la sombra del seleccionado
                    selectedIconColor = YellowLight,     // color ícono seleccionado
                    unselectedIconColor = Color.White,   // color ícono sin seleccionar
                    selectedTextColor = YellowLight,     // color texto seleccionado
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}
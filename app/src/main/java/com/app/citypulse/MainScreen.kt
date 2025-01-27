package com.app.citypulse

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app.citypulse.data.NavItem
import com.app.citypulse.presentation.screens.AddEventScreen
import com.app.citypulse.presentation.screens.ContactsScreen
import com.app.citypulse.presentation.screens.SettingsScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    //Creamos una lista que representa los botones del Navbar
    val navitemList = listOf(
        NavItem("Contacts", Icons.Default.Person, 5),
        NavItem("Add", Icons.Default.Add, 0),
        NavItem("Settings", Icons.Default.Settings, 0)

    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    //Este Scaffold ha sido traido desde el MainActivity
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navitemList.forEachIndexed{ index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            BadgedBox(badge = {
                                //Condicional para que no muestre "0" en los demas iconos
                                if(navItem.badgeCount>0)
                                Badge(){
                                    Text(text = navItem.badgeCount.toString())
                                }
                            }) {
                                Icon(imageVector = navItem.icon, contentDescription = "Icon")
                            }
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }

            }

        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {

    //Esta es la logica de seleccion y cambio de pantalla
    when(selectedIndex){
        0 -> ContactsScreen()
        1 -> AddEventScreen()
        2 -> SettingsScreen()
    }

}
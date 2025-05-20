package com.app.citypulse.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.citypulse.presentation.components.SettingButton
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.citypulse.data.enums.AccountType
import com.app.citypulse.presentation.components.DialogSubcats
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    settingsViewModel: SettingsViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val showDialog by settingsViewModel.showSubcatDialog.collectAsState()
    val userTypeStr by authViewModel.userType.collectAsState()
    val context = LocalContext.current
    val errorMsg  by settingsViewModel.errorMessage.collectAsState()
    val successMsg      by settingsViewModel.successMessage.collectAsState()

    val accountType = remember(userTypeStr) {
        try {
            AccountType.valueOf(userTypeStr?.uppercase() ?: "")
        } catch (_: Exception) {
            AccountType.PERSON
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF9C27B0))
            .padding(horizontal = 16.dp)
            .padding(innerPadding),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Settings",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Sección Cuenta
        Text(text = "Cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Person, text = "Editar perfil")
            SettingButton(icon = Icons.Default.Notifications, text = "Modo de visualización")
            SettingButton(icon = Icons.Default.Lock, text = "Contraseña")
            SettingButton(
                icon = Icons.Default.Star,
                text = "Cambiar idioma",
                onClick = { navController.navigate("language_screen") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Ayuda
        Text(text = "Ayuda", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Info, text = "Información")
            SettingButton(
                icon = Icons.Default.Add,
                text = "Añadir Subcategoría",
                onClick = {
                    // Solo COMPANY u ONG pueden abrir
                    if (accountType == AccountType.COMPANY || accountType == AccountType.ONG) {
                        settingsViewModel.openSubcatDialog()
                    } else {
                        Toast.makeText(context, "Acción prohibida", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Cuenta (Eliminar cuenta)
        Text(text = "Cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        SettingSection {
            SettingButton(icon = Icons.Default.Delete, text = "Eliminar cuenta")
        }
    }
    DialogSubcats(
        show = showDialog,
        errorMessage= errorMsg,
        successMessage = successMsg,
        onDismiss   = { settingsViewModel.closeSubcatDialog() },
        onAdd       = { name, cat, imageUri,description  -> settingsViewModel.addSubcategory(name, cat, imageUri, description) }
    )
}

@Composable
fun SettingSection(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
fun SettingButton(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Esto hace que el botón sea interactivo
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 18.sp, color = Color.Black)
    }
}


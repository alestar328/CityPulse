package com.app.citypulse.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.app.citypulse.preferences.LocaleHelper

@Composable
fun LanguageSelector(context: Context, onLanguageSelected: (String) -> Unit) {
    val languages = listOf("Español" to "es", "English" to "en")
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by rememberSaveable { mutableStateOf("Español") }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedLanguage)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            languages.forEach { (label, code) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        selectedLanguage = label
                        expanded = false
                        LocaleHelper.setLocale(context, code)
                        onLanguageSelected(code)
                    }
                )
            }
        }
    }
}

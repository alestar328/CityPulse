package com.app.citypulse.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, label: String, onValueChange: (String) -> Unit, enabled: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.LightGray,
            disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray
        ),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 240) onValueChange(it)
        },
        label = { Text("Descripción", color = Color.White) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.LightGray,
            disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray
        ),
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            Text(
                text = "${value.length}/240",
                color = if (value.length in 200..240) Color.Green else Color.Red,
                fontSize = 12.sp
            )
        }
    )
}


@Composable
fun NumericTextField(value: String, label: String, onValueChange: (String) -> Unit, isDecimal: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            // Permitir solo números y un punto decimal si es necesario
            val filteredInput = if (isDecimal) {
                input.filterIndexed { index, c -> c.isDigit() || (c == '.' && index != 0 && !value.contains('.')) }
            } else {
                input.filter { it.isDigit() }
            }
            onValueChange(filteredInput)
        },
        label = { Text(label, color = Color.White) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isDecimal) KeyboardType.Number else KeyboardType.Number
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.LightGray,
            disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray
        ),
        shape = RoundedCornerShape(12.dp)
    )
}
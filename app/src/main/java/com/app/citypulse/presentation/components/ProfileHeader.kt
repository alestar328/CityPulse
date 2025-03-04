package com.app.citypulse.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.presentation.ui.theme.TurkBlue


@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    user: UserItem,
    pendingProfileUri: Uri?, // La URI local si el usuario eligió algo nuevo
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
                .clickable { onClick() },
            contentAlignment = Alignment.Center,

            ) {
            when {
                // 1) Si el usuario eligió una foto nueva (pendingProfileUri != null)
                pendingProfileUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(pendingProfileUri),
                        contentDescription = "Foto de perfil local",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                // 2) Si en Firestore ya hay una foto de perfil
                !user.profilePictureUrl.isNullOrEmpty() -> {
                    Image(
                        painter = rememberAsyncImagePainter(user.profilePictureUrl),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                // 3) No hay nada: ícono por defecto
                else -> {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add photo",
                        tint = TurkBlue,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(50.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${user.name} ${user.surname}",
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = user.email,
                fontSize = 16.sp,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.userType.name,
                fontSize = 14.sp,
                color = Color(0xFFBBDEFB)
            )
        }
    }
}

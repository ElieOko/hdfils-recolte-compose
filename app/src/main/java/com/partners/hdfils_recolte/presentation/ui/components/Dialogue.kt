package com.partners.hdfils_recolte.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.partners.hdfils_recolte.R

@Composable
@Preview
fun MAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    dialogTitle: String = "Success",
    dialogText: String = "Magique",
    icon: ImageVector = Icons.Default.Notifications,
) {
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFF377946),
                Color(0xFF2B4B4F),
                Color(0xFF9575CD)
            )
        )
    }
    AlertDialog(
        icon = {
            Image(
                painter = painterResource(R.drawable.logo_gris),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape).size(140.dp).border(
                        BorderStroke(4.dp, rainbowColorsBrush),
                        CircleShape
                    )
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Fermer")
            }
        }
    )
}
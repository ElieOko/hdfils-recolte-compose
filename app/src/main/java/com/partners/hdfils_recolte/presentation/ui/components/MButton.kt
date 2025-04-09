package com.partners.hdfils_recolte.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MButtonIcon(){
    MButtonIcon(
        click = {},
        icon = {
            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
        }
    )
}

@Composable
fun MButtonIcon(
    modifier : Modifier = Modifier,
    click : () -> Unit ,
    backgroundColor : Color = Color.Black,
    enabled : Boolean = true,
    sizeBorder : Int = 12,
    label : String = "Button",
    space : Int = 10,
    icon : @Composable () -> Unit,
    textColor : Color = Color.Black
){
    Button(
        onClick = { click() },
        modifier =modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        enabled = enabled,
        shape = RoundedCornerShape(sizeBorder.dp)
    )
    {
        Row(content = {
            Text(text = label, color = textColor)
            Spacer(modifier = Modifier.width(space.dp))
            icon()
        })
    }
}
//0xDC43604F
@Composable
@Preview(showBackground = true)
fun PreviewMButtonIcon(){
    MButtonIcon(
        click = {},
        icon = {
            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
        }
    )
}
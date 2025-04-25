package com.partners.hdfils_recolte.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
fun TopBar(
    username: String = "elieoko",
    onclick : () -> Unit = {},
    onclickSignout : () -> Unit = {},
){
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
    Column(Modifier.padding(10.dp)) {
        Space(y=20)
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
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
            Column(Modifier.padding(10.dp)) {
                Label("Kin ezo bonga")
                Space(y=1)
                Label("@$username", modifier = Modifier.absoluteOffset(x=7.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onclickSignout) {
                    Icon(painterResource(R.drawable.signout), contentDescription = null, Modifier.size(30.dp), tint = Color.White)
                }
               IconButton(onClick = onclick) {
                   Icon(painterResource(R.drawable.transfert), contentDescription = null, Modifier.size(30.dp), tint = Color.White)
               }
            }

        }
    }

}

@Composable
@Preview
fun TopBarPreview(){
    TopBar()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    titleMain : String = "Immobilier",
    shadow : Int = 0,
    iconStart: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    iconEnd : ImageVector =  Icons.Filled.CheckCircle,
    isAction : Boolean = false,
    colorIcon : Color = Color.LightGray,
    sizeIcon : Int = 33,
    containerColor : Color = Color.White,
    clickStartAction : ()-> Unit = {},
    clickEndAction : ()-> Unit = {},
    paddingSurface : Int = 5,
    username: String ="elieoko"
){

    TopAppBar(
            title = {
                Column(Modifier.padding(5.dp)) {
                    Label("Welcome")
                    Label("@$username", modifier = Modifier.absoluteOffset(x=7.dp, y = (-10).dp))
                }
                    },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xDC3A57E7).copy(0.9f)),
            navigationIcon = {
                IconButton(onClick = { clickStartAction() }) {
                    Image(
                        painter = painterResource(R.drawable.luffy),
                        contentDescription = "",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                    )
                }
            },
            actions = {
                Row {
                    IconButton(onClick = {}) {
                        MIcon(Icons.Default.Notifications,)
                    }
                    IconButton(onClick = {}) {
                        MIcon(Icons.AutoMirrored.Filled.Send)
                    }
                }
            }
        )

}
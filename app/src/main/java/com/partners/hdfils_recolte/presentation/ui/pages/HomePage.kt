package com.partners.hdfils_recolte.presentation.ui.pages

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_recolte.presentation.ui.components.TopBar
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.presentation.ui.components.ImageCarousel
import com.partners.hdfils_recolte.presentation.ui.components.Label
import com.partners.hdfils_recolte.presentation.ui.components.MButtonIcon
import com.partners.hdfils_recolte.presentation.ui.components.ModernCarousel
import com.partners.hdfils_recolte.presentation.ui.components.Space

@Composable
fun HomePage(){
    HomePageBody()
}
val largeRadialGradient = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val biggerDimension = maxOf(size.height, size.width)
        return RadialGradientShader(
            colors = listOf(  Color(0xFF214A60),
                Color(0xFF1B5A8C),
                Color(0xFF2B4B4F),
                Color(0xFF166B6E)),
            center = size.center,
            radius = biggerDimension / 2f,
            colorStops = listOf(0f, 0.95f)
        )
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePageBody(){
    var stateTrash by remember { mutableStateOf(false) }
    Scaffold(containerColor =  Color(0xFF6E889D).copy(0.2f)) {
        Column(

        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth().padding(0.dp),
                shape = RoundedCornerShape(bottomEnd = 45.dp, bottomStart = 45.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xDC3A57E7).copy(0.7f)
                )) {
               TopBar()
                Column(Modifier.padding(1.dp).absoluteOffset(y = (-15).dp)) {
                    Column(Modifier.padding(20.dp)) {
                        Row {
                            Label("Identifiant     : ",)
                            Label("LondalaN1", color = Color.White.copy(
                                alpha = 0.5f
                            ))
                        }
                        Row {
                            Label("Avenue          : ",)
                            Label("Londala",color = Color.White.copy(
                                alpha = 0.5f
                            ))
                        }

                        Row {
                            Label("Commune    : ")
                            Label("Lingwala", color = Color.White.copy(
                                alpha = 0.5f
                            ))
                        }

                        Row {
                            Label("Ville                : ",)
                            Label("Kinshasa", color = Color.White.copy(
                                alpha = 0.5f
                            ))
                        }
                        Space(y = 9)
                        Row {
                            Text("Vidage de la poubelle", color = Color.White.copy(
                                alpha = 0.5f
                            ), fontSize = 16.sp)
                            Space(x = 8)
                            Icon(painterResource(R.drawable.trash), modifier = Modifier.size(23.dp), contentDescription = "", tint = Color.White)
                        }
                        Space(y = 4)

                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            MButtonIcon(
                                backgroundColor = Color.Red.copy(0.6f),
                                textColor = Color.White,
                                label = "Vidage",
                                click = {
                                    stateTrash = true
                                },
                                icon = {
                                    Icon(painterResource(R.drawable.arrow), modifier = Modifier.size(23.dp), contentDescription = null)
                                }
                            )
                            if (stateTrash){
                                MButtonIcon(
                                    textColor = Color.White,
                                    backgroundColor = Color.Green.copy(0.4f),
                                    label = "Valider",
                                    click = {
                                        stateTrash = !stateTrash
                                    },
                                    icon = {
                                        Icon(painterResource(R.drawable.check), modifier = Modifier.size(23.dp), contentDescription = null)
                                    }
                                )
                            }
                        }
                        Space(y = 2)
                    }
                }
            }
            Column(Modifier.verticalScroll(rememberScrollState())) {
               if(!stateTrash){
                   Space(y = 80)
               }
               if (stateTrash){
                   Card(Modifier.padding(10.dp),colors = CardDefaults.cardColors(
                       containerColor = Color.White
                   ), elevation = CardDefaults.cardElevation(5.dp)) {
                       Row(modifier = Modifier.fillMaxWidth().padding(5.dp),verticalAlignment = Alignment.CenterVertically) {
                           Icon(painterResource(R.drawable.top), contentDescription = "", modifier = Modifier.size(30.dp), tint = Color.Red.copy(alpha = 0.5f))
                           Spacer(Modifier.width(10.dp))
                           Column() {
                               Text("Ma poubelle est pleine")
                               Text("Depuis 25 Nov 2025")
                           }
                           Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Right) {

                               Box(modifier = Modifier.border(width = 10.dp, color = Color.Green.copy(
                                   alpha = 0.1F,
                               ),shape = RoundedCornerShape(52.dp)).scale(0.75f)){
                                   Icon(painterResource(R.drawable.run), modifier = Modifier.size(42.dp), contentDescription = "icon",tint = Color.Black)
                               }
                               Space(x = 4)
                               Column {
                                   Text("On arrive", Modifier.absoluteOffset(y = 12.dp))
                               }

                           }
                       }
                   }
               }
            }
            ModernCarousel()
        }
    }
}

@Composable
@Preview
fun HomePagePreview(){
    HomePageBody()
}
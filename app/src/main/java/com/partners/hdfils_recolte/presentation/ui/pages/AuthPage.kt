package com.partners.hdfils_recolte.presentation.ui.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_recolte.data.remote.ClientKtor
import com.partners.hdfils_recolte.data.remote.ResponseAPI
import com.partners.hdfils_recolte.domain.models.UserSerializable
import io.ktor.client.call.body
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.presentation.ui.components.Space
import com.partners.hdfils_recolte.presentation.ui.theme.AnimatedBackgroundShapes

@Composable
fun AuthPage(){
    AuthPageBody()
}

@Composable
fun AuthPageBody(){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var matricule by remember { mutableStateOf("") }
    var isAnimating by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(true) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF6C63FF), Color(0xFFF5F5F5)),
        startY = 0f,
        endY = 1000f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Ajouter les formes en arrière-plan
        AnimatedBackgroundShapes()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3E4EBD)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Space(y=5)
                    Image(
                        painterResource(R.drawable.logo_gris),
                        contentDescription = "",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop)
                    Space(y=10)
                    Text(
                        text = "Connexion",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Bongisa bisika ofandi elongo na biso",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = matricule,
                        onValueChange = { matricule = it },
                        label = { Text("Identifiant") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.house),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp),
                                tint = Color.White
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            cursorColor = Color.White,
                            focusedLeadingIconColor = Color.White,
                            unfocusedLeadingIconColor = Color.White
                        ),
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Space(y=20)
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isActive = false
                                delay(6000)
                                val response = ClientKtor().postData("user/login",UserSerializable(matricule))
                                val status = response.status.value
                                when(status){
                                    in 200..299 ->{
                                        isActive = true
                                        val res = response.body<ResponseAPI>()
                                        Toast.makeText(context,res.message,Toast.LENGTH_LONG).show()
                                    }
                                    in 500..599 ->{
                                        isActive = true
                                        Toast.makeText(context,"Erreur serveur",Toast.LENGTH_LONG).show()
                                    }
                                    in 400..499 ->{
                                        isActive = true
                                        val res = response.body<ResponseAPI>()
                                        Toast.makeText(context,res.message,Toast.LENGTH_LONG).show()
                                    }

                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  Color.Black,
//                            contentColor = Color(0xFF6C63FF),
                            disabledContentColor = Color(0xFF080624),
                            disabledContainerColor = Color(0xFF080624)
                        ),
                        enabled = isActive,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = if(isActive) "Se connecter" else "Chargement...", fontSize = 16.sp, color = Color.White)
                    }


                    Space(y=30)
//                    TextButton(onClick = { /* Naviguer vers l'écran d'inscription */ }) {
//                        Text(
//                            text = "Pas encore de compte ? Inscrivez-vous",
//                            color = Color.White,
//                            fontSize = 14.sp
//                        )
//                    }
                }
            }
        }
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top) {
            if(!isActive){
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    trackColor =Color(0xFF070524),
                )
            }
        }
    }
}

@Composable
@Preview
fun AuthPagePreview(){
    AuthPageBody()
}
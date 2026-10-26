package com.partners.hdfils_recolte.presentation.ui.pages

import android.annotation.SuppressLint
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.partners.hdfils_recolte.data.remote.ClientKtor
import com.partners.hdfils_recolte.data.remote.ResponseAPI
import io.ktor.client.call.body
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.data.remote.ResponseAPIGeneric
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.data.utils.EndPoint.clientAuth
import com.partners.hdfils_recolte.data.utils.isValidPhoneNumber
import com.partners.hdfils_recolte.domain.models.Address
import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.ClientAuth
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.presentation.ui.components.MAlertDialog
import com.partners.hdfils_recolte.presentation.ui.components.Space
import com.partners.hdfils_recolte.presentation.ui.theme.AnimatedBackgroundShapes

@Composable
fun AuthPage(navC: NavHostController, isConnected: Boolean) {
    AuthPageBody(navC,isConnected)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AuthPageBody(navC: NavHostController, isConnected: Boolean) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var telephone by remember { mutableStateOf("") }
    var avenue by remember { mutableStateOf("") }
    var isAnimating by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(true) }
    var msg by remember { mutableStateOf("") }
    var titleMsg by remember { mutableStateOf("Erreur") }
    var isShow by remember { mutableStateOf(false) }
    val maxLength = 10 // Longueur maximale
    val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF6C63FF), Color(0xFFF5F5F5)), startY = 0f, endY = 1000f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AnimatedBackgroundShapes()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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
                        value = telephone,
                        onValueChange = {
                            if (it.length <= maxLength) {
                                telephone = it
                            }
                             },
                        label = { Text("Téléphone") },
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
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = avenue,
                        onValueChange = { avenue = it },
                        label = { Text("Avenue") },
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
                            if(!isValidPhoneNumber(phoneNumber = telephone)) {
                                isShow = true
                                msg = "Ce numero est invalide en republique democratique du congo"
                            }
                            if(avenue.isEmpty()){
                                isShow = true
                                msg = "Le nom de l'avenue n'est pas renseigner"
                            }
                            if(isValidPhoneNumber(phoneNumber = telephone) && avenue.isNotEmpty() ){
                                if(isConnected){
                                    coroutineScope.launch {
                                        isActive = false
                                        delay(3000)
                                        val response = ClientKtor().postData(clientAuth,
                                            ClientAuth(telephone = telephone, avenue = avenue)
                                        )
                                        val status = response.status.value
                                        when(status){
                                            in 200..299 ->{
                                                isActive = true
                                                val res = response.body<ResponseAPI>()
                                                scope.launch {
                                                    StoreData(context).getDataClient.collect{
                                                        if(it != null){
                                                            if (it.telephone == telephone && it.address_client[0].avenue == avenue){
                                                                //
                                                            }
                                                            else{
                                                                StoreData(context).delete()
                                                            }
                                                        }
                                                    }
                                                }
                                                scope.launch {
                                                    StoreData(context).saveDataClientAuth(ClientAuth(telephone,avenue))
                                                    StoreData(context).saveDataClient(
                                                        res.client?.id?.let {
                                                            Client(
                                                                id = it,
                                                                nom = res.client.nom,
                                                                prenom = res.client.prenom,
                                                                telephone = res.client.telephone,
                                                                address_client = listOf<Address>(Address(
                                                                    commune_id =  res.client.address_client[0].commune_id,
                                                                    avenue = res.client.address_client[0].avenue,
                                                                    quartier = res.client.address_client[0].quartier,
                                                                    numero_parcelle = res.client.address_client[0].numero_parcelle
                                                                )),
                                                            )
                                                        }
                                                    )
                                                    navC.navigate(route = ScreenRoute.Home.name){
                                                        popUpTo(navC.graph.id){
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            }
                                            in 500..599 ->{
                                                isActive = true
                                                msg = "Erreur serveur réssayer plus tard nous resolvons ce problème"
                                                isShow = true
                                            }
                                            in 400..499 ->{
                                                isActive = true
                                                val res = response.body<ResponseAPIGeneric>()
                                                msg = res.message
                                                isShow = true
                                            }
                                        }
                                    }
                                }
                                else{
                                    msg = "Vous n'êtes pas connecté veuillez vérifier votre connexion !!!"
                                    titleMsg = "Problème de connexion"
                                    isShow = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  Color.Black,
                            disabledContentColor = Color(0xFF080624),
                            disabledContainerColor = Color(0xFF080624)
                        ),
                        enabled = isActive,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = if(isActive) "Se connecter" else "Chargement...", fontSize = 16.sp, color = Color.White)
                    }
                    if(isShow){
                        MAlertDialog(
                            dialogTitle = "Erreur",
                            dialogText =  msg,
                            onDismissRequest = {
                                isShow = false
                            }
                        )
                    }
                    Space(y=30)
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
//    AuthPageBody(navC, isConnected)
}
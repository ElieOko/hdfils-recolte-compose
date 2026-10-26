package com.partners.hdfils_recolte.presentation.ui.pages

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import com.partners.hdfils_recolte.presentation.ui.components.TopBar
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.data.remote.ClientKtor
import com.partners.hdfils_recolte.data.remote.ResponseAPI
import com.partners.hdfils_recolte.data.remote.ResponseAPIGeneric
import com.partners.hdfils_recolte.data.remote.ResponseAPITrash
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.data.utils.EndPoint.clientAuth
import com.partners.hdfils_recolte.data.utils.EndPoint.trashAction
import com.partners.hdfils_recolte.data.utils.EndPoint.trashClean
import com.partners.hdfils_recolte.domain.models.Address
import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.ClientAuth
import com.partners.hdfils_recolte.domain.models.Trash
import com.partners.hdfils_recolte.domain.models.TrashClean
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.presentation.ui.components.Circular
import com.partners.hdfils_recolte.presentation.ui.components.Label
import com.partners.hdfils_recolte.presentation.ui.components.MAlertDialog
import com.partners.hdfils_recolte.presentation.ui.components.MButtonIcon
import com.partners.hdfils_recolte.presentation.ui.components.ModernCarousel
import com.partners.hdfils_recolte.presentation.ui.components.Space
import io.ktor.client.call.body
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun HomePage(navC: NavHostController, isConnected: Boolean) {
    HomePageBody(navC,isConnected)
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun HomePageBody(navC: NavHostController, isConnected: Boolean) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var stateTrash by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(true) }
    var isShow by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }
    var titleMsg by remember { mutableStateOf("Erreur") }
    val clients = remember { mutableStateListOf<Client>() }
    val trashCleaner = remember { mutableStateListOf<TrashClean?>() }
    val coroutineScope = rememberCoroutineScope()
    var onclick : ()-> Unit = {isShow = false}

    Scaffold(containerColor =  Color(0xFF6E889D).copy(0.2f)) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth().padding(0.dp),
                shape = RoundedCornerShape(bottomEnd = 45.dp, bottomStart = 45.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xDC3A57E7).copy(0.7f)
                )) {
                    scope.launch {
                        StoreData(context).getDataTrash.collect{
                            if (it != null) {
                                trashCleaner.add(it)
                            }
                        }
                    }
                    scope.launch {
                        StoreData(context).getDataClient.collect { client ->
                            if (client != null) {
                                clients.add(client)
                            } // Ajoute le client à la liste
                        }
                    }
                    if (!clients.isEmpty()){
                        val username = (clients.toList()[0].prenom + clients.toList()[0].nom).lowercase(
                            Locale.getDefault()
                        )
                        TopBar(
                            username = username,
                            onclick = {
                                onclick = {
                                    isShow = false
                                }
                                try {
                                    if(isConnected){
                                        isActive = false
                                        coroutineScope.launch {
                                            delay(3000)
                                            val response = ClientKtor().getData("${trashAction}/${clients[0].id}")
                                            val status = response.status.value
                                            when(status){
                                                in 200..299 ->{
                                                    val res = response.body<ResponseAPITrash>()
                                                    msg = "Mises à jours effectuer avec succès"
                                                    titleMsg = "Success"
                                                    isActive = true
                                                    isShow = true
                                                    trashCleaner.add(TrashClean(
                                                        id = res.data.id,
                                                        state_trash_id = res.data.state_trash_id,
                                                        created_at = res.data.created_at,
                                                        client_id = res.data.client_id))
                                                    scope.launch {
                                                        StoreData(context).saveDataTrash(trashCleaner[0])
                                                    }
                                                }
                                                in 500..599 ->{
                                                    msg = "Erreur serveur réssayer plus tard nous resolvons ce problème"
                                                    isShow = true
                                                    isActive = true
                                                }

                                                in 400..499 ->{
                                                    val res = response.body<ResponseAPIGeneric>()
                                                    msg = res.message
                                                    titleMsg     = "Information"
                                                    isShow = true
                                                    isActive = true
                                                }

                                            }
                                        }
                                    }
                                    else{
                                        titleMsg = "Problème de connexion"
                                        msg = "Vous n'êtes pas connecté veuillez vérifier votre connexion !!!"
                                        isShow = true
                                    }
                                }
                                catch (e : Exception){
                                    Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
                                }
                            },
                            onclickSignout = {
                                isShow = true
                                titleMsg = "Information"
                                msg = "Voulez-vous vraiment vous déconnectez ??"
                                onclick = {
                                    isShow = false
                                    navC.navigate(route = ScreenRoute.Auth.name){
                                        popUpTo(navC.graph.id){
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        )
                    }
                Column(Modifier.padding(1.dp).absoluteOffset(y = (-15).dp)) {
                    if (!clients.isEmpty()){
                        Column(Modifier.padding(20.dp)) {
                            Space(y = 2)
                            Row(Modifier.fillMaxWidth()) {
                                Column {
                                    Label("Quartier", size = 16, fontWeight = FontWeight.Bold)
                                    Label(clients.toList()[0].address_client[0].quartier, color = Color.White.copy(
                                        alpha = 0.5f
                                    ))
                                }
                                Space(x=15)
                                Column {
                                    Label("Avenue", size = 16, fontWeight = FontWeight.Bold)
                                    Label(clients.toList()[0].address_client[0].avenue,color = Color.White.copy(
                                        alpha = 0.5f
                                    ))
                                }
                                Space(x=15)
                                Column {
                                    Label("Commune", size = 16, fontWeight = FontWeight.Bold)
                                    Label(clients.toList()[0].address_client[0].commune_id.toString(), color = Color.White.copy(
                                        alpha = 0.5f
                                    ))
                                }
                            }
                            Label("Ville", size = 16, fontWeight = FontWeight.Bold)
                            Label("Kinshasa", color = Color.White.copy(
                                    alpha = 0.5f
                                ))
                            Space(y = 9)
                            HorizontalDivider()
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


                                        if(!trashCleaner.isEmpty()){
                                            if(trashCleaner.toList()[0]?.state_trash_id == 2){
                                                titleMsg = "Information"
                                                msg = "Votre poubelle est encours de nettoyage"
                                                isShow = true
                                                onclick = { isShow = false}
                                            }
                                            else{
                                                if(isConnected){
                                                    isActive = false
                                                    coroutineScope.launch {
                                                        delay(3000)
                                                        val response = ClientKtor().postData(
                                                            trashAction,
                                                            Trash(client_id = clients.toList()[0].id, state_trash_id = 3)
                                                        )
                                                        val status = response.status.value
                                                        when(status){
                                                            in 200..299 ->{
                                                                isActive = true
                                                                val res = response.body<ResponseAPITrash>()
                                                                Toast.makeText(context,res.message, Toast.LENGTH_LONG).show()
                                                                msg = res.message
                                                                titleMsg = "Success"

                                                                isShow = true
                                                                scope.launch {
                                                                    StoreData(context).saveDataTrash(
                                                                        res.data.id.let {
                                                                            TrashClean(
                                                                                id = it,
                                                                                state_trash_id = res.data.state_trash_id,
                                                                                client_id = res.data.client_id,
                                                                                created_at = res.data.created_at
                                                                            )
                                                                        }
                                                                    )
                                                                }
                                                                scope.launch {
                                                                    StoreData(context).getDataTrash.collect{
                                                                        if (it != null) {
                                                                            trashCleaner[0] =it
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
                                                    titleMsg = "Success"
                                                    msg = "Vous n'êtes pas connecté veuillez vérifier votre connexion !!!"
                                                    isShow = true
                                                }
                                            }
                                        }
                                        else{
                                            if(isConnected){
                                                coroutineScope.launch {
                                                    delay(3000)
                                                    isActive = false
                                                    val response = ClientKtor().postData(
                                                        trashAction,
                                                        Trash(client_id = clients.toList()[0].id, state_trash_id = 3)
                                                    )
                                                    val status = response.status.value
                                                    when(status){
                                                        in 200..299 ->{
                                                            val res = response.body<ResponseAPITrash>()
                                                            Toast.makeText(context,res.message, Toast.LENGTH_LONG).show()
                                                            msg = res.message
                                                            titleMsg     = "Information"
                                                            isShow = true
                                                            isActive = true
                                                            scope.launch {
                                                                StoreData(context).saveDataTrash(
                                                                    res.data.id.let {
                                                                        TrashClean(
                                                                            id = it,
                                                                            state_trash_id = res.data.state_trash_id,
                                                                            client_id = res.data.client_id,
                                                                            created_at = res.data.created_at
                                                                        )
                                                                    }
                                                                )
                                                            }
                                                            scope.launch {
                                                                StoreData(context).getDataTrash.collect{
                                                                    if (it != null) {
                                                                        trashCleaner[0] =it
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
                                               titleMsg = "Problème de connexion"
                                                msg = "Vous n'êtes pas connecté veuillez vérifier votre connexion !!!"
                                                isShow = true
                                            }
                                        }

                                    },
                                    icon = {
                                        Icon(painterResource(R.drawable.arrow), modifier = Modifier.size(23.dp), contentDescription = null)
                                    }
                                )
                                if (!trashCleaner.isEmpty()){
                                    MButtonIcon(
                                        textColor = Color.White,
                                        backgroundColor = Color.Green.copy(0.4f),
                                        label = "Valider",
                                        click = {
                                            if(trashCleaner[0]?.state_trash_id == 2){
                                                titleMsg = "Information"
                                                msg = "La validation de la poubelle est fait est géré par notre équipe"
                                                isShow = true
                                                onclick = { isShow = false}
                                            }
                                            else{
                                                if(isConnected){
                                                    isActive = false
                                                    coroutineScope.launch {
                                                        delay(3000)
                                                        val response = ClientKtor().postData(
                                                            "${trashClean}${clients.toList()[0].id}",
                                                            Trash(client_id = clients.toList()[0].id, state_trash_id = 2)
                                                        )
                                                        val status = response.status.value
                                                        when(status){
                                                            in 200..299 ->{
                                                                isActive = true
                                                                val res = response.body<ResponseAPITrash>()
                                                                titleMsg ="Success"
                                                                msg = res.message
                                                                isShow = true
                                                                scope.launch {
                                                                    StoreData(context).saveDataTrash(
                                                                        res.data.id.let {
                                                                            TrashClean(
                                                                                id = it,
                                                                                state_trash_id = res.data.state_trash_id,
                                                                                client_id = res.data.client_id,
                                                                                created_at = res.data.created_at
                                                                            )
                                                                        }
                                                                    )
                                                                }
                                                                scope.launch {
                                                                    StoreData(context).getDataTrash.collect{
                                                                        if (it != null) {
                                                                            trashCleaner[0] =it
                                                                        }
                                                                        //Toast.makeText(context,"SIZE = ${trashCleaner.size}",Toast.LENGTH_LONG).show()
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
                                                    isShow = true
                                                    titleMsg = "Problème de connexion"
                                                }
                                            }
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
            }
            Column(Modifier.verticalScroll(rememberScrollState())) {
                //Toast.makeText(context,"${trashCleaner.size}",Toast.LENGTH_LONG).show()
                   Space(y = 20)
               if (!trashCleaner.isEmpty()){
                   var titleTrash = "Ma poubelle est propre"
                   var colorTrash = Color.Blue.copy(alpha = 0.1F)
                   var titleTrashRun = "Clean"
                   var iconState = R.drawable.check
                   when(trashCleaner[0]?.state_trash_id?.or(0)){
                       3 -> {
                           titleTrash = "Ma poubelle est pleine"
                           titleTrashRun = "On arrive"

                           iconState = R.drawable.run
                           colorTrash  =  Color.Green.copy(alpha = 0.1F)
                       }
                       2 -> {
                           titleTrash = "Nettoyage poubelle"
                           titleTrashRun = "Ramassage"
                           iconState = R.drawable.transfert
                           colorTrash = Color.Red.copy(
                               alpha = 0.1F,
                           )
                       }
                       1 ->{
                           titleTrash = "Ma poubelle est propre"
                           titleTrashRun = "Clean"
                           iconState = R.drawable.check
                       }
                   }
                   Card(Modifier.padding(10.dp),colors = CardDefaults.cardColors(
                       containerColor = Color.White
                   ), elevation = CardDefaults.cardElevation(5.dp)) {
                       Row(modifier = Modifier.fillMaxWidth().padding(5.dp),verticalAlignment = Alignment.CenterVertically) {
                           Icon(painterResource(R.drawable.top), contentDescription = "", modifier = Modifier.size(30.dp), tint = Color.Red.copy(alpha = 0.5f))
                           Spacer(Modifier.width(10.dp))
                           Column() {
                               Text(titleTrash)
                           }
                           Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Right) {

                               Box(modifier = Modifier.border(width = 10.dp, color = colorTrash,shape = RoundedCornerShape(52.dp)).scale(0.75f)){
                                   Icon(painterResource(iconState), modifier = Modifier.size(42.dp), contentDescription = "icon",tint = Color.Black)
                               }
                               Space(x = 4)
                               Column {
                                   Text(titleTrashRun, Modifier.absoluteOffset(y = 12.dp))
                               }

                           }
                       }
                   }
               }
            }
            Space(y = 20)
            ModernCarousel()
            if(isShow){
                MAlertDialog(
                    dialogTitle = titleMsg,
                    dialogText =  msg,
                    onDismissRequest = {
                        isShow = false
                    },
                    onConfirmation = onclick
                )
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
fun HomePagePreview(){
//    HomePageBody(navC = remem, isConnected = false)
}
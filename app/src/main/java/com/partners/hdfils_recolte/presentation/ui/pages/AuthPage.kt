package com.partners.hdfils_recolte.presentation.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.data.remote.ClientKtor
import com.partners.hdfils_recolte.data.remote.ResponseAPI
import com.partners.hdfils_recolte.data.remote.ResponseAPIGeneric
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.data.utils.EndPoint.clientAuth
import com.partners.hdfils_recolte.data.utils.isValidPhoneNumber
import com.partners.hdfils_recolte.domain.models.Address
import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.ClientAuth
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.presentation.ui.components.ConnectionBanner
import com.partners.hdfils_recolte.presentation.ui.components.MAlertDialog
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlue
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_recolte.presentation.ui.theme.BrandGold
import com.partners.hdfils_recolte.presentation.ui.theme.BrandStatusBar
import com.partners.hdfils_recolte.presentation.ui.theme.Danger
import com.partners.hdfils_recolte.presentation.ui.theme.HdfilsrecolteTheme
import com.partners.hdfils_recolte.presentation.ui.theme.Ink
import com.partners.hdfils_recolte.presentation.ui.theme.InkMuted
import com.partners.hdfils_recolte.presentation.ui.theme.Line
import com.partners.hdfils_recolte.presentation.ui.theme.PageBackground
import io.ktor.client.call.body
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AuthPage(navC: NavHostController, isConnected: Boolean) {
    AuthPageBody(navC, isConnected)
}

@Composable
fun AuthPageBody(navC: NavHostController, isConnected: Boolean) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var telephone by remember { mutableStateOf("") }
    var avenue by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var avenueError by remember { mutableStateOf<String?>(null) }
    var isActive by remember { mutableStateOf(true) }
    var msg by remember { mutableStateOf("") }
    var titleMsg by remember { mutableStateOf("Erreur") }
    var isShow by remember { mutableStateOf(false) }

    fun submit() {
        val avenueValue = avenue.trim()
        val phoneOk = isValidPhoneNumber(telephone)
        val avenueOk = avenueValue.isNotEmpty()
        phoneError = if (phoneOk) {
            null
        } else {
            "Ce numéro n'est pas valide en République démocratique du Congo."
        }
        avenueError = if (avenueOk) null else "Indiquez le nom de votre avenue."
        if (!phoneOk || !avenueOk) return
        if (!isConnected) {
            titleMsg = "Hors ligne"
            msg = "Vous êtes hors ligne. Vérifiez votre connexion, puis réessayez."
            isShow = true
            return
        }
        coroutineScope.launch {
            isActive = false
            try {
                val response = ClientKtor().postData(
                    clientAuth,
                    ClientAuth(telephone = telephone, avenue = avenueValue)
                )
                when (val status = response.status.value) {
                    in 200..299 -> {
                        val res = response.body<ResponseAPI>()
                        val remote = res.client
                        val address = remote?.address_client?.firstOrNull()
                        if (remote == null || address == null) {
                            isActive = true
                            titleMsg = "Erreur"
                            msg = "La réponse du serveur est incomplète. Réessayez."
                            isShow = true
                            return@launch
                        }
                        val store = StoreData(context)
                        val existing = store.getDataClient.first()
                        if (existing != null) {
                            val samePhone = existing.telephone == telephone
                            val sameAvenue = existing.address_client.firstOrNull()?.avenue == avenueValue
                            if (!samePhone || !sameAvenue) {
                                store.delete()
                            }
                        }
                        store.saveDataClientAuth(ClientAuth(telephone, avenueValue))
                        store.saveDataClient(
                            Client(
                                id = remote.id,
                                nom = remote.nom,
                                prenom = remote.prenom,
                                telephone = remote.telephone,
                                address_client = listOf(
                                    Address(
                                        commune_id = address.commune_id,
                                        avenue = address.avenue,
                                        quartier = address.quartier,
                                        numero_parcelle = address.numero_parcelle
                                    )
                                )
                            )
                        )
                        isActive = true
                        navC.navigate(route = ScreenRoute.Home.name) {
                            popUpTo(navC.graph.id) { inclusive = true }
                        }
                    }
                    in 500..599 -> {
                        isActive = true
                        titleMsg = "Service indisponible"
                        msg = "Le serveur ne répond pas. Réessayez dans un instant."
                        isShow = true
                    }
                    in 400..499 -> {
                        isActive = true
                        val res = response.body<ResponseAPIGeneric>()
                        titleMsg = "Information"
                        msg = res.message
                        isShow = true
                    }
                    else -> {
                        isActive = true
                        titleMsg = "Erreur"
                        msg = "Réponse inattendue ($status). Réessayez."
                        isShow = true
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                isActive = true
                titleMsg = "Erreur"
                msg = "Impossible de joindre le service. Réessayez."
                isShow = true
            }
        }
    }

    AuthScreen(
        telephone = telephone,
        avenue = avenue,
        isActive = isActive,
        isConnected = isConnected,
        phoneError = phoneError,
        avenueError = avenueError,
        onTelephoneChange = { input ->
            telephone = input.filter { it.isDigit() }.take(10)
            phoneError = null
        },
        onAvenueChange = { input ->
            avenue = input
            avenueError = null
        },
        onSubmit = ::submit
    )
    if (isShow) {
        MAlertDialog(
            dialogTitle = titleMsg,
            dialogText = msg,
            onDismissRequest = { isShow = false },
            onConfirmation = { isShow = false }
        )
    }
}

@Composable
private fun AuthScreen(
    telephone: String,
    avenue: String,
    isActive: Boolean,
    isConnected: Boolean,
    phoneError: String?,
    avenueError: String?,
    onTelephoneChange: (String) -> Unit,
    onAvenueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val headerBrush = Brush.verticalGradient(listOf(BrandBlueDeep, BrandBlue))
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandBlue,
        unfocusedBorderColor = Line,
        focusedLabelColor = BrandBlue,
        unfocusedLabelColor = InkMuted,
        cursorColor = BrandBlue,
        focusedLeadingIconColor = BrandBlue,
        unfocusedLeadingIconColor = InkMuted,
        focusedTextColor = Ink,
        unfocusedTextColor = Ink,
        errorBorderColor = Danger,
        errorLabelColor = Danger,
        errorLeadingIconColor = Danger,
        errorCursorColor = Danger
    )

    BrandStatusBar(useDarkIcons = false)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBrush)
                    .statusBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_gris),
                    contentDescription = "Logo Bopeto",
                    modifier = Modifier
                        .size(92.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Bopeto",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(7.dp)
                            .clip(RoundedCornerShape(50))
                            .background(BrandGold)
                    )
                    Text(
                        text = "  HD & FILS",
                        color = Color.White.copy(alpha = 0.92f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Kin ezo bonga",
                    color = BrandGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "La propreté de votre quartier, suivie simplement.",
                    color = Color.White.copy(alpha = 0.84f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-32).dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Connexion",
                        color = Ink,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Téléphone et avenue de votre domicile.",
                        color = InkMuted,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    if (!isConnected) {
                        Spacer(Modifier.height(10.dp))
                        ConnectionBanner()
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = telephone,
                        onValueChange = onTelephoneChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Téléphone") },
                        placeholder = { Text("0812345678") },
                        leadingIcon = {
                            Icon(Icons.Filled.Phone, contentDescription = null)
                        },
                        isError = phoneError != null,
                        supportingText = if (phoneError == null) null else {
                            { Text(phoneError.orEmpty()) }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors
                    )
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = avenue,
                        onValueChange = onAvenueChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Avenue") },
                        placeholder = { Text("Nom de votre avenue") },
                        leadingIcon = {
                            Icon(Icons.Filled.Place, contentDescription = null)
                        },
                        isError = avenueError != null,
                        supportingText = if (avenueError == null) null else {
                            { Text(avenueError.orEmpty()) }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                onSubmit()
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
                                                            if (it.telephone == telephone && it.address_client?.get(0)?.avenue == avenue){
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
                                                                    commune_id = res.client.address_client?.get(0)?.commune_id
                                                                        ?: 0,
                                                                    avenue = res.client.address_client?.get(0)?.avenue?:"",
                                                                    quartier = res.client.address_client?.get(0)?.quartier?:"",
                                                                    numero_parcelle = res.client.address_client?.get(0)?.numero_parcelle?:""
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
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onSubmit,
                        enabled = isActive,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandBlue,
                            contentColor = Color.White,
                            disabledContainerColor = BrandBlue.copy(alpha = 0.55f),
                            disabledContentColor = Color.White
                        )
                    ) {
                        if (isActive) {
                            Text(
                                text = "Se connecter",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                            Spacer(Modifier.size(10.dp))
                            Text(
                                text = "Connexion…",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Ces informations servent uniquement à identifier votre domicile.",
                        color = InkMuted,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Text(
                text = "Service de collecte  ·  Kinshasa",
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-12).dp)
                    .padding(bottom = 28.dp),
                color = InkMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        if (!isActive) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = BrandGold,
                trackColor = Color.White.copy(alpha = 0.28f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthPagePreview() {
    HdfilsrecolteTheme {
        AuthScreen(
            telephone = "",
            avenue = "",
            isActive = true,
            isConnected = true,
            phoneError = null,
            avenueError = null,
            onTelephoneChange = {},
            onAvenueChange = {},
            onSubmit = {}
        )
    }
}

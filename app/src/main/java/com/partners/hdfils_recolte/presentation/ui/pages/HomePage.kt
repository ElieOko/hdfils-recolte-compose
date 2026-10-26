package com.partners.hdfils_recolte.presentation.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.data.remote.ClientKtor
import com.partners.hdfils_recolte.data.remote.ResponseAPIGeneric
import com.partners.hdfils_recolte.data.remote.ResponseAPITrash
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.data.utils.EndPoint.trashAction
import com.partners.hdfils_recolte.data.utils.EndPoint.trashClean
import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.Trash
import com.partners.hdfils_recolte.domain.models.TrashClean
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.presentation.ui.components.ConnectionBanner
import com.partners.hdfils_recolte.presentation.ui.components.MAlertDialog
import com.partners.hdfils_recolte.presentation.ui.components.MButtonIcon
//import com.partners.hdfils_recolte.presentation.ui.components.ModernCarousel
import com.partners.hdfils_recolte.presentation.ui.components.Space
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@Composable
fun HomePage(navC: NavHostController, isConnected: Boolean) {
    HomePageBody(navC, isConnected)
}

@Composable
fun HomePageBody(navC: NavHostController, isConnected: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isActive by remember { mutableStateOf(true) }
    var isShow by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }
    var titleMsg by remember { mutableStateOf("Information") }
    var confirmText by remember { mutableStateOf("OK") }
    var dismissText by remember { mutableStateOf("Fermer") }
    var clientLoaded by remember { mutableStateOf(false) }
    val clients = remember { mutableStateListOf<Client>() }
    val trashCleaner = remember { mutableStateListOf<TrashClean>() }
    var onConfirm by remember { mutableStateOf<() -> Unit>({ isShow = false }) }

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
                                    Label(
                                        clients.toList()[0].address_client?.get(0)?.quartier?:"", color = Color.White.copy(
                                        alpha = 0.5f
                                    ))
                                }
                                Space(x=15)
                                Column {
                                    Label("Avenue", size = 16, fontWeight = FontWeight.Bold)
                                    Label(
                                        clients.toList()[0].address_client?.get(0)?.avenue?:"",color = Color.White.copy(
                                        alpha = 0.5f
                                    ))
                                }
                                Space(x=15)
                                Column {
                                    Label("Commune", size = 16, fontWeight = FontWeight.Bold)
                                    Label(
                                        clients.toList()[0].address_client?.get(0)?.commune_id.toString(), color = Color.White.copy(
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

    fun publishTrash(trash: TrashClean) {
        if (trashCleaner.isEmpty()) trashCleaner.add(trash) else trashCleaner[0] = trash
    }

    fun leaveHome() {
        isShow = false
        navC.navigate(route = ScreenRoute.Auth.name) {
            popUpTo(navC.graph.id) { inclusive = true }
        }
    }

    LaunchedEffect(Unit) {
        launch {
            try {
                StoreData(context).getDataClient.collect { value ->
                    clients.clear()
                    if (value != null) clients.add(value)
                    clientLoaded = true
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                clientLoaded = true
            }
        }
        launch {
            try {
                StoreData(context).getDataTrash.collect { value ->
                    trashCleaner.clear()
                    if (value != null) trashCleaner.add(value)
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
            }
        }
    }

    val client = clients.firstOrNull()
    val trash = trashCleaner.firstOrNull()

    suspend fun dispatch(response: HttpResponse, onSuccess: suspend (ResponseAPITrash) -> Unit) {
        when (val status = response.status.value) {
            in 200..299 -> onSuccess(response.body<ResponseAPITrash>())
            in 500..599 -> showDialog(
                "Service indisponible",
                "Le serveur ne répond pas. Réessayez dans un instant."
            )
            in 400..499 -> showDialog("Information", response.body<ResponseAPIGeneric>().message)
            else -> showDialog("Erreur", "Réponse inattendue ($status). Réessayez.")
        }
    }

    fun runRequest(block: suspend () -> Unit) {
        if (!isConnected) {
            showDialog(
                "Hors ligne",
                "Vous êtes hors ligne. Vérifiez votre connexion, puis réessayez."
            )
            return
        }
        scope.launch {
            isActive = false
            try {
                block()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                showDialog("Erreur", "Impossible de joindre le service. Réessayez.")
            } finally {
                isActive = true
            }
        }
    }

    fun refresh() {
        val current = client ?: return
        runRequest {
            val response = ClientKtor().getData("$trashAction/${current.id}")
            dispatch(response) { res ->
                publishTrash(res.data)
                StoreData(context).saveDataTrash(res.data)
                showDialog("Succès", "Mise à jour effectuée.")
            }
        }
    }

    fun requestPickup() {
        val current = client ?: return
        if (trash?.state_trash_id == 2) {
            showDialog("Information", "Votre poubelle est en cours de nettoyage.")
            return
        }
        runRequest {
            val response = ClientKtor().postData(
                trashAction,
                Trash(client_id = current.id, state_trash_id = 3)
            )
            dispatch(response) { res ->
                publishTrash(res.data)
                StoreData(context).saveDataTrash(res.data)
                showDialog("Succès", res.message)
            }
        }
    }

    fun confirmPickup() {
        val current = client ?: return
        if (trash?.state_trash_id == 2) {
            showDialog(
                "Information",
                "La validation du ramassage est gérée par notre équipe."
            )
            return
        }
        runRequest {
            val response = ClientKtor().postData(
                "$trashClean${current.id}",
                Trash(client_id = current.id, state_trash_id = 2)
            )
            dispatch(response) { res ->
                publishTrash(res.data)
                StoreData(context).saveDataTrash(res.data)
                showDialog("Succès", res.message)
            }
        }
    }

    BrandStatusBar(useDarkIcons = false)
    Scaffold(
        containerColor = PageBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(bottom = 20.dp)
            ) {
                HomeHeader(
                    greeting = greetingForNow(),
                    name = when {
                        client != null -> displayName(client)
                        clientLoaded -> "Bopeto"
                        else -> "…"
                    },
                    onRefresh = ::refresh,
                    onSignOut = {
                        showDialog(
                            title = "Déconnexion",
                            message = "Voulez-vous vraiment vous déconnecter ?",
                            confirmLabel = "Se déconnecter",
                            dismissLabel = "Annuler",
                            confirm = ::leaveHome
                        )
                    },
                    refreshEnabled = isActive && client != null
                )
                Spacer(Modifier.height(18.dp))
                if (!isConnected) {
                    ConnectionBanner(Modifier.padding(horizontal = 20.dp))
                    Spacer(Modifier.height(14.dp))
                }
                when {
                    !clientLoaded -> LoadingCard()
                    client == null -> EmptySession(onBack = ::leaveHome)
                    else -> {
                        SectionLabel("Votre adresse")
                        Spacer(Modifier.height(8.dp))
                        AddressCard(client)
                        Spacer(Modifier.height(18.dp))
                        SectionLabel("Ma poubelle")
                        Spacer(Modifier.height(8.dp))
                        TrashStatusCard(trash)
                        Spacer(Modifier.height(18.dp))
                        SectionLabel("Actions")
                        Spacer(Modifier.height(8.dp))
                        ActionsCard(
                            canConfirm = trash != null,
                            enabled = isActive,
                            onRequestPickup = ::requestPickup,
                            onConfirmPickup = ::confirmPickup
                        )
                    }
                }
                Spacer(Modifier.height(22.dp))
                SectionLabel("À la une")
                Spacer(Modifier.height(10.dp))
                ModernCarousel()
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
    if (isShow) {
        MAlertDialog(
            dialogTitle = titleMsg,
            dialogText = msg,
            confirmText = confirmText,
            dismissText = dismissText,
            onDismissRequest = { isShow = false },
            onConfirmation = onConfirm
        )
    }
}

@Composable
private fun HomeHeader(
    greeting: String,
    name: String,
    onRefresh: () -> Unit,
    onSignOut: () -> Unit,
    refreshEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(Brush.verticalGradient(listOf(BrandBlueDeep, BrandBlue)))
            .statusBarsPadding()
            .padding(start = 20.dp, end = 8.dp, top = 16.dp, bottom = 22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.logo_gris),
                contentDescription = "Logo Bopeto",
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = greeting,
                    color = Color.White.copy(alpha = 0.78f),
                    fontSize = 13.sp
                )
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Kin ezo bonga",
                    color = BrandGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = onRefresh, enabled = refreshEnabled) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Actualiser l'état de la poubelle",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onSignOut) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Se déconnecter",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun AddressCard(client: Client) {
    val address = client.address_client.firstOrNull()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row {
                InfoCell(
                    modifier = Modifier.weight(1f),
                    label = "Quartier",
                    value = address?.quartier.orDash(),
                    icon = Icons.Filled.Home
                )
                InfoCell(
                    modifier = Modifier.weight(1f),
                    label = "Avenue",
                    value = address?.avenue.orDash(),
                    icon = Icons.Filled.Place
                )
            }
            Spacer(Modifier.height(8.dp))
            Row {
                InfoCell(
                    modifier = Modifier.weight(1f),
                    label = "Commune",
                    value = address?.commune_id?.takeIf { it > 0 }?.let { "N° $it" } ?: "—",
                    icon = Icons.Filled.LocationOn
                )
                InfoCell(
                    modifier = Modifier.weight(1f),
                    label = "Ville",
                    value = "Kinshasa",
                    icon = Icons.Filled.LocationOn
                )
            }
            if (client.telephone.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(color = Line)
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = client.telephone,
                        color = Ink,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCell(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BrandBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(text = label, color = InkMuted, fontSize = 12.sp)
            Text(
                text = value,
                color = Ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TrashStatusCard(trash: TrashClean?) {
    val presentation = trashPresentation(trash?.state_trash_id)
    val updated = formatStamp(trash?.created_at)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = presentation.container),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = presentation.icon,
                    contentDescription = null,
                    tint = presentation.accent,
                    modifier = Modifier.size(26.dp)
                )
            }
            Space(y = 20)
            //ModernCarousel()
            if(isShow){
                MAlertDialog(
                    dialogTitle = titleMsg,
                    dialogText =  msg,
                    onDismissRequest = {
                        isShow = false
                    },
                    color = InkMuted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                if (updated != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Mis à jour le $updated",
                        color = InkMuted,
                        fontSize = 12.sp
                    )
                }
            }
            Text(
                text = presentation.badge,
                color = presentation.accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun ActionsCard(
    canConfirm: Boolean,
    enabled: Boolean,
    onRequestPickup: () -> Unit,
    onConfirmPickup: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Le vidage prévient l'équipe. La confirmation indique que le passage est fait.",
                color = InkMuted,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Button(
                onClick = onRequestPickup,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue,
                    contentColor = Color.White,
                    disabledContainerColor = BrandBlue.copy(alpha = 0.35f),
                    disabledContentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Demander le vidage", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
            if (canConfirm) {
                OutlinedButton(
                    onClick = onConfirmPickup,
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandBlue)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Confirmer le passage", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = BrandBlue
            )
            Spacer(Modifier.width(12.dp))
            Text("Chargement de votre espace…", color = Ink, fontSize = 14.sp)
        }
    }
}

@Composable
private fun EmptySession(onBack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Session introuvable",
                color = Ink,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Reconnectez-vous avec votre téléphone et votre avenue.",
                color = InkMuted,
                fontSize = 14.sp
            )
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text("Retour à la connexion")
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = 16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(BrandGold)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

private data class TrashPresentation(
    val title: String,
    val detail: String,
    val badge: String,
    val container: Color,
    val accent: Color,
    val icon: ImageVector
)

private fun trashPresentation(stateId: Int?): TrashPresentation = when (stateId) {
    3 -> TrashPresentation(
        title = "Poubelle pleine",
        detail = "Votre demande est enregistrée. Une équipe est en route.",
        badge = "En route",
        container = Color(0xFFFFF6E8),
        accent = BrandGold,
        icon = Icons.Filled.Info
    )
    2 -> TrashPresentation(
        title = "Ramassage en cours",
        detail = "Notre équipe s'occupe du passage.",
        badge = "En cours",
        container = Color(0xFFE7F1FC),
        accent = BrandBlue,
        icon = Icons.Filled.Refresh
    )
    else -> TrashPresentation(
        title = "Poubelle propre",
        detail = "Aucun ramassage en attente pour le moment.",
        badge = "À jour",
        container = Color(0xFFE8F6EE),
        accent = Success,
        icon = Icons.Filled.CheckCircle
    )
}

private fun displayName(client: Client): String {
    return listOf(client.prenom, client.nom)
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .joinToString(" ") { part ->
            part.lowercase(Locale.FRENCH).replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase(Locale.FRENCH) else char.toString()
            }
        }
        .ifBlank { "Client" }
}

private fun greetingForNow(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Bonjour"
        in 12..17 -> "Bon après-midi"
        else -> "Bonsoir"
    }
}

private fun formatStamp(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    return raw.replace('T', ' ').substringBefore('.').trim().ifBlank { null }
}

private fun String?.orDash(): String = if (this.isNullOrBlank()) "—" else this

@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    HdfilsrecolteTheme {
        HomeHeader(
            greeting = "Bonjour",
            name = "Amina Kabila",
            onRefresh = {},
            onSignOut = {},
            refreshEnabled = true
        )
    }
}

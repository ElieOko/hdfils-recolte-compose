package com.partners.hdfils_recolte.presentation.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.TrashClean
import com.partners.hdfils_recolte.domain.models.TrashTone
import com.partners.hdfils_recolte.domain.models.addresses
import com.partners.hdfils_recolte.domain.models.communeLabel
import com.partners.hdfils_recolte.domain.models.displayName
import com.partners.hdfils_recolte.domain.models.subscription
import com.partners.hdfils_recolte.domain.models.trashStatus
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.presentation.state.DialogKind
import com.partners.hdfils_recolte.presentation.state.HomeEvent
import com.partners.hdfils_recolte.presentation.state.HomeViewModel
import com.partners.hdfils_recolte.presentation.ui.components.AppLogo
import com.partners.hdfils_recolte.presentation.ui.components.CasaCarousel
import com.partners.hdfils_recolte.presentation.ui.components.InfoRow
import com.partners.hdfils_recolte.presentation.ui.components.MAlertDialog
import com.partners.hdfils_recolte.presentation.ui.components.MButtonIcon
import com.partners.hdfils_recolte.presentation.ui.components.PrimaryButton
import com.partners.hdfils_recolte.presentation.ui.components.SectionLabel
import com.partners.hdfils_recolte.presentation.ui.components.SubscriptionBlock
import com.partners.hdfils_recolte.presentation.ui.components.formatApiDate
import com.partners.hdfils_recolte.presentation.ui.theme.AppBackground
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlue
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDark
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_recolte.presentation.ui.theme.Danger
import com.partners.hdfils_recolte.presentation.ui.theme.Ink
import com.partners.hdfils_recolte.presentation.ui.theme.InkMuted
import com.partners.hdfils_recolte.presentation.ui.theme.Success
import com.partners.hdfils_recolte.presentation.ui.theme.Warning

@Composable
fun HomePage(navC: NavHostController, isConnected: Boolean) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is HomeEvent.SignedOut) {
                navC.navigate(ScreenRoute.Auth.name) {
                    popUpTo(navC.graph.id) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
    ) {
        val client = state.client
        if (!state.ready || client == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppLogo(size = 84.dp, ring = BrandBlueLight)
                Spacer(Modifier.height(12.dp))
                Text("CasaClean", color = BrandBlueDeep, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                if (state.ready && client == null) {
                    Spacer(Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Se connecter",
                        onClick = {
                            navC.navigate(ScreenRoute.Auth.name) {
                                popUpTo(navC.graph.id) { inclusive = true }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 32.dp),
                    )
                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                ClientHeader(
                    client = client,
                    onRefresh = { viewModel.refresh(isConnected) },
                    onSignOut = viewModel::askSignOut,
                )
                if (state.loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        trackColor = BrandBlueDark,
                    )
                }
                if (!isConnected) {
                    Text(
                        text = "Hors ligne. Les actions de collecte attendent le réseau.",
                        color = BrandBlueDeep,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Warning.copy(alpha = 0.16f))
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                    )
                }
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(420)) + slideInVertically(tween(480)) { it / 8 },
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .navigationBarsPadding()
                            .padding(bottom = 28.dp),
                    ) {
                        SectionLabel("À la une")
                        CasaCarousel()
                        SectionLabel("Souscription")
                        WhiteCard {
                            Text(
                                text = client.code.ifBlank { "Compte client" },
                                color = BrandBlue,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                            )
                            Spacer(Modifier.height(6.dp))
                            SubscriptionBlock(souscription = client.subscription())
                            if (!client.created_at.isNullOrBlank()) {
                                Text(
                                    text = "Client depuis le ${formatApiDate(client.created_at)}",
                                    color = InkMuted,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                        SectionLabel("Adresse")
                        WhiteCard {
                            val address = client.addresses().firstOrNull()
                            InfoRow(Icons.Default.Place, "Quartier", address?.quartier.orEmpty())
                            InfoRow(Icons.Default.Place, "Avenue", address?.avenue.orEmpty())
                            InfoRow(Icons.Default.Home, "Parcelle", address?.numero_parcelle.orEmpty())
                            InfoRow(Icons.Default.Place, "Commune", communeLabel(address?.commune_id))
                            InfoRow(Icons.Default.Home, "Ville", "Kinshasa")
                            InfoRow(Icons.Default.Phone, "Téléphone", client.telephone)
                        }
                        SectionLabel("Poubelle")
                        TrashCard(trash = state.trash)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            MButtonIcon(
                                modifier = Modifier.weight(1f),
                                backgroundColor = Danger.copy(alpha = 0.9f),
                                textColor = Color.White,
                                label = "Vidage",
                                enabled = !state.loading,
                                click = { viewModel.requestEmpty(isConnected) },
                                icon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp),
                                    )
                                },
                            )
                            if (state.trash != null) {
                                MButtonIcon(
                                    modifier = Modifier.weight(1f),
                                    backgroundColor = Success,
                                    textColor = Color.White,
                                    label = "Valider",
                                    enabled = !state.loading,
                                    click = { viewModel.validateClean(isConnected) },
                                    icon = {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp),
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
        val dialog = state.dialog
        if (dialog != null) {
            MAlertDialog(
                dialogTitle = dialog.title,
                dialogText = dialog.message,
                confirmText = dialog.confirmLabel,
                onDismissRequest = viewModel::dismissDialog,
                onConfirmation = {
                    if (dialog.kind == DialogKind.ConfirmSignOut) {
                        viewModel.onDialogConfirm()
                    } else {
                        viewModel.dismissDialog()
                    }
                },
            )
        }
    }
}

@Composable
private fun ClientHeader(
    client: Client,
    onRefresh: () -> Unit,
    onSignOut: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(BrandBlue, BrandBlueDark)))
            .statusBarsPadding()
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppLogo(size = 56.dp)
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f),
            ) {
                Text(
                    text = "CasaClean",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Text(
                    text = "Bonjour ${client.displayName()}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                )
            }
            IconButton(onClick = onSignOut) {
                Icon(
                    painter = painterResource(R.drawable.signout),
                    contentDescription = "Se déconnecter",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp),
                )
            }
            IconButton(onClick = onRefresh) {
                Icon(
                    painter = painterResource(R.drawable.transfert),
                    contentDescription = "Actualiser",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
        if (client.code.isNotBlank()) {
            Surface(
                color = Color.White.copy(alpha = 0.16f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                Text(
                    text = client.code,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun WhiteCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun TrashCard(trash: TrashClean?) {
    val status = trashStatus(trash?.state_trash_id)
    val tone = when (status.tone) {
        TrashTone.Clean -> Success
        TrashTone.Waiting -> Warning
        TrashTone.Full -> Danger
        TrashTone.Unknown -> BrandBlue
    }
    WhiteCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = tone.copy(alpha = 0.14f), shape = RoundedCornerShape(16.dp)) {
                Icon(
                    painter = painterResource(R.drawable.trash),
                    contentDescription = null,
                    tint = tone,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(26.dp),
                )
            }
            Column(Modifier.padding(start = 12.dp).weight(1f)) {
                Text(status.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(status.detail, color = InkMuted, fontSize = 13.sp)
            }
        }
    }
}

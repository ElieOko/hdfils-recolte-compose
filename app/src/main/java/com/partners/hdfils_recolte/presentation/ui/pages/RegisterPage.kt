package com.partners.hdfils_recolte.presentation.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.partners.hdfils_recolte.domain.models.splitAmount
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.domain.subscription.trancheOptions
import com.partners.hdfils_recolte.presentation.state.RegisterEvent
import com.partners.hdfils_recolte.presentation.state.RegisterViewModel
import com.partners.hdfils_recolte.presentation.ui.components.AppField
import com.partners.hdfils_recolte.presentation.ui.components.AppLogo
import com.partners.hdfils_recolte.presentation.ui.components.DeviseSelector
import com.partners.hdfils_recolte.presentation.ui.components.PrimaryButton
import com.partners.hdfils_recolte.presentation.ui.components.SearchableCommuneField
import com.partners.hdfils_recolte.presentation.ui.components.TrancheChip
import com.partners.hdfils_recolte.presentation.ui.theme.AppBackground
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlue
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_recolte.presentation.ui.theme.Danger
import com.partners.hdfils_recolte.presentation.ui.theme.Ink
import com.partners.hdfils_recolte.presentation.ui.theme.InkMuted
import com.partners.hdfils_recolte.presentation.ui.theme.Line

@Composable
fun RegisterPage(navC: NavHostController, isConnected: Boolean) {
    val viewModel: RegisterViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val draft = state.draft
    val focusManager = LocalFocusManager.current
    val amount = draft.montant.toIntOrNull() ?: 0
    val preview = splitAmount(amount, draft.nombreTranches)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.OpenHome -> {
                    navC.navigate(ScreenRoute.Home.name) {
                        popUpTo(navC.graph.id) { inclusive = true }
                    }
                }
                is RegisterEvent.OpenLogin -> {
                    navC.getBackStackEntry(ScreenRoute.Auth.name)
                        .savedStateHandle["notice"] = event.message
                    navC.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(bottom = 24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navC.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = BrandBlueDeep,
                )
            }
            Text(
                text = "Retour à la connexion",
                color = BrandBlueDeep,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppLogo(size = 78.dp, ring = BrandBlue.copy(alpha = 0.18f))
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "CasaClean",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlueDeep,
                )
                Text(
                    text = "Créer mon compte client",
                    fontSize = 14.sp,
                    color = InkMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                )
                SearchableCommuneField(
                    resetKey = state.resetKey,
                    selectedId = draft.communeId,
                    onSelect = { viewModel.onCommune(it.id) },
                )
                Spacer(Modifier.height(10.dp))
                AppField(
                    value = draft.nom,
                    onValueChange = viewModel::onNom,
                    label = "Nom",
                    icon = Icons.Default.Person,
                )
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = draft.prenom,
                    onValueChange = viewModel::onPrenom,
                    label = "Prénom",
                    icon = Icons.Default.Person,
                )
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = draft.telephone,
                    onValueChange = viewModel::onTelephone,
                    label = "Téléphone",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    AppField(
                        value = draft.avenue,
                        onValueChange = viewModel::onAvenue,
                        label = "Avenue",
                        icon = Icons.Default.Place,
                        modifier = Modifier.weight(1.4f),
                    )
                    Spacer(Modifier.width(8.dp))
                    AppField(
                        value = draft.numeroParcelle,
                        onValueChange = viewModel::onNumero,
                        label = "N°",
                        icon = Icons.Default.Home,
                        modifier = Modifier.weight(0.8f),
                    )
                }
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = draft.quartier,
                    onValueChange = viewModel::onQuartier,
                    label = "Quartier",
                    icon = Icons.Default.Place,
                )
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = draft.montant,
                    onValueChange = viewModel::onMontant,
                    label = "Montant de souscription",
                    icon = Icons.Default.Info,
                    keyboardType = KeyboardType.Number,
                )
                Spacer(Modifier.height(12.dp))
                DeviseSelector(selected = draft.devise, onSelect = viewModel::onDevise)
                Spacer(Modifier.height(14.dp))
                TrancheTypePicker(
                    selected = draft.nombreTranches,
                    onSelect = viewModel::onTranches,
                )
                AnimatedVisibility(
                    visible = preview.isNotEmpty(),
                    enter = fadeIn(tween(280)) + expandVertically(tween(320)),
                ) {
                    Column(Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        Text(
                            text = "Répartition estimée",
                            color = Ink,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                        )
                        Spacer(Modifier.height(8.dp))
                        preview.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                row.forEach { tranche ->
                                    TrancheChip(
                                        tranche = tranche,
                                        devise = draft.devise,
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
                if (state.error != null) {
                    Text(
                        text = state.error.orEmpty(),
                        color = Danger,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    )
                }
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = "Créer mon compte",
                    loading = state.loading,
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.submit(isConnected)
                    },
                )
            }
        }
    }
}

@Composable
private fun TrancheTypePicker(
    selected: Int,
    onSelect: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Type de tranche",
            color = Ink,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
        Text(
            text = "Choisissez comment répartir votre souscription.",
            color = InkMuted,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
        )
        trancheOptions.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { option ->
                    val isSelected = selected == option.count
                    val background by animateColorAsState(
                        targetValue = if (isSelected) BrandBlue else Color.White,
                        animationSpec = tween(220),
                        label = "tranche-${option.count}",
                    )
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelect(option.count) },
                        shape = RoundedCornerShape(16.dp),
                        color = background,
                        border = BorderStroke(1.dp, if (isSelected) BrandBlue else Line),
                    ) {
                        Column(Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
                            Text(
                                text = option.title,
                                color = if (isSelected) Color.White else BrandBlueDeep,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                            )
                            Text(
                                text = option.rhythm,
                                color = if (isSelected) Color.White.copy(alpha = 0.9f) else InkMuted,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

package com.partners.hdfils_recolte.presentation.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.presentation.state.AuthEvent
import com.partners.hdfils_recolte.presentation.state.AuthViewModel
import com.partners.hdfils_recolte.presentation.ui.components.AppField
import com.partners.hdfils_recolte.presentation.ui.components.AppLogo
import com.partners.hdfils_recolte.presentation.ui.components.BrandWordmark
import com.partners.hdfils_recolte.presentation.ui.components.PrimaryButton
import com.partners.hdfils_recolte.presentation.ui.theme.AnimatedBackgroundShapes
import com.partners.hdfils_recolte.presentation.ui.theme.AppBackground
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlue
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_recolte.presentation.ui.theme.Danger
import com.partners.hdfils_recolte.presentation.ui.theme.InkMuted
import com.partners.hdfils_recolte.presentation.ui.theme.Success
import kotlinx.coroutines.flow.first

@Composable
fun AuthPage(navC: NavHostController, isConnected: Boolean) {
    val viewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (StoreData(context).isLoggedIn.first()) {
            navC.navigate(ScreenRoute.Home.name) {
                popUpTo(navC.graph.id) { inclusive = true }
            }
        }
    }
    val focusManager = LocalFocusManager.current
    var notice by remember { mutableStateOf("") }
    LaunchedEffect(navC) {
        val handle = navC.currentBackStackEntry?.savedStateHandle ?: return@LaunchedEffect
        handle.getStateFlow("notice", "").collect { notice = it }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is AuthEvent.OpenHome) {
                navC.navigate(ScreenRoute.Home.name) {
                    popUpTo(navC.graph.id) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        AnimatedBackgroundShapes()
        BoxWithConstraints(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 22.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(520)) + scaleIn(tween(620), initialScale = 0.86f),
                        ) {
                            AppLogo(size = 96.dp, ring = BrandBlue.copy(alpha = 0.2f))
                        }
                        Spacer(Modifier.height(12.dp))
                        BrandWordmark()
                        Text(
                            text = "Connexion client",
                            color = InkMuted,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        Text(
                            text = "Entrez le téléphone et l'avenue de votre domicile. La session reste ouverte sur cet appareil.",
                            color = InkMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp, bottom = 16.dp),
                        )
                        if (notice.isNotBlank()) {
                            Surface(
                                color = Success.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                            ) {
                                Text(
                                    text = notice,
                                    color = BrandBlueDeep,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(12.dp),
                                )
                            }
                        }
                        AppField(
                            value = state.telephone,
                            onValueChange = viewModel::onTelephone,
                            label = "Téléphone",
                            icon = Icons.Default.Phone,
                            keyboardType = KeyboardType.Phone,
                            isError = state.attempted && state.error != null && state.telephone.length < 10,
                        )
                        Spacer(Modifier.height(10.dp))
                        AppField(
                            value = state.avenue,
                            onValueChange = viewModel::onAvenue,
                            label = "Avenue",
                            icon = Icons.Default.Home,
                            imeAction = ImeAction.Done,
                            onImeAction = {
                                focusManager.clearFocus()
                                viewModel.submit(isConnected)
                            },
                        )
                        if (state.error != null) {
                            Text(
                                text = state.error.orEmpty(),
                                color = Danger,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                            )
                        }
                        Spacer(Modifier.height(18.dp))
                        PrimaryButton(
                            text = "Se connecter",
                            loading = state.loading,
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.submit(isConnected)
                            },
                        )
                        TextButton(
                            onClick = { navC.navigate(ScreenRoute.Register.name) },
                            modifier = Modifier.padding(top = 6.dp),
                        ) {
                            Text(
                                text = "Créer un compte",
                                color = BrandBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(28.dp))
            }
        }
    }
}

package com.partners.hdfils_recolte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.domain.connectivity.AndroidConnectivityObserver
import com.partners.hdfils_recolte.domain.connectivity.ConnectivityViewModel
import com.partners.hdfils_recolte.domain.route.Navigation
import com.partners.hdfils_recolte.domain.route.startRoute
import com.partners.hdfils_recolte.presentation.ui.components.AppLogo
import com.partners.hdfils_recolte.presentation.ui.theme.AppBackground
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlue
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_recolte.presentation.ui.theme.CasaCleanTheme
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0xFF307EC7.toInt()),
            navigationBarStyle = SystemBarStyle.light(0xFFFFFFFF.toInt(), 0xFFFFFFFF.toInt()),
        )
        setContent {
            val viewModel = viewModel<ConnectivityViewModel> {
                ConnectivityViewModel(
                    connectivityObserver = AndroidConnectivityObserver(
                        context = applicationContext,
                    ),
                )
            }
            val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
            CasaCleanTheme {
                CasaCleanApp(isConnected)
            }
        }
    }
}

@Composable
private fun CasaCleanApp(isConnected: Boolean) {
    val context = LocalContext.current
    val store = remember { StoreData(context) }
    var ready by remember { mutableStateOf(false) }
    var loggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(store) {
        loggedIn = store.isLoggedIn.first()
        ready = true
    }

    if (!ready) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AppLogo(size = 88.dp, ring = BrandBlueLight)
                Spacer(Modifier.height(14.dp))
                CircularProgressIndicator(color = BrandBlue)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "CasaClean",
                    color = BrandBlueDeep,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    } else {
        val navHostController = rememberNavController()
        Navigation(
            navC = navHostController,
            isConnected = isConnected,
            startDestination = startRoute(loggedIn).name,
        )
    }
}

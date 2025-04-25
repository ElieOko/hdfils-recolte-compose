package com.partners.hdfils_recolte.domain.connectivity


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.partners.hdfils_recolte.domain.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ConnectivityViewModel(
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {

    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )
}
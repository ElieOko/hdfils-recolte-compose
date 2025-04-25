package com.partners.hdfils_recolte.domain.connectivity


import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}
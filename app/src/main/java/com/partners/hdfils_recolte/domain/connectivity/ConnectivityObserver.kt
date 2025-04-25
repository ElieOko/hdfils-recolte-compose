package com.partners.hdfils_agent.domain.connectivity


import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}
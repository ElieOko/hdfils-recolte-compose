package com.partners.hdfils_recolte.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Client(
    var id              : Int   = 0,
    var code            : String = "",
    var nom             : String= "",
    var prenom          : String="",
    var telephone       : String="",
    val address_client  : List<Address>
)

@Serializable
data class ClientAuth(
    val telephone: String,
    val avenue : String
)

@Serializable
data class Address(
    var id  : Int = 0,
    var avenue : String = "",
    var numero_parcelle : String = "",
    var quartier : String = "",
    var commune_id: Int = 0
)
package com.partners.hdfils_recolte.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Tranche(
    val numero: Int = 0,
    val montant: Int = 0,
)

@Serializable
data class Souscription(
    val montant_souscription: Int = 0,
    val devise: String = "",
    val nombre_tranches: Int = 0,
    val tranche: String = "",
    val tranches: List<Tranche> = emptyList(),
)

@Serializable
data class Client(
    var id: Int = 0,
    var code: String = "",
    var nom: String = "",
    var prenom: String = "",
    var telephone: String = "",
    val montant_souscription: Int = 0,
    val devise: String = "",
    val nombre_tranches: Int = 0,
    val tranche: String = "",
    val tranches: List<Tranche>? = emptyList(),
    val created_at: String? = null,
    val updated_at: String? = null,
    val address_client: List<Address>? = emptyList(),
)

@Serializable
data class ClientAuth(
    val telephone: String,
    val avenue: String,
)

@Serializable
data class ClientCreate(
    val nom: String,
    val prenom: String,
    val telephone: String,
    val commune_id: Int,
    val avenue: String,
    val quartier: String,
    val numero_parcelle: String,
    val montant_souscription: Int,
    val devise: String,
    val nombre_tranches: Int,
)

@Serializable
data class Address(
    var id: Int = 0,
    var client_id: Int = 0,
    var avenue: String = "",
    var numero_parcelle: String = "",
    var quartier: String = "",
    var commune_id: Int = 0,
    var created_at: String? = null,
    var updated_at: String? = null,
)

fun Client.displayName(): String {
    return listOf(prenom, nom)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { "Client" }
}

fun Client.addresses(): List<Address> = address_client.orEmpty()

fun Client.subscription(): Souscription {
    return Souscription(
        montant_souscription = montant_souscription,
        devise = devise.ifBlank { "FC" },
        nombre_tranches = nombre_tranches,
        tranche = tranche,
        tranches = resolvedTranches(),
    )
}

fun Client.resolvedTranches(): List<Tranche> {
    val provided = tranches.orEmpty().filter { it.numero > 0 }
    if (provided.isNotEmpty()) return provided
    return splitAmount(montant_souscription, nombre_tranches)
}

fun splitAmount(montant: Int, count: Int): List<Tranche> {
    if (montant <= 0 || count <= 0) return emptyList()
    val base = montant / count
    val remainder = montant % count
    return (1..count).map { index ->
        val extra = if (index == count) remainder else 0
        Tranche(numero = index, montant = base + extra)
    }
}

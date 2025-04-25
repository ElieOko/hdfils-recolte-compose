package com.partners.hdfils_recolte.data.remote

import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.TrashClean
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAPI(
    val message: String,
    val client : Client?
)

@Serializable
data class ResponseAPITrash(
    val message: String,
    val data :  TrashClean
)

@Serializable
data class ResponseAPIGeneric(
    val message: String
)

package com.partners.hdfils_recolte.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Trash(
    val client_id : Int,
    val state_trash_id : Int
)

@Serializable
data class TrashClean(
    val id              : Int,
    val client_id       : Int,
    var state_trash_id  : Int = 0,
    val created_at      : String
)

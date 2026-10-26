package com.partners.hdfils_recolte.domain.models

enum class TrashTone {
    Clean,
    Waiting,
    Full,
    Unknown,
}

data class TrashStatus(
    val title: String,
    val detail: String,
    val tone: TrashTone,
)

fun trashStatus(stateId: Int?): TrashStatus = when (stateId) {
    1 -> TrashStatus(
        title = "Ma poubelle est propre",
        detail = "Clean",
        tone = TrashTone.Clean,
    )
    2 -> TrashStatus(
        title = "Nettoyage en cours",
        detail = "Ramassage",
        tone = TrashTone.Waiting,
    )
    3 -> TrashStatus(
        title = "Ma poubelle est pleine",
        detail = "On arrive",
        tone = TrashTone.Full,
    )
    else -> TrashStatus(
        title = "Aucune demande",
        detail = "Demandez un vidage quand la poubelle est pleine",
        tone = TrashTone.Unknown,
    )
}

package com.partners.hdfils_recolte.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Commune(
    val id: Int = 0,
    val nom: String = "",
) {
    fun communeList(): List<Commune> {
        return listOf(
            Commune(1, "Bandalungwa"),
            Commune(2, "Barumbu"),
            Commune(3, "Bumbu"),
            Commune(4, "Gombe"),
            Commune(5, "Kalamu"),
            Commune(6, "Kasa-Vubu"),
            Commune(7, "Kimbanseke"),
            Commune(8, "Kinshasa"),
            Commune(9, "Kintambo"),
            Commune(10, "Kisenso"),
            Commune(11, "Lemba"),
            Commune(12, "Limete"),
            Commune(13, "Lingwala"),
            Commune(14, "Makala"),
            Commune(15, "Maluku"),
            Commune(16, "Masina"),
            Commune(17, "Matete"),
            Commune(18, "Mont-Ngafula"),
            Commune(19, "Ndjili"),
            Commune(20, "Ngaba"),
            Commune(21, "Ngaliema"),
            Commune(22, "Ngiri-Ngiri"),
            Commune(23, "Nsele"),
            Commune(24, "Selembao"),
        )
    }
}

fun communeLabel(id: Int?): String {
    if (id == null || id == 0) return "—"
    return Commune().communeList().firstOrNull { it.id == id }?.nom ?: "Commune $id"
}

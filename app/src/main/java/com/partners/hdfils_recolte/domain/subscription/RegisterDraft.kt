package com.partners.hdfils_recolte.domain.subscription

import com.partners.hdfils_recolte.data.utils.isValidPhoneNumber

data class TrancheOption(
    val count: Int,
    val title: String,
    val rhythm: String,
)

val trancheOptions = listOf(
    TrancheOption(1, "1 tranche", "Paiement unique"),
    TrancheOption(2, "2 tranches", "Deux versements"),
    TrancheOption(3, "3 tranches", "Trois versements"),
    TrancheOption(4, "4 tranches", "Quatre versements"),
    TrancheOption(6, "6 tranches", "Six versements"),
    TrancheOption(12, "12 tranches", "Chaque mois"),
)

data class RegisterDraft(
    val nom: String = "",
    val prenom: String = "",
    val telephone: String = "",
    val communeId: Int = 0,
    val avenue: String = "",
    val quartier: String = "",
    val numeroParcelle: String = "",
    val montant: String = "",
    val devise: String = "FC",
    val nombreTranches: Int = 0,
)

fun validateRegister(draft: RegisterDraft): String? = when {
    draft.communeId == 0 -> "Sélectionnez une commune"
    draft.nom.isBlank() -> "Le nom est obligatoire"
    draft.prenom.isBlank() -> "Le prénom est obligatoire"
    draft.telephone.isBlank() -> "Le téléphone est obligatoire"
    !isValidPhoneNumber(draft.telephone) -> "Ce numéro n'est pas valide en République démocratique du Congo"
    draft.avenue.isBlank() -> "L'avenue est obligatoire"
    draft.quartier.isBlank() -> "Le quartier est obligatoire"
    draft.numeroParcelle.isBlank() -> "Le numéro de parcelle est obligatoire"
    draft.montant.toIntOrNull() == null || (draft.montant.toIntOrNull() ?: 0) <= 0 ->
        "Le montant de souscription est obligatoire"
    draft.devise.isBlank() -> "Sélectionnez une devise"
    draft.nombreTranches <= 0 -> "Choisissez le type de tranche"
    else -> null
}

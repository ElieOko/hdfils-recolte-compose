package com.partners.hdfils_recolte.data.utils


fun isValidPhoneNumber(phoneNumber: String): Boolean {
    // Supprimer le zéro au début si présent
    val normalizedNumber = phoneNumber.removePrefix("0")
    // Expression régulière pour valider le numéro de téléphone
    val regex = Regex("^(99|97|98|85|89|80|84|81|82|83|91|90)\\d{7}$")
    return regex.matches(normalizedNumber)
}
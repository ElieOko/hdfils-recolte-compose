package com.partners.hdfils_recolte.data.utils


class Constants {
    companion object {
        const val BASE_URL = "https://dummyjson.com/"
        const val HOST_DEV = "192.168.2.124:8000/api"
        const val HOST_PROD = "custom.cadeauparfait1.com/api"
        const val IS_PROD = false
        const val TokenLocal = ""
        const val API_INTERNET_MESSAGE = "No Internet Connection"
        val listGenre = listOf("Mr(M)", "Mme(F)")
        val listVille = listOf("Kinshasa", "Boma", "Matadi")
        val listProfession = listOf(
            "Commerçant(e)",
            "Informaticien",
            "Enseignant(e)",
            "Entrepreneur",
            "Consultant(e)",
            "Agent spécifique",
            "Manager",
            "Etudiant(e)",
            "Autres",
        )
        val listDevise = listOf("FC", "USD")
    }
}
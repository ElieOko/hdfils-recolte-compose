package com.partners.hdfils_recolte.presentation.ui.components

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatMoney(amount: Int, devise: String): String {
    val grouped = String.format(Locale.FRANCE, "%,d", amount)
        .replace('\u202F', ' ')
        .replace('\u00A0', ' ')
    val unit = when (devise.trim()) {
        "$", "USD" -> "USD"
        else -> devise.ifBlank { "FC" }
    }
    return "$grouped $unit"
}

fun formatApiDate(iso: String?): String {
    if (iso.isNullOrBlank()) return "—"
    val dateOnly = Regex("""^(\d{4})-(\d{2})-(\d{2})$""").find(iso.trim())
    if (dateOnly != null) {
        val (year, month, day) = dateOnly.destructured
        return "$day/$month/$year"
    }
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val cleaned = iso.substringBefore('.').removeSuffix("Z")
        val date = parser.parse(cleaned) ?: return iso.take(10)
        val out = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)
        out.timeZone = TimeZone.getTimeZone("Africa/Kinshasa")
        out.format(date)
    } catch (error: Exception) {
        iso.take(10)
    }
}

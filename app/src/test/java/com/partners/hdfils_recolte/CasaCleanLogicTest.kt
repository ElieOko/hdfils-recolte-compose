package com.partners.hdfils_recolte

import com.partners.hdfils_recolte.data.utils.isValidPhoneNumber
import com.partners.hdfils_recolte.domain.models.TrashTone
import com.partners.hdfils_recolte.domain.models.splitAmount
import com.partners.hdfils_recolte.domain.models.trashStatus
import com.partners.hdfils_recolte.domain.route.ScreenRoute
import com.partners.hdfils_recolte.domain.route.startRoute
import com.partners.hdfils_recolte.domain.subscription.RegisterDraft
import com.partners.hdfils_recolte.domain.subscription.validateRegister
import com.partners.hdfils_recolte.presentation.ui.components.formatMoney
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CasaCleanLogicTest {
    @Test
    fun loggedInClientSkipsAuth() {
        assertEquals(ScreenRoute.Home, startRoute(loggedIn = true))
    }

    @Test
    fun loggedOutClientOpensAuth() {
        assertEquals(ScreenRoute.Auth, startRoute(loggedIn = false))
    }

    @Test
    fun subscriptionSplitsEvenlyAcrossFourTranches() {
        val parts = splitAmount(24000, 4)
        assertEquals(listOf(1, 2, 3, 4), parts.map { it.numero })
        assertEquals(listOf(6000, 6000, 6000, 6000), parts.map { it.montant })
    }

    @Test
    fun remainderGoesToTheLastTranche() {
        val parts = splitAmount(100, 3)
        assertEquals(listOf(33, 33, 34), parts.map { it.montant })
    }

    @Test
    fun emptySplitWhenAmountOrCountIsMissing() {
        assertTrue(splitAmount(0, 4).isEmpty())
        assertTrue(splitAmount(12000, 0).isEmpty())
    }

    @Test
    fun congoleseMobileNumberIsAccepted() {
        assertTrue(isValidPhoneNumber("0827824163"))
    }

    @Test
    fun registerRequiresATrancheType() {
        val draft = RegisterDraft(
            nom = "Oko",
            prenom = "Elie",
            telephone = "0827824163",
            communeId = 1,
            avenue = "londala",
            quartier = "ngunda lokombe",
            numeroParcelle = "16",
            montant = "24000",
            devise = "FC",
            nombreTranches = 0,
        )
        assertEquals("Choisissez le type de tranche", validateRegister(draft))
        assertNull(validateRegister(draft.copy(nombreTranches = 4)))
    }

    @Test
    fun trashStatesMatchTheClientFlow() {
        assertEquals(TrashTone.Full, trashStatus(3).tone)
        assertEquals("On arrive", trashStatus(3).detail)
        assertEquals(TrashTone.Waiting, trashStatus(2).tone)
        assertEquals(TrashTone.Clean, trashStatus(1).tone)
        assertEquals(TrashTone.Unknown, trashStatus(null).tone)
    }

    @Test
    fun moneyUsesTheSubscriptionCurrency() {
        assertEquals("24 000 FC", formatMoney(24000, "FC"))
        assertEquals("24 000 USD", formatMoney(24000, "USD"))
    }
}

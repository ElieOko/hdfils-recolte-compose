package com.partners.hdfils_recolte.presentation.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity

@Composable
fun AnimatedBackgroundShapes() {
    // Accéder à la densité de l'écran
    val density = LocalDensity.current

    // Convertir dp en px
    val circle1RadiusPx = with(density) { 200.dp.toPx() }
    val circle2RadiusPx = with(density) { 300.dp.toPx() }

    // Animation pour le déplacement et la taille des cercles
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Animation pour le premier cercle
    val circle1OffsetX by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val circle1OffsetY by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val circle1Radius by infiniteTransition.animateFloat(
        initialValue = circle1RadiusPx,
        targetValue = with(density) { 400.dp.toPx() }, // Convertir dp en px
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Animation pour le deuxième cercle
    val circle2OffsetX by infiniteTransition.animateFloat(
        initialValue = 1000f,
        targetValue = -100f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val circle2OffsetY by infiniteTransition.animateFloat(
        initialValue = 1000f,
        targetValue = -100f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val circle2Radius by infiniteTransition.animateFloat(
        initialValue = circle2RadiusPx,
        targetValue = with(density) { 500.dp.toPx() }, // Convertir dp en px
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Dessiner le premier cercle animé
        drawCircle(
            color = Color(0xFF6C63FF).copy(alpha = 0.2f),
            radius = circle1Radius,
            center = Offset(circle1OffsetX, circle1OffsetY)
        )

        // Dessiner le deuxième cercle animé
        drawCircle(
            color = Color(0xFF6C63FF).copy(alpha = 0.2f),
            radius = circle2Radius,
            center = Offset(circle2OffsetX, circle2OffsetY)
        )
    }
}
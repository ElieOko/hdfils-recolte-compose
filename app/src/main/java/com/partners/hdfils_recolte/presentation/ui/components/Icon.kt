package com.partners.hdfils_recolte.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MIcon(icon: ImageVector,color: Color= Color.White){
    Icon(icon, contentDescription = "", tint = color)
}
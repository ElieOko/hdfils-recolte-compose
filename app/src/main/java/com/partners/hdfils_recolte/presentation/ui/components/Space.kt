package com.partners.hdfils_recolte.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Space(x:Int=0,y:Int=0){
    Spacer(Modifier.width(x.dp).height(y.dp))
}
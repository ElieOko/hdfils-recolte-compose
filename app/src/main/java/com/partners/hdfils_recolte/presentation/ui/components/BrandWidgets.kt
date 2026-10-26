package com.partners.hdfils_recolte.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.domain.models.Commune
import com.partners.hdfils_recolte.domain.models.Souscription
import com.partners.hdfils_recolte.domain.models.Tranche
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlue
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDark
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_recolte.presentation.ui.theme.Danger
import com.partners.hdfils_recolte.presentation.ui.theme.Ink
import com.partners.hdfils_recolte.presentation.ui.theme.InkMuted
import com.partners.hdfils_recolte.presentation.ui.theme.Line

@Composable
fun AppLogo(
    size: Dp = 72.dp,
    ring: Color = Color.White,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.logo_casa),
        contentDescription = "CasaClean",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(BorderStroke(2.dp, ring), CircleShape),
    )
}

@Composable
fun BrandWordmark(light: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "CasaClean",
            color = if (light) Color.White else BrandBlueDeep,
            fontWeight = FontWeight.Black,
            fontSize = 30.sp,
            letterSpacing = 0.4.sp,
        )
        Text(
            text = "Votre collecte, votre domicile",
            color = if (light) Color.White.copy(alpha = 0.88f) else InkMuted,
            fontSize = 13.sp,
        )
    }
}

@Composable
fun brandFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BrandBlue,
    unfocusedBorderColor = Line,
    focusedLabelColor = BrandBlue,
    unfocusedLabelColor = InkMuted,
    cursorColor = BrandBlue,
    focusedLeadingIconColor = BrandBlue,
    unfocusedLeadingIconColor = BrandBlueDark,
    focusedTextColor = Ink,
    unfocusedTextColor = Ink,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    errorBorderColor = Danger,
    errorLabelColor = Danger,
    errorCursorColor = Danger,
    errorSupportingTextColor = Danger,
    errorLeadingIconColor = Danger,
)

@Composable
fun AppField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp))
        },
        modifier = modifier
            .fillMaxWidth()
            .focusBringIntoView(),
        singleLine = singleLine,
        isError = isError,
        supportingText = supportingText?.let { message -> { Text(message) } },
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction?.invoke() },
            onNext = { onImeAction?.invoke() },
            onGo = { onImeAction?.invoke() },
        ),
        colors = brandFieldColors(),
    )
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandBlue,
            contentColor = Color.White,
            disabledContainerColor = BrandBlue.copy(alpha = 0.35f),
            disabledContentColor = Color.White,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
        } else {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun DeviseSelector(
    selected: String,
    onSelect: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Devise",
            color = Ink,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DeviseChip(
                label = "Franc congolais",
                value = "FC",
                selected = selected == "FC",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("FC") },
            )
            DeviseChip(
                label = "Dollar",
                value = "USD",
                selected = selected == "USD",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("USD") },
            )
        }
    }
}

@Composable
private fun DeviseChip(
    label: String,
    value: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) BrandBlue else Color.White,
        border = BorderStroke(1.dp, if (selected) BrandBlue else Line),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                color = if (selected) Color.White else BrandBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Text(
                text = label,
                color = if (selected) Color.White.copy(alpha = 0.9f) else InkMuted,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableCommuneField(
    resetKey: Int,
    selectedId: Int,
    onSelect: (Commune) -> Unit,
) {
    val communes = remember { Commune().communeList() }
    var query by remember(resetKey) { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val filtered = remember(query, selectedId, communes) {
        val selectedName = communes.firstOrNull { it.id == selectedId }?.nom
        val needle = query.trim()
        if (needle.isEmpty() || needle.equals(selectedName, ignoreCase = true)) {
            communes
        } else {
            communes.filter { it.nom.contains(needle, ignoreCase = true) }
        }
    }
    val selected = communes.firstOrNull { it.id == selectedId }

    Column(modifier = Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    expanded = true
                    if (selected != null && !it.equals(selected.nom, ignoreCase = true)) {
                        onSelect(Commune())
                    }
                },
                label = { Text("Rechercher une commune") },
                placeholder = { Text("Ex. Ngaliema, Lemba…") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlue)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = brandFieldColors(),
                textStyle = TextStyle(color = Ink),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                    .focusBringIntoView(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                if (filtered.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Aucune commune trouvée", color = InkMuted) },
                        onClick = { expanded = false },
                        enabled = false,
                    )
                } else {
                    filtered.forEach { commune ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    commune.nom,
                                    color = Ink,
                                    fontWeight = if (commune.id == selectedId) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            leadingIcon = if (commune.id == selectedId) {
                                {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = BrandBlue)
                                }
                            } else {
                                null
                            },
                            onClick = {
                                query = commune.nom
                                expanded = false
                                onSelect(commune)
                            },
                        )
                    }
                }
            }
        }
        if (selected != null) {
            Spacer(Modifier.height(8.dp))
            Surface(
                color = BrandBlueLight,
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(
                    text = "Commune : ${selected.nom}",
                    color = BrandBlueDeep,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
fun SubscriptionBlock(
    souscription: Souscription,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = formatMoney(souscription.montant_souscription, souscription.devise),
            color = BrandBlueDeep,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
        Text(
            text = souscription.tranche.ifBlank {
                val count = souscription.nombre_tranches
                if (count <= 0) "Tranche à définir" else "$count tranche${if (count > 1) "s" else ""}"
            },
            color = InkMuted,
            fontSize = 14.sp,
        )
        if (souscription.tranches.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            souscription.tranches.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    row.forEach { tranche ->
                        TrancheChip(
                            tranche = tranche,
                            devise = souscription.devise,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (row.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TrancheChip(
    tranche: Tranche,
    devise: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = BrandBlueLight,
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(
                text = "Tranche ${tranche.numero}",
                color = InkMuted,
                fontSize = 11.sp,
            )
            Text(
                text = formatMoney(tranche.montant, devise),
                color = BrandBlueDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(BrandBlueLight, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text(label, color = InkMuted, fontSize = 12.sp)
            Text(
                text = value.ifBlank { "—" },
                color = Ink,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
            )
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        color = Ink,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
    )
}

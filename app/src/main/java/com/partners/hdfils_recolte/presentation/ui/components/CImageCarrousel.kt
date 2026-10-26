package com.partners.hdfils_recolte.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.partners.hdfils_recolte.R
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlue
import com.partners.hdfils_recolte.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_recolte.presentation.ui.theme.Line
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

data class PromoSlide(
    @DrawableRes val image: Int,
    val title: String,
    val caption: String,
)

fun casaSlides(): List<PromoSlide> = listOf(
    PromoSlide(
        image = R.drawable.promo_services,
        title = "Tous vos services, une seule application",
        caption = "Plomberie, électricité, ménage et le suivi de votre domicile.",
    ),
    PromoSlide(
        image = R.drawable.promo_app,
        title = "CasaClean dans votre poche",
        caption = "Des professionnels fiables, tout près de chez vous.",
    ),
    PromoSlide(
        image = R.drawable.promo_collecte,
        title = "Collecte des déchets à domicile",
        caption = "Une poubelle à la maison, un ramassage jusqu'à votre parcelle.",
    ),
)

@Composable
fun CasaCarousel(
    slides: List<PromoSlide> = casaSlides(),
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { slides.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(4600)
            if (!pagerState.isScrollInProgress && slides.isNotEmpty()) {
                val next = (pagerState.currentPage + 1) % slides.size
                pagerState.animateScrollToPage(
                    page = next,
                    animationSpec = tween(durationMillis = 880, easing = FastOutSlowInEasing),
                )
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 14.dp,
            contentPadding = PaddingValues(horizontal = 28.dp),
        ) { page ->
            val offset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val distance = offset.absoluteValue.coerceIn(0f, 1f)
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(286.dp)
                    .graphicsLayer {
                        val scale = lerp(0.9f, 1f, 1f - distance)
                        scaleX = scale
                        scaleY = scale
                        alpha = lerp(0.62f, 1f, 1f - distance)
                        rotationY = offset * -16f
                        cameraDistance = 18f * density
                    },
            ) {
                Box(Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(slides[page].image),
                        contentDescription = slides[page].title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = offset * 36.dp.toPx()
                                scaleX = 1.08f
                                scaleY = 1.08f
                            },
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(132.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, BrandBlueDeep.copy(alpha = 0.94f)),
                                ),
                            ),
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                    ) {
                        Text(
                            text = slides[page].title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                        )
                        Text(
                            text = slides[page].caption,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            slides.indices.forEach { index ->
                val selected = pagerState.currentPage == index
                val width by animateDpAsState(
                    targetValue = if (selected) 22.dp else 8.dp,
                    animationSpec = tween(320),
                    label = "indicator-$index",
                )
                Box(
                    modifier = Modifier
                        .size(width = width, height = 8.dp)
                        .clip(CircleShape)
                        .background(if (selected) BrandBlue else Line)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = index,
                                    animationSpec = tween(520, easing = FastOutSlowInEasing),
                                )
                            }
                        },
                )
            }
        }
    }
}

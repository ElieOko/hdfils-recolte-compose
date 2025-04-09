package com.partners.hdfils_recolte.presentation.ui.components

import android.R.attr.scaleY
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.partners.hdfils_recolte.R
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier
) {
    val imageList = listOf(
        R.drawable.sac,
        R.drawable.affiche,
        R.drawable.detergeant,
        R.drawable.poubelle,
        R.drawable.car,
    )

    val pagerState = rememberPagerState { imageList.size }

    Column(
        modifier
            .defaultMinSize(minHeight = 300.dp)
            .fillMaxWidth()
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 10.dp,
            contentPadding = PaddingValues(horizontal = 30.dp)
        ) { page ->
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .graphicsLayer {
                        val pageOffset =
                            (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue

                        lerp(
                            start = 75.dp,
                            stop = 100.dp,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleY = scale / 100.dp
                        }
                    },
                contentScale = ContentScale.Crop
            )
        }

    }

}

@Composable
@Preview
fun ModernCarousel(
    imageList: List<Int> = listOf<Int>( R.drawable.sac,
    R.drawable.affiche,
    R.drawable.detergeant,
    R.drawable.poubelle,
    R.drawable.car,),
    texts: List<String> = listOf<String>("Propreté et Écologie : Ensemble pour un Avenir Durable ","Kin ezo bonga","Propreté et Style : Faites le Choix Intelligent ","Votre Allié pour un Quotidien Impeccable","Optez pour la Propreté : Un Choix Responsable ")) {
    val pagerState: PagerState = rememberPagerState { imageList.size }

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000) // Délai de 3 secondes
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % imageList.size)
        }
    }

    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = 300.dp)
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 10.dp,
            contentPadding = PaddingValues(horizontal = 30.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = imageList[page]),
                        contentDescription = texts[page],
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset = (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue
                                lerp(
                                    start = 75.dp,
                                    stop = 100.dp,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleY = scale / 100.dp
                                    scaleX = scale / 100.dp
                                }
                            },
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(70.dp)
                            .fillMaxWidth()
                            .background(Color.Black.copy(0.5f))
                            .padding(13.dp)
                    ){
                        Text(
                            text = texts[page],
                            modifier = Modifier
                                .padding(2.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }

        // Indicateurs de page
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        ) {
            repeat(imageList.size) { index ->
                val color = if (pagerState.currentPage == index) Color.White else Color.Gray
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, shape = RectangleShape)
                        .padding(2.dp)
                )
                Space(x = 5)
            }
        }
    }
}
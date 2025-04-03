package com.dalmuina.pruebamango.heroes.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dalmuina.pruebamango.heroes.presentation.HeroListAction
import com.dalmuina.pruebamango.heroes.presentation.components.MainImage
import com.dalmuina.pruebamango.heroes.presentation.components.MainTopBar
import com.dalmuina.pruebamango.heroes.presentation.state.HeroDetailState
import com.dalmuina.pruebamango.heroes.presentation.viewmodel.HeroesViewModel
import com.dalmuina.pruebamango.ui.theme.primaryContainerDark

@Composable
fun DetailViewWrapper(
    viewModel: HeroesViewModel,
    id: Int,
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit
) {
    val detail by viewModel.detail.collectAsStateWithLifecycle()
    DetailViewScreen(
        detail = detail,
        modifier = modifier,
        id = id,
        onAction = {action ->
            when(action){
                HeroListAction.OnBackButtonClick -> {
                    viewModel.onAction(action)
                    onClickBack
                }
                is HeroListAction.OnLoadHeroDetail -> {
                    viewModel.onAction(action)
                }
                else -> Unit
            }
        }
    )
}

@Composable
fun DetailViewScreen(
    detail: HeroDetailState,
    modifier: Modifier,
    id: Int,
    onAction: (HeroListAction)->Unit
) {
    LaunchedEffect(Unit) {
        onAction(HeroListAction.OnLoadHeroDetail(id))
    }

    if (detail.isLoading){
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            modifier = modifier,
            topBar = {
                MainTopBar (
                    title = detail.heroDetailUi.name,
                    showBackButton = true,
                    onClickBackButton = { onAction(HeroListAction.OnBackButtonClick) }
                )
            }
        ) {
            ContentDetailView(it, detail)
        }
    }
}

@Composable
fun ContentDetailView(
    pad: PaddingValues,
    detail: HeroDetailState
) {
    Column(
        modifier = Modifier
            .padding(pad)
            .background(primaryContainerDark)
    ) {
        with(detail.heroDetailUi){
            MainImage(imageUrl = backgroundImage)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 5.dp)
            ) {
                //TODO extra informacion
            }

            val scroll = rememberScrollState(0)
            Text(
                text = detail.heroDetailUi.descriptionRaw,
                color = Color.White,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
                    .verticalScroll(scroll)
            )
        }
    }
}

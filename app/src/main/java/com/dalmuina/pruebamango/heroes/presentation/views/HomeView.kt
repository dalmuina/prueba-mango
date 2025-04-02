package com.dalmuina.pruebamango.heroes.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.dalmuina.pruebamango.heroes.presentation.components.MainTopBar
import com.dalmuina.pruebamango.heroes.presentation.model.HeroUi

@Composable
fun HomeViewWrapper(
    modifier: Modifier = Modifier
) {

}

@Composable
fun HomeViewScreen(
    heroesPagingItems: LazyPagingItems<HeroUi>,
    modifier: Modifier = Modifier
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            MainTopBar(
                title = "Mango App",
                onClickBackButton = {}
            )
        }
    ){padding->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeroListContent(
                heroesPagingItems
            )
        }
    }
}

@Composable
fun HeroListContent(
    heroesPagingItems: LazyPagingItems<HeroUi>
) {

}
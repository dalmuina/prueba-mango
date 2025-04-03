package com.dalmuina.pruebamango.heroes.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dalmuina.pruebamango.heroes.presentation.components.CardHero
import com.dalmuina.pruebamango.heroes.presentation.components.ErrorItem
import com.dalmuina.pruebamango.heroes.presentation.components.Loader
import com.dalmuina.pruebamango.heroes.presentation.components.MainTopBar
import com.dalmuina.pruebamango.heroes.presentation.components.ShimmerListItem
import com.dalmuina.pruebamango.heroes.presentation.model.HeroUi
import com.dalmuina.pruebamango.heroes.presentation.viewmodel.HeroesViewModel
import com.dalmuina.pruebamango.ui.theme.primaryContainerDark

@Composable
fun HomeViewWrapper(
    viewModel: HeroesViewModel,
    modifier: Modifier = Modifier
) {
    val heroesPagingItems = viewModel.heroesPagingFlow.collectAsLazyPagingItems()
    HomeViewScreen(
        heroesPagingItems = heroesPagingItems,
        modifier = modifier)
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
    val isLoading = heroesPagingItems.loadState.refresh is LoadState.Loading
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background(primaryContainerDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ){
        if (isLoading && heroesPagingItems.itemCount == 0) {
            items(5){
                ShimmerListItem(
                    isLoading = true,
                    contentAfterLoading = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            items(heroesPagingItems.itemCount) {index ->
                val hero = heroesPagingItems[index]
                ShimmerListItem(
                    isLoading = hero == null,
                    contentAfterLoading = {
                        if (hero != null) {
                            CardHero(hero, onClick = {})
                            Text(
                                text = hero.name,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
        heroesPagingItems.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item { Loader() }
                }
                loadState.refresh is LoadState.Error -> {
                    item { ErrorItem(onRetry = { retry() }) }
                }
                loadState.append is LoadState.Error -> {
                    item { ErrorItem(onRetry = { retry() }) }
                }
            }
        }
    }
}

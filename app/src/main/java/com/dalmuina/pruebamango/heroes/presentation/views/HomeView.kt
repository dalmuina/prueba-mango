package com.dalmuina.pruebamango.heroes.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dalmuina.pruebamango.heroes.presentation.HeroListAction
import com.dalmuina.pruebamango.heroes.presentation.components.CardHero
import com.dalmuina.pruebamango.heroes.presentation.components.ErrorItem
import com.dalmuina.pruebamango.heroes.presentation.components.Loader
import com.dalmuina.pruebamango.heroes.presentation.components.MainTopBar
import com.dalmuina.pruebamango.heroes.presentation.components.ShimmerListItem
import com.dalmuina.pruebamango.heroes.presentation.model.HeroUi
import com.dalmuina.pruebamango.heroes.presentation.navigation.Detail
import com.dalmuina.pruebamango.heroes.presentation.viewmodel.HeroesViewModel
import com.dalmuina.pruebamango.ui.theme.primaryContainerDark

@Composable
fun HomeViewWrapper(
    viewModel: HeroesViewModel,
    modifier: Modifier = Modifier,
    onClickDetail:(Detail)->Unit
) {

    val filter = viewModel.filter.collectAsStateWithLifecycle()

    val heroesPagingItems = viewModel.heroesPagingFlow.collectAsLazyPagingItems()

    HomeViewScreen(
        heroesPagingItems = heroesPagingItems,
        filter = filter.value,
        modifier = modifier,
        onAction = {action ->
            when(action) {
                is HeroListAction.OnLoadHeroDetail -> {
                    onClickDetail(Detail(action.id))
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun HomeViewScreen(
    heroesPagingItems: LazyPagingItems<HeroUi>,
    filter: String,
    modifier: Modifier = Modifier,
    onAction:(HeroListAction)->Unit
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
            FilterField(
                value = filter,
                onFilterChange = {
                    onAction(HeroListAction.OnFilterChange(it)) },
                onFilter = {})
            HeroListContent(
                heroesPagingItems,
                onItemClick = {hero->
                    onAction(HeroListAction.OnLoadHeroDetail(hero.id))
                }
            )
        }
    }
}

@Composable
fun HeroListContent(
    heroesPagingItems: LazyPagingItems<HeroUi>,
    onItemClick: (HeroUi) -> Unit
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
                            CardHero(hero, onClick = {onItemClick(hero)})
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

@Composable
fun FilterField(
    value: String,
    onFilterChange: (String) -> Unit,
    onFilter: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onFilterChange,
        label = { Text("Filter") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onFilter() }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

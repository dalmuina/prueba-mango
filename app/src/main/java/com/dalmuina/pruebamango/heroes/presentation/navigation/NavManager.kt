package com.dalmuina.pruebamango.heroes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dalmuina.pruebamango.heroes.presentation.viewmodel.HeroesViewModel
import com.dalmuina.pruebamango.heroes.presentation.views.DetailViewWrapper
import com.dalmuina.pruebamango.heroes.presentation.views.HomeViewWrapper
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavManager(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val viewModel = koinViewModel<HeroesViewModel>()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home>{
            HomeViewWrapper(
                viewModel= viewModel,
                modifier= modifier
            )
        }
        composable<Detail>{backStackEntry->
            val detail = backStackEntry.toRoute<Detail>()
            DetailViewWrapper(
                modifier = modifier
            )
        }
    }
}

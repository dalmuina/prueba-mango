package com.dalmuina.pruebamango.heroes.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dalmuina.pruebamango.PruebaMangoApp
import com.dalmuina.pruebamango.ui.theme.PruebaMangoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    showBackButton: Boolean = false,
    onClickBackButton: () -> Unit
) {
    TopAppBar(
        title ={ Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.ExtraBold) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            if(showBackButton){
                IconButton(onClick = {onClickBackButton()}) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                        , tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
fun MainTopBarPreviewBack(){
    PruebaMangoTheme {
        MainTopBar(
            title = "MANGO",
            true,
            onClickBackButton = {})
    }
}

@PreviewLightDark
@Composable
fun MainTopBarPreviewMain(){
    PruebaMangoTheme {
        MainTopBar(
            title = "MANGO",
            false,
            onClickBackButton = {})
    }
}

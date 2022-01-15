package com.p4r4d0x.skintker.presenter.home.view.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SettingScreen(onBackIconPressed: () -> Unit) {

    Scaffold(
        topBar = {
            SettingsTopBar(onBackIconPressed)
        }
    ) {
        SettingScreenContent()


    }
}

@Composable
fun SettingsTopBar(onBackIconPressed: () -> Unit) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp
    ) {

        //TopAppBar Content
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = "Hello"
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    onBackIconPressed()
                },
                enabled = true,
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    }
}

@Composable
fun SettingScreenContent() {

    Column(Modifier.fillMaxSize()) {
        Text("hola")

    }
}
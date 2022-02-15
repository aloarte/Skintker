package com.p4r4d0x.skintker.presenter.settings.view.compose

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.enums.SettingsStatus
import com.p4r4d0x.skintker.presenter.common.compose.SkintkerDivider


@Composable
fun SettingScreen(
    prefs: SharedPreferences?,
    onBackIconPressed: () -> Unit,
    onExportPressed: () -> Unit,
    onImportPressed: () -> Unit,
    settingsCallback: (SettingsStatus) -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopBar(onBackIconPressed)
        }
    ) {
        SettingScreenContent(prefs, onExportPressed, onImportPressed, settingsCallback)
    }
}

@Composable
fun SettingsTopBar(
    onBackIconPressed: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp
    ) {
        //TopAppBar Content
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.h6,
                    text = stringResource(id = R.string.screen_settings)
                )
            }
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingScreenContent(
    prefs: SharedPreferences?,
    onExportPressed: () -> Unit,
    onImportPressed: () -> Unit,
    settingsCallback: (SettingsStatus) -> Unit
) {

    LazyColumn {
        item {
            Column(Modifier.fillMaxSize()) {
                ParametersConfiguration(prefs, settingsCallback)
                SkintkerDivider()
                val multiplePermissionsState =
                    rememberMultiplePermissionsState(
                        listOf(
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    )
                when {
                    // If all permissions are granted, then show the question
                    multiplePermissionsState.allPermissionsGranted -> {
                        CSVButtons(onExportPressed, onImportPressed)
                    }
                    multiplePermissionsState.shouldShowRationale -> {
                        PermissionsRationale(multiplePermissionsState = multiplePermissionsState) {

                        }
                    }
                    // If the criteria above hasn't been met, the user denied some permission, but show the question
                    else -> {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                            text = stringResource(id = R.string.file_rationale_no_permissions),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                }
            }
        }
    }
}
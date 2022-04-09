package com.p4r4d0x.skintker.presenter.settings.view.compose

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.p4r4d0x.skintker.presenter.common.compose.Description
import com.p4r4d0x.skintker.presenter.common.compose.SkintkerDivider
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel


@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel,
    prefs: SharedPreferences?,
    onBackIconPressed: () -> Unit,
    onExportPressed: () -> Unit,
    onLogoutPressed: () -> Unit,
    onAlarmPressed: () -> Unit,
    settingsCallback: (SettingsStatus) -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopBar(onBackIconPressed)
        }
    ) {
        SettingScreenContent(
            settingsViewModel,
            prefs,
            onExportPressed,
            onLogoutPressed,
            onAlarmPressed,
            settingsCallback
        )
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
    settingsViewModel: SettingsViewModel,
    prefs: SharedPreferences?,
    onExportPressed: () -> Unit,
    onLogoutPressed: () -> Unit,
    onAlarmPressed: () -> Unit,
    settingsCallback: (SettingsStatus) -> Unit
) {
    LazyColumn {
        item {
            Column(Modifier.fillMaxSize()) {
                ProfileSection(settingsViewModel, onLogoutPressed)
                SkintkerDivider()
                AlarmSection(settingsViewModel, onAlarmPressed)
                SkintkerDivider()
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
                        CSVButtons(onExportPressed)
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

@Composable
fun AlarmSection(viewModel: SettingsViewModel, onAlarmPressed: () -> Unit) {
    val inputText: String = viewModel.reminderTime.collectAsState().value
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (inputText != "") {
            AlarmDescription(inputText)
        } else {
            Description(R.string.settings_notification_description)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            enabled = true,
            modifier = Modifier
                .height(40.dp),
            onClick = { onAlarmPressed() }
        ) {
            Text(
                text = stringResource(
                    id = if (inputText != "") {
                        R.string.btn_notification_update
                    } else {
                        R.string.btn_notification_create
                    }
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

    }
}

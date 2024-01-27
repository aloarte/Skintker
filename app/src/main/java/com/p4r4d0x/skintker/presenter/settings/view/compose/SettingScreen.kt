package com.p4r4d0x.skintker.presenter.settings.view.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.common.compose.CustomDialog
import com.p4r4d0x.skintker.presenter.common.compose.DeleteDialogAllContent
import com.p4r4d0x.skintker.presenter.common.compose.Description
import com.p4r4d0x.skintker.presenter.common.compose.SkintkerDivider
import com.p4r4d0x.skintker.presenter.common.utils.DialogsData
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel

@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel,
    onBackIconPressed: () -> Unit,
    onExportPressed: () -> Unit,
    onLogoutPressed: () -> Unit,
    onRemoveLogsPressed: () -> Unit,
    onAlarmPressed: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopBar(onBackIconPressed)
        }
    ) {
        SettingScreenContent(
            it,
            settingsViewModel,
            onLogoutPressed,
            onRemoveLogsPressed,
            onAlarmPressed

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
    paddingValues: PaddingValues,
    settingsViewModel: SettingsViewModel,
    onLogoutPressed: () -> Unit,
    onRemoveLogsPressed: () -> Unit,
    onAlarmPressed: (Boolean) -> Unit
) {

    val showDeleteDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val showLogoutDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val showAlarmSection: MutableState<Boolean> = remember {
        mutableStateOf(true)
    }
    LazyColumn(contentPadding = paddingValues) {
        item {
            StateDeleteDialog(showDeleteDialog, onRemoveLogsPressed)
            StateLogoutDialog(showLogoutDialog, onLogoutPressed)
            Column(Modifier.fillMaxSize()) {
                ProfileSection(settingsViewModel) {
                    showLogoutDialog.value = true
                }
                SkintkerDivider()
                RemoveLogsSection {
                    showDeleteDialog.value = true
                }
                SkintkerDivider()
                if(showAlarmSection.value){
                    AlarmPermissions(settingsViewModel, onAlarmPressed){
                        showAlarmSection.value = false
                    }
                }

            }
        }
    }
}

@Composable
fun StateLogoutDialog(showLogoutDialog: MutableState<Boolean>, onLogoutPressed: () -> Unit) {
    val dialogData = DialogsData(
        titleRes = R.string.dialog_title_logout,
        okButtonRes = R.string.dialog_logout_ok,
        descriptionRes = R.string.dialog_description_logout
    )

    if (showLogoutDialog.value) {
        CustomDialog(dialogData, { DeleteDialogAllContent(dialogData) }) { confirm ->
            if (confirm) {
                onLogoutPressed()
            }
            showLogoutDialog.value = false
        }
    }
}

@Composable
fun StateDeleteDialog(showDeleteDialog: MutableState<Boolean>, onRemoveLogsPressed: () -> Unit) {
    val dialogData = DialogsData(
        titleRes = R.string.dialog_title_all,
        okButtonRes = R.string.dialog_all_ok,
        descriptionRes = R.string.dialog_description_all
    )

    if (showDeleteDialog.value) {
        CustomDialog(dialogData, { DeleteDialogAllContent(dialogData) }) { confirm ->
            if (confirm) {
                onRemoveLogsPressed()
            }
            showDeleteDialog.value = false
        }
    }
}

@Composable
fun RemoveLogsSection(onRemoveLogsPressed: () -> Unit) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Description(R.string.settings_wipe_data_description)
        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Button(
                enabled = true,
                modifier = Modifier
                    .height(40.dp),
                onClick = {
                    onRemoveLogsPressed()
                }
            ) {
                Text(text = stringResource(R.string.btn_wipe_data))
            }
            Divider(
                modifier = Modifier.width(20.dp),
                color = Color.Transparent
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

}

@Composable
fun AlarmSection(viewModel: SettingsViewModel, onAlarmPressed: (Boolean) -> Unit) {
    val inputText: String = viewModel.reminderTime.collectAsState().value
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (inputText != "") {
            AlarmDescription(inputText)
        } else {
            Description(R.string.settings_notification_description)
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (inputText != "") {
            Row {
                Button(
                    enabled = true,
                    modifier = Modifier
                        .height(40.dp),
                    onClick = { onAlarmPressed(true) }
                ) {
                    Text(text = stringResource(R.string.btn_notification_update))
                }
                Divider(
                    modifier = Modifier.width(20.dp),
                    color = Color.Transparent
                )
                Button(
                    enabled = true,
                    modifier = Modifier
                        .height(40.dp),
                    onClick = { onAlarmPressed(false) }
                ) {
                    Text(text = stringResource(R.string.btn_notification_clear))
                }
            }
        } else {
            Button(
                enabled = true,
                modifier = Modifier
                    .height(40.dp),
                onClick = { onAlarmPressed(true) }
            ) {
                Text(text = stringResource(R.string.btn_notification_create))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

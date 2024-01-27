package com.p4r4d0x.skintker.presenter.settings.view.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.p4r4d0x.domain.bo.ProfileBO
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.common.compose.Description
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel

@Composable
fun CSVButtons(
    onExportPressed: () -> Unit
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Description(R.string.settings_export_description)
        Button(
            enabled = true,
            modifier = Modifier
                .height(40.dp),
            onClick = { onExportPressed() }
        ) {
            Text(text = stringResource(id = R.string.btn_export_data))
        }
    }
}

@Composable
fun ProfileSection(
    settingsViewModel: SettingsViewModel,
    onLogoutPressed: () -> Unit
) {
    settingsViewModel.profile.observeAsState().value?.let { profile ->
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            if(profile.isAnonymous){
                AnonymousUser()
            }else{
                AuthenticatedUser(profile as ProfileBO.AuthenticatedProfileBO)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                enabled = true,
                modifier = Modifier
                    .height(40.dp),
                onClick = { onLogoutPressed() }
            ) {
                Text(text = stringResource(id = R.string.btn_logout))
            }
            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

@Composable
fun AnonymousUser(){
    Description(R.string.settings_anon_profile_description)
}

@Composable
fun AuthenticatedUser(profile: ProfileBO.AuthenticatedProfileBO){
    Description(R.string.settings_profile_description)
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = profile.email,
            style = MaterialTheme.typography.caption
        )

        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = profile.name,
            style = MaterialTheme.typography.caption
        )

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsRationale(
    multiplePermissionsState: MultiplePermissionsState,
    onDoNotAskForPermissions: () -> Unit
) {
    Card(
        modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary)
    ) {
        Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = 25.dp)) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.file_rationale_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.file_rationale),
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.caption,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = {
                    multiplePermissionsState.launchMultiplePermissionRequest()
                }
            ) {
                Text(stringResource(R.string.request_permissions))
            }
            Spacer(modifier = Modifier.height(2.dp))
            OutlinedButton(onClick = onDoNotAskForPermissions) {
                Text(stringResource(R.string.do_not_ask_permissions))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsRationaleAlarm(
    multiplePermissionsState: MultiplePermissionsState,
    onDoNotAskForPermissions: () -> Unit
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Description(R.string.settings_notification_description)

        Card(
            modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary)
        ) {
            Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = 25.dp)) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.notifications_rationale_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.notifications_rationale),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.caption,
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = {
                        multiplePermissionsState.launchMultiplePermissionRequest()
                    }
                ) {
                    Text(stringResource(R.string.request_permissions))
                }
                Spacer(modifier = Modifier.height(2.dp))
                OutlinedButton(onClick = onDoNotAskForPermissions) {
                    Text(stringResource(R.string.do_not_ask_permissions))
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

}


@Composable
fun AlarmDescription(reminderTime: String) {
    Text(
        stringResource(id = R.string.settings_notification_reminder_description, reminderTime),
        fontSize = 10.sp,
        fontWeight = FontWeight.Light,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 30.dp)
    )
}


@ExperimentalPermissionsApi
@Composable
fun AlarmPermissions(
    viewModel: SettingsViewModel,
    onAlarmPressed: (Boolean) -> Unit,
    onDoNotAskForPermissions: ()->Unit
) {

    val multiplePermissionsState = rememberMultiplePermissionsState(listOf(
        android.Manifest.permission.POST_NOTIFICATIONS,
    ))

    when {
        //The first time: user didn't denied and the permissions ain't granted yet
        !multiplePermissionsState.shouldShowRationale && !multiplePermissionsState.allPermissionsGranted -> {
            PermissionsRationaleAlarm(
                multiplePermissionsState = multiplePermissionsState,
                onDoNotAskForPermissions = onDoNotAskForPermissions
            )
        }
        //If the user denied the permission explicitly from the android permission mgr
        multiplePermissionsState.shouldShowRationale -> {
            PermissionsRationaleAlarm(
                multiplePermissionsState = multiplePermissionsState,
                onDoNotAskForPermissions = onDoNotAskForPermissions
            )
        }
        // If all permissions are granted, then show the question
        multiplePermissionsState.allPermissionsGranted -> {
            AlarmSection(viewModel,onAlarmPressed)
        }
    }
}


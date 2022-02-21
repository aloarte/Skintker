package com.p4r4d0x.skintker.presenter.settings.view.compose

import android.content.SharedPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.enums.SettingsStatus
import com.p4r4d0x.skintker.presenter.common.compose.Description
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel


@Composable
fun ParametersConfiguration(prefs: SharedPreferences?, settingsCallback: (SettingsStatus) -> Unit) {
    val minLogsRange = 3..30 step 3
    val thresholdRange = 25..75 step 5
    val levelRange = 1..10

    Description(R.string.settings_parameters_description)

    prefs?.let { preference ->
        var irritationLevelValue by remember {
            mutableStateOf(
                preference.getInt(
                    Constants.PREFERENCES_IRRITATION_NUMBER,
                    Constants.DEFAULT_IRRITATION_LEVEL_THRESHOLD
                )
            )
        }
        var minLogsValue by remember {
            mutableStateOf(
                preference.getInt(
                    Constants.PREFERENCES_MIN_LOGS,
                    Constants.DEFAULT_MIN_LOGS
                )
            )
        }
        var foodThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    Constants.PREFERENCES_FOOD_THRESHOLD,
                    Constants.DEFAULT_FOOD_THRESHOLD
                ) * 100).toInt()
            )
        }
        var zonesThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    Constants.PREFERENCES_ZONES_THRESHOLD,
                    Constants.DEFAULT_ZONES_THRESHOLD
                ) * 100).toInt()
            )
        }
        var alcoholThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    Constants.PREFERENCES_ALCOHOL_THRESHOLD,
                    Constants.DEFAULT_ALCOHOL_THRESHOLD
                ) * 100).toInt()
            )
        }
        var travelThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    Constants.PREFERENCES_TRAVEL_THRESHOLD,
                    Constants.DEFAULT_TRAVEL_THRESHOLD
                ) * 100).toInt()
            )
        }
        var temperatureThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    Constants.PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD,
                    Constants.DEFAULT_WEATHER_TEMPERATURE_THRESHOLD
                ) * 100).toInt()
            )
        }
        var humidityThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    Constants.PREFERENCES_WEATHER_HUMIDITY_THRESHOLD,
                    Constants.DEFAULT_WEATHER_HUMIDITY_THRESHOLD
                ) * 100).toInt()
            )
        }
        var stressLevelValue by remember {
            mutableStateOf(
                preference.getInt(
                    Constants.PREFERENCES_STRESS_VALUE,
                    Constants.DEFAULT_STRESS_VALUE
                )
            )
        }
        var stressThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    Constants.PREFERENCES_STRESS_THRESHOLD,
                    Constants.DEFAULT_STRESS_THRESHOLD
                ) * 100).toInt()
            )
        }

        Description(R.string.settings_parameters_description_levels)
        //Irritation level
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_irritation)
            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = irritationLevelValue,
                    range = levelRange,
                    onValueChange = {
                        irritationLevelValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        //Min logs number
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_min_logs)
            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = minLogsValue,
                    range = minLogsRange,
                    onValueChange = {
                        minLogsValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        Description(R.string.settings_parameters_description_thresholds)
        //Food threshold
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_food_threshold)
            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = foodThresholdValue,
                    range = thresholdRange,
                    onValueChange = {
                        foodThresholdValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        //Zones threshold
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_zones_threshold)
            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = zonesThresholdValue,
                    range = thresholdRange,
                    onValueChange = {
                        zonesThresholdValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        //Alcohol threshold
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_alcohol_threshold)
            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = alcoholThresholdValue,
                    range = thresholdRange,
                    onValueChange = {
                        alcoholThresholdValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        //Travel threshold
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_travel_threshold)

            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = travelThresholdValue,
                    range = thresholdRange,
                    onValueChange = {
                        travelThresholdValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        //Weather temperature threshold
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_weather_temperature_threshold)

            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = temperatureThresholdValue,
                    range = thresholdRange,
                    onValueChange = {
                        temperatureThresholdValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        //Weather humidity threshold
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_weather_humidity_threshold)

            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = humidityThresholdValue,
                    range = thresholdRange,
                    onValueChange = {
                        humidityThresholdValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        Description(R.string.settings_parameters_description_stress)
        //Stress level
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_stress_value)
            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = stressLevelValue,
                    range = levelRange,
                    onValueChange = {
                        stressLevelValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        //Stress threshold
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.settings_param_stress_threshold)

            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NumberPicker(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = stressThresholdValue,
                    range = thresholdRange,
                    onValueChange = {
                        stressThresholdValue = it
                    },
                    textStyle = MaterialTheme.typography.caption
                )
            }
        }
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                enabled = true,
                modifier = Modifier
                    .height(40.dp),
                onClick = {
                    setPreferenceValues(
                        prefs,
                        irritationLevelValue,
                        minLogsValue,
                        foodThresholdValue,
                        zonesThresholdValue,
                        alcoholThresholdValue,
                        travelThresholdValue,
                        temperatureThresholdValue,
                        humidityThresholdValue,
                        stressLevelValue,
                        stressThresholdValue,
                        settingsCallback
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.btn_save_logs))
            }
        }
        Divider(Modifier.height(20.dp), color = Color.Transparent)
    } ?: run {
        settingsCallback(SettingsStatus.ErrorLoadPreferences)
    }
}

fun setPreferenceValues(
    prefs: SharedPreferences?,
    irritationLevelValue: Int,
    minLogsValue: Int,
    foodThresholdValue: Int,
    zonesThresholdValue: Int,
    alcoholThresholdValue: Int,
    travelThresholdValue: Int,
    temperatureThresholdValue: Int,
    humidityThresholdValue: Int,
    stressLevelValue: Int,
    stressThresholdValue: Int,
    settingsCallback: (SettingsStatus) -> Unit
) {
    val editor: SharedPreferences.Editor? = prefs?.edit()
    editor?.let {
        it.putInt(Constants.PREFERENCES_IRRITATION_NUMBER, irritationLevelValue)
        it.putInt(Constants.PREFERENCES_MIN_LOGS, minLogsValue)
        it.putFloat(Constants.PREFERENCES_FOOD_THRESHOLD, foodThresholdValue.toFloat() / 100f)
        it.putFloat(Constants.PREFERENCES_ZONES_THRESHOLD, zonesThresholdValue.toFloat() / 100f)
        it.putFloat(Constants.PREFERENCES_ALCOHOL_THRESHOLD, alcoholThresholdValue.toFloat() / 100f)
        it.putFloat(Constants.PREFERENCES_TRAVEL_THRESHOLD, travelThresholdValue.toFloat() / 100f)
        it.putFloat(
            Constants.PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD,
            temperatureThresholdValue.toFloat() / 100f
        )
        it.putFloat(
            Constants.PREFERENCES_WEATHER_HUMIDITY_THRESHOLD,
            humidityThresholdValue.toFloat() / 100f
        )
        it.putInt(Constants.PREFERENCES_STRESS_VALUE, stressLevelValue)
        it.putFloat(Constants.PREFERENCES_STRESS_THRESHOLD, stressThresholdValue.toFloat() / 100f)
        editor.apply()
        settingsCallback(SettingsStatus.PreferencesSaved)

    } ?: run {
        settingsCallback(SettingsStatus.ErrorSavePreferences)

    }
}

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

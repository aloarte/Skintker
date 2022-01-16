package com.p4r4d0x.skintker.presenter.settings.view.compose

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants.DEFAULT_ALCOHOL_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.DEFAULT_FOOD_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.DEFAULT_IRRITATION_LEVEL_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.DEFAULT_MIN_LOGS
import com.p4r4d0x.skintker.data.Constants.DEFAULT_STRESS_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.DEFAULT_STRESS_VALUE
import com.p4r4d0x.skintker.data.Constants.DEFAULT_TRAVEL_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.DEFAULT_WEATHER_HUMIDITY_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.DEFAULT_WEATHER_TEMPERATURE_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.DEFAULT_ZONES_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_ALCOHOL_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_FOOD_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_IRRITATION_NUMBER
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_MIN_LOGS
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_STRESS_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_STRESS_VALUE
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_TRAVEL_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_WEATHER_HUMIDITY_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_ZONES_THRESHOLD
import com.p4r4d0x.skintker.data.enums.SettingsStatus
import com.p4r4d0x.skintker.presenter.common.compose.Description
import com.p4r4d0x.skintker.presenter.common.compose.SkintkerDivider


@Composable
fun SettingScreen(
    prefs: SharedPreferences?,
    onBackIconPressed: () -> Unit,
    settingsCallback: (SettingsStatus) -> Unit
) {

    Scaffold(
        topBar = {
            SettingsTopBar(onBackIconPressed)
        }
    ) {
        SettingScreenContent(prefs, settingsCallback)
    }
}

@Composable
fun SettingsTopBar(onBackIconPressed: () -> Unit) {
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

@Composable
fun SettingScreenContent(prefs: SharedPreferences?, settingsCallback: (SettingsStatus) -> Unit) {

    LazyColumn {
        item {
            Column(Modifier.fillMaxSize()) {
                ParametersConfiguration(prefs, settingsCallback)
                CSVButtons()
            }
        }
    }
}


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
                    PREFERENCES_IRRITATION_NUMBER,
                    DEFAULT_IRRITATION_LEVEL_THRESHOLD
                )
            )
        }
        var minLogsValue by remember {
            mutableStateOf(
                preference.getInt(
                    PREFERENCES_MIN_LOGS,
                    DEFAULT_MIN_LOGS
                )
            )
        }
        var foodThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    PREFERENCES_FOOD_THRESHOLD,
                    DEFAULT_FOOD_THRESHOLD
                ) * 100).toInt()
            )
        }
        var zonesThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    PREFERENCES_ZONES_THRESHOLD,
                    DEFAULT_ZONES_THRESHOLD
                ) * 100).toInt()
            )
        }
        var alcoholThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    PREFERENCES_ALCOHOL_THRESHOLD,
                    DEFAULT_ALCOHOL_THRESHOLD
                ) * 100).toInt()
            )
        }
        var travelThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    PREFERENCES_TRAVEL_THRESHOLD,
                    DEFAULT_TRAVEL_THRESHOLD
                ) * 100).toInt()
            )
        }
        var temperatureThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD,
                    DEFAULT_WEATHER_TEMPERATURE_THRESHOLD
                ) * 100).toInt()
            )
        }
        var humidityThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    PREFERENCES_WEATHER_HUMIDITY_THRESHOLD,
                    DEFAULT_WEATHER_HUMIDITY_THRESHOLD
                ) * 100).toInt()
            )
        }
        var stressLevelValue by remember {
            mutableStateOf(
                preference.getInt(
                    PREFERENCES_STRESS_VALUE,
                    DEFAULT_STRESS_VALUE
                )
            )
        }
        var stressThresholdValue by remember {
            mutableStateOf(
                (preference.getFloat(
                    PREFERENCES_STRESS_THRESHOLD,
                    DEFAULT_STRESS_THRESHOLD
                ) * 100).toInt()
            )
        }

        Description(R.string.settings_parameters_description_levels)
        //Irritation level
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
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
        it.putInt(PREFERENCES_IRRITATION_NUMBER, irritationLevelValue)
        it.putInt(PREFERENCES_MIN_LOGS, minLogsValue)
        it.putFloat(PREFERENCES_FOOD_THRESHOLD, foodThresholdValue.toFloat() / 100f)
        it.putFloat(PREFERENCES_ZONES_THRESHOLD, zonesThresholdValue.toFloat() / 100f)
        it.putFloat(PREFERENCES_ALCOHOL_THRESHOLD, alcoholThresholdValue.toFloat() / 100f)
        it.putFloat(PREFERENCES_TRAVEL_THRESHOLD, travelThresholdValue.toFloat() / 100f)
        it.putFloat(
            PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD,
            temperatureThresholdValue.toFloat() / 100f
        )
        it.putFloat(PREFERENCES_WEATHER_HUMIDITY_THRESHOLD, humidityThresholdValue.toFloat() / 100f)
        it.putInt(PREFERENCES_STRESS_VALUE, stressLevelValue)
        it.putFloat(PREFERENCES_STRESS_THRESHOLD, stressThresholdValue.toFloat() / 100f)
        editor.apply()
        settingsCallback(SettingsStatus.PreferencesSaved)

    } ?: run {
        settingsCallback(SettingsStatus.ErrorSavePreferences)

    }
}

@Composable
fun CSVButtons() {
    SkintkerDivider()

    Column(Modifier.fillMaxWidth(), horizontalAlignment = CenterHorizontally) {
        Description(R.string.settings_export_description)
        Button(
            enabled = true,
            modifier = Modifier
                .height(40.dp),
            onClick = { }
        ) {
            Text(text = stringResource(id = R.string.btn_export_data))
        }
        Description(R.string.settings_export_description)
        Divider(Modifier.height(10.dp), color = Color.Transparent)
        Button(
            enabled = true,
            modifier = Modifier
                .height(40.dp),
            onClick = { }
        ) {
            Text(text = stringResource(id = R.string.btn_import_data))
        }
    }
}

package com.p4r4d0x.skintker.presenter.home.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.getHumidityString
import com.p4r4d0x.skintker.getTemperatureString
import com.p4r4d0x.skintker.presenter.common.compose.Description
import com.p4r4d0x.skintker.presenter.common.compose.SkintkerDivider
import com.p4r4d0x.skintker.presenter.common.utils.PossibleCausesProvider
import com.p4r4d0x.skintker.theme.SkintkerTheme
import java.util.*

@SuppressLint("SimpleDateFormat")
@Preview
@Composable
fun ResumeBody(
    @PreviewParameter(PossibleCausesProvider::class) causes: PossibleCausesBO
) {
    SkintkerTheme {
        val noCauses = causes.mostAffectedZones.isEmpty() &&
                !causes.alcoholCause &&
                causes.dietaryCauses.isEmpty() &&
                !causes.stressCause.possibleCause &&
                !causes.travelCause.possibleCause &&
                !causes.weatherCause.first.possibleCause &&
                !causes.weatherCause.first.possibleCause
        Column(Modifier.background(MaterialTheme.colors.background)) {
            ResumeTitle(noCauses)
            if (noCauses) {
                EmptyUserStats()
            } else {
                LazyColumn {
                    item {
                        Column {
                            Description(R.string.resume_description)
                            ZonesOrFoodResume(causes.mostAffectedZones, zones = true)
                            AlcoholResume(causes.alcoholCause)
                            ZonesOrFoodResume(causes.dietaryCauses, zones = false)
                            StressResume(causes.stressCause)
                            TravelResume(causes.travelCause)
                            WeatherResume(causes.weatherCause)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StandaloneEmptyUserStats() {
    SkintkerTheme {
        Column(Modifier.background(MaterialTheme.colors.background)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Text(
                    stringResource(id = R.string.resume_no_causes_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                )
            }
            EmptyUserStats()
        }
    }
}

@Composable
fun EmptyUserStats() {
    Text(
        stringResource(id = R.string.resume_no_causes),
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 30.dp)
    )
    TumbleweedRolling()
}

@Composable
fun ResumeTitle(noCauses: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {

        Text(
            stringResource(
                id = if (noCauses) {
                    R.string.resume_no_causes_title
                } else {
                    R.string.resume_title
                }
            ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(vertical = 15.dp, horizontal = 10.dp)
        )
    }
}

@Composable
fun ZonesOrFoodResume(list: List<String>, zones: Boolean) {
    if (list.isNotEmpty()) {
        SkintkerDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 30.dp)

        ) {
            Text(
                stringResource(
                    id = if (zones) {
                        R.string.resume_zones_caption
                    } else {
                        R.string.resume_dietary_caption
                    }
                ),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.caption
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                list.forEach { zone ->
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = zone,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AlcoholResume(alcoholCause: Boolean) {
    if (alcoholCause) {
        SkintkerDivider()
        Text(
            stringResource(id = R.string.resume_alcohol),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 30.dp)
        )
    }
}

@Composable
fun StressResume(stressCause: PossibleCausesBO.StressCauseBO) {
    if (stressCause.possibleCause) {
        SkintkerDivider()
        Column(
            Modifier
                .padding(vertical = 20.dp, horizontal = 30.dp)
        ) {
            Text(
                stringResource(id = R.string.resume_stress),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.caption
            )
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun TravelResume(travelCause: PossibleCausesBO.TravelCauseBO) {
    if (travelCause.possibleCause) {
        SkintkerDivider()
        Column(
            Modifier
                .padding(vertical = 20.dp, horizontal = 30.dp)
        ) {
            Text(
                stringResource(id = R.string.resume_travel),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.caption
            )
            travelCause.city?.let { city ->
                Spacer(modifier = Modifier.height(3.dp))
                Row {
                    Text(
                        stringResource(id = R.string.resume_travel_city),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        city.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 3.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun WeatherResume(weatherCause: Pair<PossibleCausesBO.WeatherCauseBO, PossibleCausesBO.WeatherCauseBO>) {
    val weatherTemperature = weatherCause.first
    val weatherHumidity = weatherCause.second

    if (weatherTemperature.possibleCause || weatherHumidity.possibleCause) {
        SkintkerDivider()
        Column(
            Modifier
                .padding(vertical = 20.dp, horizontal = 30.dp)
        ) {
            Text(
                stringResource(id = R.string.resume_weather),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.caption
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (weatherTemperature.possibleCause) {
                    Column(
                        Modifier.padding(vertical = 10.dp, horizontal = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(id = R.string.resume_weather_temperature),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            stringResource(id = getTemperatureString(weatherTemperature.averageValue)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(horizontal = 3.dp)
                        )
                    }
                }
                if (weatherHumidity.possibleCause) {
                    Column(
                        Modifier.padding(vertical = 10.dp, horizontal = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(id = R.string.resume_weather_humidity),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            stringResource(id = getHumidityString(weatherHumidity.averageValue)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(horizontal = 3.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TumbleweedRolling() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.tumbleweed_rolling))
    Surface(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
    ) {
        LottieAnimation(composition, restartOnPlay = true)

    }

}
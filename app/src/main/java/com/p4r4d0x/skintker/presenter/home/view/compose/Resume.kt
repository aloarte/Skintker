package com.p4r4d0x.skintker.presenter.home.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
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
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO
import com.p4r4d0x.skintker.domain.parsers.DataParser.getHumidityString
import com.p4r4d0x.skintker.domain.parsers.DataParser.getTemperatureString
import com.p4r4d0x.skintker.presenter.utils.PossibleCausesProvider
import com.p4r4d0x.skintker.theme.SkintkerTheme

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
            ResumeTitle(!causes.enoughData || noCauses)
            if (!causes.enoughData || noCauses) {
                Text(
                    stringResource(id = R.string.resume_no_causes),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 30.dp)
                )

                TumbleweedRolling()
            } else {
                LazyColumn {
                    item {
                        Column {
                            Description()
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
fun Description() {
    Text(
        stringResource(id = R.string.resume_description),
        fontSize = 10.sp,
        fontWeight = FontWeight.Light,
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 30.dp)
    )
}

@Composable
fun ZonesOrFoodResume(list: List<String>, zones: Boolean) {
    if (list.isNotEmpty()) {
        ResumeDivider()
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
        ResumeDivider()
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
        ResumeDivider()
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
            Row {
                Text(
                    stringResource(id = R.string.resume_stress_level),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    stressCause.averageLevel.toString(),
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

@Composable
fun TravelResume(travelCause: PossibleCausesBO.TravelCauseBO) {
    if (travelCause.possibleCause) {
        ResumeDivider()
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
                        city,
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
        ResumeDivider()
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
fun ResumeDivider() {
    Divider(
        modifier = Modifier
            .height(0.2.dp)
            .padding(horizontal = 80.dp),
        color = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun TumbleweedRolling() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.tumbleweed_rolling))
    Surface(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
    ) {
        LottieAnimation(composition)

    }

}
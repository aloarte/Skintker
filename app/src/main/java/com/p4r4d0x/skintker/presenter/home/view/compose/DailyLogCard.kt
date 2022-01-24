package com.p4r4d0x.skintker.presenter.home.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.parsers.DataParser.getAlcoholLevel
import com.p4r4d0x.skintker.domain.parsers.DataParser.getHumidityString
import com.p4r4d0x.skintker.domain.parsers.DataParser.getTemperatureString
import com.p4r4d0x.skintker.presenter.common.utils.DailyLogProvider
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SimpleDateFormat")
@Preview
@Composable
fun DailyLogCard(
    @PreviewParameter(DailyLogProvider::class) log: DailyLogBO
) {
    var collapseView by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier.clickable(
            onClick = { collapseView = !collapseView }
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary),

        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
        ) {
            val dfDay = SimpleDateFormat("EEEE")
            val dfDate = SimpleDateFormat("dd/MM/yyyy")

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    textAlign = if (collapseView) {
                        TextAlign.Left
                    } else {
                        TextAlign.Center
                    },
                    text = "${dfDay.format(log.date)} ${dfDate.format(log.date)}".replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .fillMaxWidth(
                            if (collapseView) {
                                0.8f
                            } else {
                                1f
                            }
                        )
                )

                if (collapseView) {
                    Text(
                        log.irritation?.overallValue.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                    )
                }

            }

            if (!collapseView) {
                Divider(
                    modifier = Modifier.height(10.dp),
                    color = Color.Transparent
                )
                Divider(
                    modifier = Modifier
                        .height(0.5.dp)
                        .padding(horizontal = 50.dp),
                    color = MaterialTheme.colors.primaryVariant
                )
                Divider(
                    modifier = Modifier.height(10.dp),
                    color = Color.Transparent
                )
                DailyLogCardItemBody(log)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun DailyLogCardItemBody(log: DailyLogBO) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            log.irritation?.let { irritation ->
                IrritationItem(
                    irritationBO = irritation, modifier = Modifier
                        .fillMaxWidth(0.5f)
                )
            }
            log.additionalData?.let { additionalData ->
                AdditionalData(
                    additionalDataBO = additionalData, modifier = Modifier
                        .fillMaxWidth(1f)
                )
            }
        }
        if (log.foodList.isNotEmpty()) {
            FoodScheduleList(log.foodList)
        }

    }
}

@Composable
private fun IrritationItem(
    irritationBO: IrritationBO,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        val irritationModifier = Modifier.fillMaxWidth()
        ItemTextPairNumber(
            stringResource(id = R.string.card_irritation_label),
            irritationBO.overallValue,
            irritationModifier
        )
        irritationBO.zoneValues.forEach { zone ->
            Text(
                text = zone.name,
                fontSize = 10.sp,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(vertical = 1.dp, horizontal = 5.dp)
            )
        }
    }
}

@Composable
private fun AdditionalData(additionalDataBO: AdditionalDataBO, modifier: Modifier) {
    Column(modifier = modifier) {
        val additionalDataModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 5.dp)
        ItemTextPairNumber(
            text = stringResource(id = R.string.card_stress_label),
            value = additionalDataBO.stressLevel,
            modifier = Modifier
                .fillMaxWidth()
        )
        WeatherText(additionalDataBO.weather)
        TextCity(
            travel = additionalDataBO.travel,
            modifier = additionalDataModifier
        )
        Text(
            text = stringResource(id = getAlcoholLevel(additionalDataBO.alcoholLevel.value)),
            fontSize = 10.sp,
            style = MaterialTheme.typography.body1,
            modifier = additionalDataModifier
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun FoodScheduleList(list: List<String>) {
    Column(
        Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.card_dietary_label),
            fontSize = 10.sp,
            style = MaterialTheme.typography.body1,
        )
        FoodGrid(list)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodGrid(foodList: List<String>) {
    val halfList = if (foodList.size % 2 == 0) {
        foodList.size / 2
    } else {
        foodList.size / 2 + 1
    }
    val firstColumn = foodList.subList(0, halfList)
    val secondColumn = foodList.subList(halfList, foodList.size)

    Row(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth(0.5F)) {
            firstColumn.forEach { item ->
                Text(text = item, fontSize = 10.sp)
            }
        }
        Column(Modifier.fillMaxWidth(0.5F)) {
            secondColumn.forEach { item ->
                Text(text = item, fontSize = 10.sp)
            }
        }
    }
}


@Composable
private fun ItemTextPairNumber(
    text: String,
    value: Int,
    modifier: Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = text,
            fontSize = 10.sp,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(vertical = 1.dp, horizontal = 5.dp)
        )
        if (value != -1) {
            Text(
                text = value.toString(),
                fontSize = 10.sp,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(vertical = 1.dp, horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun TextCity(travel: AdditionalDataBO.TravelBO, modifier: Modifier) {
    Text(
        text = stringResource(
            id = if (travel.traveled) {
                R.string.card_traveled_today
            } else {
                R.string.card_not_traveled_today
            }
        ),
        fontSize = 10.sp,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
    Row(modifier = modifier) {
        Text(
            text = stringResource(R.string.card_slept_in),
            fontSize = 10.sp,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(vertical = 1.dp)
        )
        Text(
            text = travel.city.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            fontSize = 10.sp,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .padding(vertical = 1.dp, horizontal = 2.dp)
        )
    }
}

@Composable
fun WeatherText(weather: AdditionalDataBO.WeatherBO) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 5.dp)
    ) {
        Text(
            text = stringResource(id = getHumidityString(weather.humidity)),
            fontSize = 10.sp,
            style = MaterialTheme.typography.body1
        )
        Text(
            text = ", ",
            fontSize = 10.sp,
            style = MaterialTheme.typography.body1
        )
        Text(
            text = stringResource(id = getTemperatureString(weather.temperature)),
            fontSize = 10.sp,
            style = MaterialTheme.typography.body1
        )
    }
}


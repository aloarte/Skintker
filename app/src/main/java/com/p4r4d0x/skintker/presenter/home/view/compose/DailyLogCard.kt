package com.p4r4d0x.skintker.presenter.home.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.DataParser.getAlcoholLevel
import com.p4r4d0x.skintker.domain.DataParser.getHumidityString
import com.p4r4d0x.skintker.domain.DataParser.getTemperatureString
import com.p4r4d0x.skintker.domain.bo.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Preview
@Composable
fun DailyLogCard(
    @PreviewParameter(DailyLogProvider::class) log: DailyLogBO
) {
    var collapseView by remember { mutableStateOf(true) }
    Card(
        elevation = 4.dp,
        border = BorderStroke(width = 0.5.dp, color = MaterialTheme.colors.primaryVariant),
        modifier = Modifier.clickable(
            onClick = { collapseView = !collapseView }
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            val dfDay = SimpleDateFormat("EEEE")
            val dfDate = SimpleDateFormat("dd/MM/yyyy")

            Text(
                "${dfDay.format(log.date)} ${dfDate.format(log.date)}".replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                },
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            )
            Divider(modifier = Modifier.height(0.5.dp), color = MaterialTheme.colors.primaryVariant)
            DailyLogCardItemBody(log, collapseView)
        }
    }
}

@Composable
private fun DailyLogCardItemBody(log: DailyLogBO, collapseView: Boolean = false) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            log.irritation?.let { irritation ->
                IrritationItem(
                    irritationBO = irritation, modifier = Modifier
                        .fillMaxWidth(0.5f),
                    collapseView = collapseView
                )
            }
            log.additionalData?.let { additionalData ->
                AdditionalData(
                    additionalDataBO = additionalData, modifier = Modifier
                        .fillMaxWidth(1f)
                )
            }
        }
        log.foodSchedule?.let { foodSchedule ->
            FoodSchedule(foodSchedule, collapseView)
        }
    }
}

@Composable
private fun IrritationItem(
    irritationBO: IrritationBO,
    modifier: Modifier,
    collapseView: Boolean = false
) {
    val maxVisibleItems = 4
    var zoneCnt = 0
    var dotsShown = false
    Column(
        modifier = modifier
    ) {
        val irritationModifier = Modifier.fillMaxWidth()
        ItemTextPairNumber("Overall", irritationBO.overallValue, true, irritationModifier)
        irritationBO.zoneValues.forEach { zone ->
            if (!collapseView || (collapseView && zoneCnt < maxVisibleItems)) {
                ItemTextPairNumber(zone.name, zone.intensity, false, irritationModifier)
            } else {
                if (!dotsShown) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "...",
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(vertical = 1.dp, horizontal = 10.dp)
                        )
                    }
                    dotsShown = true
                }
            }
            zoneCnt++
        }
    }
}

@Composable
private fun AdditionalData(additionalDataBO: AdditionalDataBO, modifier: Modifier) {
    Column(modifier = modifier) {
        val additionalDataModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 10.dp)
        ItemTextPairNumber(
            text = "Stress level",
            value = additionalDataBO.stressLevel,
            highlight = true,
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

@Composable
private fun FoodSchedule(foodScheduleBO: FoodScheduleBO, collapseView: Boolean = false) {
    val anyListNotEmpty = foodScheduleBO.breakfast?.isNotEmpty() == true ||
            foodScheduleBO.morningSnack?.isNotEmpty() == true ||
            foodScheduleBO.lunch?.isNotEmpty() == true ||
            foodScheduleBO.afternoonSnack?.isNotEmpty() == true ||
            foodScheduleBO.dinner?.isNotEmpty() == true
    if (anyListNotEmpty) {
        if (!collapseView) {
            foodScheduleBO.breakfast?.let {
                FoodScheduleList("Breakfast", it)
            }
            foodScheduleBO.morningSnack?.let {
                FoodScheduleList("Morning Snack", it)
            }
            foodScheduleBO.lunch?.let {
                FoodScheduleList("Lunch", it)
            }
            foodScheduleBO.afternoonSnack?.let {
                FoodScheduleList("Afternoon Snack", it)
            }
            foodScheduleBO.dinner?.let {
                FoodScheduleList("Dinner", it)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "_",
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(vertical = 1.dp, horizontal = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun FoodScheduleList(title: String, list: List<FoodItemBO>) {
    Column(
        Modifier
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .fillMaxWidth()
    ) {
        var stringFoodList = ""

        Text(title, fontSize = 12.sp)
        list.forEach { foodItem ->
            val comma = if (list.indexOf(foodItem) != 0) {
                ", "
            } else {
                ""
            }
            stringFoodList = "$stringFoodList$comma${foodItem.name}"
        }
        Text(text = stringFoodList, fontSize = 10.sp, maxLines = 2)
    }
}


@Composable
private fun ItemTextPairNumber(
    text: String,
    value: Int,
    highlight: Boolean = false,
    modifier: Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = text,
            fontSize = 10.sp,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(vertical = 1.dp, horizontal = 10.dp)
        )
        Text(
            text = value.toString(),
            fontSize = 10.sp,
            style = if (highlight) {
                MaterialTheme.typography.body1
            } else {
                MaterialTheme.typography.caption
            },
            color = if (highlight) {
                MaterialTheme.colors.primaryVariant
            } else {
                MaterialTheme.colors.primary
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp, horizontal = 10.dp)
        )
    }
}

@Composable
fun TextCity(travel: AdditionalDataBO.TravelBO, modifier: Modifier) {
    Text(
        text = stringResource(
            id = if (travel.traveled) {
                R.string.traveled_today
            } else {
                R.string.traveled_today
            }
        ),
        fontSize = 10.sp,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
    Row(modifier = modifier) {
        Text(
            text = stringResource(R.string.slept_in),
            fontSize = 10.sp,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(vertical = 1.dp)
        )
        Text(
            text = travel.city,
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
            .padding(vertical = 1.dp, horizontal = 10.dp)
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

class DailyLogProvider : PreviewParameterProvider<DailyLogBO> {
    override val values: Sequence<DailyLogBO>
        get() = listOf(
            DailyLogBO(
                date = Calendar.getInstance().time,
                irritation = IrritationBO(
                    overallValue = 8,
                    zoneValues = listOf(
                        IrritationBO.IrritatedZoneBO("Wrist", 10),
                        IrritationBO.IrritatedZoneBO("Shoulder", 6),
                        IrritationBO.IrritatedZoneBO("Ear", 7)
                    )
                ),
                foodSchedule = FoodScheduleBO(
                    breakfast = listOf(FoodItemBO("Apple"), FoodItemBO("Coffee")),
                    lunch = listOf(
                        FoodItemBO("Tomatoes"),
                        FoodItemBO("Rice"),
                        FoodItemBO("Shrimp"),
                        FoodItemBO("Red pepper"),
                        FoodItemBO("Onion"),
                        FoodItemBO("Bread")
                    ),
                    afternoonSnack = listOf(FoodItemBO("Biscuit"), FoodItemBO("Milk")),
                    dinner = listOf(FoodItemBO("Chickpea"), FoodItemBO("Bread"))
                ),
                additionalData = AdditionalDataBO(
                    stressLevel = 7,
                    alcoholLevel = AlcoholLevel.Few,
                    weather = AdditionalDataBO.WeatherBO(humidity = 2, temperature = 1),
                    travel = AdditionalDataBO.TravelBO(traveled = true, city = "Madrid")
                )
            )
        ).asSequence()
}
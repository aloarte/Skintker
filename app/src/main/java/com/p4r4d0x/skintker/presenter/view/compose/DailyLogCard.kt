package com.p4r4d0x.skintker.presenter.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.p4r4d0x.skintker.domain.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Preview
@Composable
fun DailyLogCard(
    @PreviewParameter(DailyLogProvider::class) log: DailyLogBO
) {
    Card(elevation = 4.dp) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            val dfDay = SimpleDateFormat("EEEE")
            val dfDate = SimpleDateFormat("dd/MM/yyyy")

            Text(
                "${dfDay.format(log.date)} ${dfDate.format(log.date)}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Divider(modifier = Modifier.height(1.dp), color = Color.Black)
            DailyLogCardItemBody(log)
        }
    }
}

@Composable
private fun DailyLogCardItemBody(log: DailyLogBO) {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        IrritationItem(log.irritation)
        FoodSchedule(log.fooodSchedule)
        AdditionalData(log.additionalData)
    }

}

@Composable
private fun IrritationItem(irritationBO: IrritationBO) {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text("Picor: ${irritationBO.overallValue}", fontSize = 10.sp)
        irritationBO.zoneValues.forEach { zone ->
            Text(
                text = "${zone.name}: ${zone.intensity}",
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            )
        }
    }
}

@Composable
private fun FoodSchedule(foodScheduleBO: FoodScheduleBO) {
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
}

@Composable
private fun FoodScheduleList(title: String, list: List<FoodItemBO>) {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(title, fontSize = 10.sp)
        list.forEach { foodItem ->
            Text(
                text = foodItem.name,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            )
        }
    }
}

@Composable
private fun AdditionalData(additionalDataBO: AdditionalDataBO) {

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
                fooodSchedule = FoodScheduleBO(
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
                additionalData = AdditionalDataBO(stressLevel = 7, WeatherStatus.CLOUDY)
            )
        ).asSequence()
}


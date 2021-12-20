package com.p4r4d0x.skintker.presenter.view

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.p4r4d0x.skintker.domain.bo.*
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.presenter.view.compose.DailyLogCard
import com.p4r4d0x.skintker.presenter.view.compose.LogQuestionScreen
import com.p4r4d0x.skintker.presenter.view.compose.SurveyResultScreen
import com.p4r4d0x.skintker.presenter.viewmodel.MainViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import java.util.*


@Composable
fun LogScreen(viewModel: MainViewModel) {
    SkintkerTheme {
        viewModel.uiState.observeAsState().value?.let { logState ->
            when (logState) {
                is SurveyState.LogQuestions -> {
                    logState.state.forEach {
                        Log.d("ALRALR", "${it}")
                    }
                    LogQuestionScreen(
                        logQuestions = logState,
                        onDonePressed = { viewModel.computeResult(logState) },
                        onBackPressed = {
//                            activity?.onBackPressedDispatcher?.onBackPressed()
                        }
                    )
                }
                is SurveyState.Result -> {
                    Log.d("ALRALR", "$logState")
                    SurveyResultScreen(
                        result = logState,
                        onDonePressed = {
//                        activity?.onBackPressedDispatcher?.onBackPressed()
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun ResumeScreen() {

}

@Composable
fun HistoryScreen() {
    val shortLog = DailyLogBO(
        date = Calendar.getInstance().time,
        irritation = IrritationBO(
            overallValue = 8,
            zoneValues = listOf(
                IrritationBO.IrritatedZoneBO("Wrist", 10),
                IrritationBO.IrritatedZoneBO("Shoulder", 6),
                IrritationBO.IrritatedZoneBO("Ear", 7)
            )
        ),
        foodSchedule = FoodScheduleBO(),
        additionalData = AdditionalDataBO(
            stressLevel = 7,
            alcoholLevel = 0,
            weather = AdditionalDataBO.WeatherBO(humidity = 0, temperature = 4),
            travel = AdditionalDataBO.TravelBO(traveled = true, city = "Madrid")
        )
    )

    val log = DailyLogBO(
        date = Calendar.getInstance().time,
        irritation = IrritationBO(
            overallValue = 8,
            zoneValues = listOf(
                IrritationBO.IrritatedZoneBO("Wrist", 10),
                IrritationBO.IrritatedZoneBO("Shoulder", 6),
                IrritationBO.IrritatedZoneBO("Ear", 7)
            )
        ),
        foodSchedule = FoodScheduleBO(dinner = listOf(FoodItemBO("a"), FoodItemBO("b"))),
        additionalData = AdditionalDataBO(
            alcoholLevel = 1,
            stressLevel = 7,
            weather = AdditionalDataBO.WeatherBO(humidity = 3, temperature = 3),
            travel = AdditionalDataBO.TravelBO(traveled = true, city = "Madrid")
        )
    )

    val irritationLog = DailyLogBO(
        date = Calendar.getInstance().time,
        irritation = IrritationBO(
            overallValue = 8,
            zoneValues = listOf(
                IrritationBO.IrritatedZoneBO("Wrist", 10),
                IrritationBO.IrritatedZoneBO("Shoulder", 6),
                IrritationBO.IrritatedZoneBO("Ear", 7),
                IrritationBO.IrritatedZoneBO("Knee", 7),
                IrritationBO.IrritatedZoneBO("Lips", 7)
            )
        ),
        foodSchedule = FoodScheduleBO(/*dinner = listOf(FoodItemBO("a"),FoodItemBO("b"))*/),
        additionalData = AdditionalDataBO(
            alcoholLevel = 2,
            stressLevel = 7,
            weather = AdditionalDataBO.WeatherBO(humidity = 0, temperature = 4),
            travel = AdditionalDataBO.TravelBO(traveled = true, city = "Madrid")
        )
    )

    val long = DailyLogBO(
        date = Calendar.getInstance().time,
        irritation = IrritationBO(
            overallValue = 8,
            zoneValues = listOf(
                IrritationBO.IrritatedZoneBO("Wrist", 10),
                IrritationBO.IrritatedZoneBO("Shoulder", 6),
                IrritationBO.IrritatedZoneBO("Ear", 7),
                IrritationBO.IrritatedZoneBO("Knee", 7),
                IrritationBO.IrritatedZoneBO("Nose", 7),
                IrritationBO.IrritatedZoneBO("Knee", 7),
                IrritationBO.IrritatedZoneBO("Lips", 7)

            )
        ),
        foodSchedule = FoodScheduleBO(
            dinner = listOf(
                FoodItemBO("Garbanzos"),
                FoodItemBO("Pimenton"),
                FoodItemBO("Comino"),
                FoodItemBO("Aceite"),
                FoodItemBO("Pan"),
                FoodItemBO("Harina"),
                FoodItemBO("Cerveza"),
                FoodItemBO("Garbanzos"),
                FoodItemBO("Pimenton"),
                FoodItemBO("Comino"),
                FoodItemBO("Aceite"),
                FoodItemBO("Pan"),
                FoodItemBO("Harina")


            ),
            breakfast = listOf(FoodItemBO("a 1"), FoodItemBO("b")),
            lunch = listOf(FoodItemBO("a 1"), FoodItemBO("b"), FoodItemBO("c"), FoodItemBO("d")),
            afternoonSnack = listOf(FoodItemBO("a 1")),

            ),
        additionalData = AdditionalDataBO(
            alcoholLevel = 2,
            stressLevel = 7,
            weather = AdditionalDataBO.WeatherBO(humidity = 4, temperature = 2),
            travel = AdditionalDataBO.TravelBO(traveled = true, city = "Madrid")
        )
    )

    SkintkerTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp)
        ) {
            item {
                DailyLogCard(log = log)
                Spacer(modifier = Modifier.height(5.dp))
                DailyLogCard(log = irritationLog)
                Spacer(modifier = Modifier.height(5.dp))
                DailyLogCard(log = long)
                Spacer(modifier = Modifier.height(5.dp))
                DailyLogCard(log = shortLog)
            }

        }
    }
}

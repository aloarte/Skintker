package com.p4r4d0x.skintker.presenter.common.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.p4r4d0x.domain.bo.AlcoholLevel
import com.example.domain.bo.AdditionalDataBO
import com.example.domain.bo.DailyLogBO
import com.example.domain.bo.IrritationBO
import com.example.domain.bo.PossibleCausesBO
import java.util.*

class DailyLogProvider : PreviewParameterProvider<com.example.domain.bo.DailyLogBO> {
    override val values: Sequence<com.example.domain.bo.DailyLogBO>
        get() = listOf(
            com.example.domain.bo.DailyLogBO(
                date = Calendar.getInstance().time,
                irritation = com.example.domain.bo.IrritationBO(
                    overallValue = 8,
                    zoneValues = listOf(
                        "Wrist",
                        "Shoulder",
                        "Ear"
                    )
                ),
                foodList = listOf(
                    "Tomatoes \uD83C\uDF3F",
                    "Rice \uD83C\uDF3F",
                    "Shrimp \uD83C\uDF3F",
                    "Red pepper \uD83C\uDF3F",
                    "Onion \uD83C\uDF3F"
                ),
                additionalData = com.example.domain.bo.AdditionalDataBO(
                    stressLevel = 7,
                    alcoholLevel = com.p4r4d0x.domain.bo.AlcoholLevel.Few,
                    weather = com.example.domain.bo.AdditionalDataBO.WeatherBO(
                        humidity = 2,
                        temperature = 1
                    ),
                    travel = com.example.domain.bo.AdditionalDataBO.TravelBO(
                        traveled = true,
                        city = "Madrid"
                    )
                )
            ),
            com.example.domain.bo.DailyLogBO(
                date = Calendar.getInstance().time,
                irritation = com.example.domain.bo.IrritationBO(
                    overallValue = 8,
                    zoneValues = listOf(
                        "Wrist",
                        "Shoulder",
                        "Ear",
                        "Legs",
                        "Hand",
                        "Neck",
                        "Back",
                    )
                ),
                foodList = listOf(
                    "Tomatoes",
                    "Rice",
                    "Shrimp",
                    "Red pepper",
                    "Onion",
                    "Bread",
                    "Red pepper",
                    "Onion"
                ),
                additionalData = com.example.domain.bo.AdditionalDataBO(
                    stressLevel = 7,
                    alcoholLevel = com.p4r4d0x.domain.bo.AlcoholLevel.Few,
                    weather = com.example.domain.bo.AdditionalDataBO.WeatherBO(
                        humidity = 2,
                        temperature = 1
                    ),
                    travel = com.example.domain.bo.AdditionalDataBO.TravelBO(
                        traveled = true,
                        city = "Madrid"
                    ),
                    beerTypes = listOf("IPA", "Stout")
                )
            )
        ).asSequence()
}

class PossibleCausesProvider : PreviewParameterProvider<com.example.domain.bo.PossibleCausesBO> {
    override val values: Sequence<com.example.domain.bo.PossibleCausesBO>
        get() = listOf(
            com.example.domain.bo.PossibleCausesBO(
                enoughData = true,
                dietaryCauses = listOf("Alcohol", "Nuts", "Fermented"),
                alcoholCause = true,
                mostAffectedZones = listOf("Neck", "Wrists"),
                stressCause = com.example.domain.bo.PossibleCausesBO.StressCauseBO(true, 8),
                travelCause = com.example.domain.bo.PossibleCausesBO.TravelCauseBO(true, "Madrid"),
                weatherCause = Pair(
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        true,
                        3
                    ),
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        true,
                        3
                    )
                )
            ),
            com.example.domain.bo.PossibleCausesBO(
                enoughData = false,
                dietaryCauses = emptyList(),
                alcoholCause = false,
                mostAffectedZones = emptyList(),
                stressCause = com.example.domain.bo.PossibleCausesBO.StressCauseBO(false, 0),
                travelCause = com.example.domain.bo.PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        false,
                        0
                    ),
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            ),
            com.example.domain.bo.PossibleCausesBO(
                enoughData = true,
                dietaryCauses = listOf("Alcohol", "Nuts", "Fermented"),
                alcoholCause = true,
                mostAffectedZones = listOf("Neck", "Wrists"),
                stressCause = com.example.domain.bo.PossibleCausesBO.StressCauseBO(false, 0),
                travelCause = com.example.domain.bo.PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        true,
                        2
                    ),
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            ),
            com.example.domain.bo.PossibleCausesBO(
                enoughData = false,
                dietaryCauses = listOf("Alcohol", "Nuts", "Fermented"),
                alcoholCause = true,
                mostAffectedZones = listOf("Neck", "Wrists"),
                stressCause = com.example.domain.bo.PossibleCausesBO.StressCauseBO(false, 0),
                travelCause = com.example.domain.bo.PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        true,
                        2
                    ),
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            ),
            com.example.domain.bo.PossibleCausesBO(
                enoughData = true,
                dietaryCauses = listOf("Alcohol"),
                alcoholCause = false,
                mostAffectedZones = emptyList(),
                stressCause = com.example.domain.bo.PossibleCausesBO.StressCauseBO(true, 6),
                travelCause = com.example.domain.bo.PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        false,
                        2
                    ),
                    com.example.domain.bo.PossibleCausesBO.WeatherCauseBO(
                        com.example.domain.bo.PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            )
        ).asSequence()
}
package com.p4r4d0x.skintker.presenter.common.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO
import java.util.*

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
                foodList = listOf(
                    "Tomatoes",
                    "Rice",
                    "Shrimp",
                    "Red pepper",
                    "Onion",
                    "Bread"
                ),
                additionalData = AdditionalDataBO(
                    stressLevel = 7,
                    alcoholLevel = AlcoholLevel.Few,
                    weather = AdditionalDataBO.WeatherBO(humidity = 2, temperature = 1),
                    travel = AdditionalDataBO.TravelBO(traveled = true, city = "Madrid")
                )
            ),
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
                foodList = listOf(
                    "Tomatoes",
                    "Rice",
                    "Shrimp",
                    "Red pepper",
                    "Onion",
                    "Bread"
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

class PossibleCausesProvider : PreviewParameterProvider<PossibleCausesBO> {
    override val values: Sequence<PossibleCausesBO>
        get() = listOf(
            PossibleCausesBO(
                enoughData = true,
                dietaryCauses = listOf("Alcohol", "Nuts", "Fermented"),
                alcoholCause = true,
                mostAffectedZones = listOf("Neck", "Wrists"),
                stressCause = PossibleCausesBO.StressCauseBO(true, 8),
                travelCause = PossibleCausesBO.TravelCauseBO(true, "Madrid"),
                weatherCause = Pair(
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        true,
                        3
                    ),
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        true,
                        3
                    )
                )
            ),
            PossibleCausesBO(
                enoughData = false,
                dietaryCauses = emptyList(),
                alcoholCause = false,
                mostAffectedZones = emptyList(),
                stressCause = PossibleCausesBO.StressCauseBO(false, 0),
                travelCause = PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        false,
                        0
                    ),
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            ),
            PossibleCausesBO(
                enoughData = true,
                dietaryCauses = listOf("Alcohol", "Nuts", "Fermented"),
                alcoholCause = true,
                mostAffectedZones = listOf("Neck", "Wrists"),
                stressCause = PossibleCausesBO.StressCauseBO(false, 0),
                travelCause = PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        true,
                        2
                    ),
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            ),
            PossibleCausesBO(
                enoughData = false,
                dietaryCauses = listOf("Alcohol", "Nuts", "Fermented"),
                alcoholCause = true,
                mostAffectedZones = listOf("Neck", "Wrists"),
                stressCause = PossibleCausesBO.StressCauseBO(false, 0),
                travelCause = PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        true,
                        2
                    ),
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            ),
            PossibleCausesBO(
                enoughData = true,
                dietaryCauses = listOf("Alcohol"),
                alcoholCause = false,
                mostAffectedZones = emptyList(),
                stressCause = PossibleCausesBO.StressCauseBO(true, 6),
                travelCause = PossibleCausesBO.TravelCauseBO(false, null),
                weatherCause = Pair(
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                        false,
                        2
                    ),
                    PossibleCausesBO.WeatherCauseBO(
                        PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                        false,
                        0
                    )
                )
            )
        ).asSequence()
}
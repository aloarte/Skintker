package com.p4r4d0x.data.dto.logs

import com.p4r4d0x.data.parsers.DataParser
import com.p4r4d0x.domain.bo.AdditionalDataBO
import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.IrritationBO

data class ReportDto(
    val date: String,
    val foodList: List<String>,
    val irritation: IrritationDto,
    val additionalData: AdditionalDataDto
) {
    data class IrritationDto(val overallValue: Int, val zoneValues: List<String>) {
        fun toBo() = IrritationBO(overallValue = this.overallValue, zoneValues = this.zoneValues)

        fun fromBo(bo: IrritationBO) =
            IrritationDto(overallValue = bo.overallValue, zoneValues = bo.zoneValues)
    }

    data class AdditionalDataDto(
        val stressLevel: Int,
        val weather: WeatherDto,
        val travel: TravelDto,
        val alcohol: AlcoholDto
    ) {
        fun toBo() = AdditionalDataBO(
            stressLevel = this.stressLevel,
            weather = this.weather.toBo(),
            travel = this.travel.toBo(),
            alcohol = this.alcohol.toBo()
        )
    }

    data class WeatherDto(val humidity: Int, val temperature: Int) {
        fun toBo() = AdditionalDataBO.WeatherBO(
            humidity = this.humidity,
            temperature = this.temperature
        )

        fun fromBo(bo: AdditionalDataBO.WeatherBO) = WeatherDto(
            humidity = bo.humidity,
            temperature = bo.temperature
        )
    }

    data class TravelDto(val traveled: Boolean, val city: String) {
        fun toBo() = AdditionalDataBO.TravelBO(
            traveled = this.traveled,
            city = this.city
        )

        fun fromBo(bo: AdditionalDataBO.TravelBO) = TravelDto(
            traveled = this.traveled,
            city = this.city
        )
    }

    data class AlcoholDto(
        val level: AlcoholLevel,
        val beers: List<String> = emptyList(),
        val wines: List<String> = emptyList(),
        val distilledDrinks: List<String> = emptyList()
    ) {
        fun toBo() = AdditionalDataBO.AlcoholBO(
            level = this.level,
            beers = this.beers,
            wines = this.wines,
            distilledDrinks = this.distilledDrinks,
        )

        fun fromBo(bo: AdditionalDataBO.AlcoholBO) = AlcoholDto(
            level = this.level,
            beers = this.beers,
            wines = this.wines,
            distilledDrinks = this.distilledDrinks,
        )
    }
}

fun DailyLogBO.toDto() = ReportDto(
    date = DataParser.backendDateToString(this.date),
    foodList = this.foodList,
    irritation = this.irritation.toDto(),
    additionalData = this.additionalData.toDto()
)

fun IrritationBO.toDto() = ReportDto.IrritationDto(
    overallValue = this.overallValue,
    zoneValues = this.zoneValues
)

fun AdditionalDataBO.toDto() = ReportDto.AdditionalDataDto(
    stressLevel = this.stressLevel,
    weather = this.weather.toDto(),
    travel = this.travel.toDto(),
    alcohol = this.alcohol.toDto()
)

fun AdditionalDataBO.WeatherBO.toDto() = ReportDto.WeatherDto(
    humidity = this.humidity,
    temperature = this.temperature
)

fun AdditionalDataBO.TravelBO.toDto() = ReportDto.TravelDto(
    traveled = this.traveled,
    city = this.city
)

fun AdditionalDataBO.AlcoholBO.toDto() = ReportDto.AlcoholDto(
    level = this.level,
    beers = this.beers,
    wines = this.wines,
    distilledDrinks = this.distilledDrinks,
)


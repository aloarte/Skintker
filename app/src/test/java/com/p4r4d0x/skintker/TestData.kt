package com.p4r4d0x.skintker

import com.p4r4d0x.data.parsers.DataParser
import com.p4r4d0x.domain.bo.AdditionalDataBO
import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.IrritationBO

object TestData {

    val date = DataParser.getCurrentFormattedDate()

    val dailyLog = DailyLogBO(
        date, IrritationBO(0, emptyList()),
        AdditionalDataBO(
            0,
            AdditionalDataBO.WeatherBO(0, 0),
            AdditionalDataBO.TravelBO(false, ""),
            AdditionalDataBO.AlcoholBO(level =  AlcoholLevel.None)
        ), emptyList()
    )

    const val USER_ID = "user_id"

}
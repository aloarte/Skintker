package com.p4r4d0x.skintker.data.room

import androidx.room.*
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.FoodScheduleBO
import java.util.*

@Entity(
)
class DailyLog(
    var date: Date
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    var logId: Long = 0
}

@Entity
data class DailyLogDetails(
    @Embedded
    val dailyLog: DailyLog,

    @Relation(
        parentColumn = "log_id",
        entityColumn = "irritation_id",
        entity = IrritationDetails::class
    )
    val irritation: IrritationDetails
) {
    fun toDomain() = DailyLogBO(
        date = dailyLog.date, irritation = irritation.toDomain(),
        FoodScheduleBO(), AdditionalDataBO(
            stressLevel = 7,
            alcoholLevel = 0,
            weather = AdditionalDataBO.WeatherBO(humidity = 0, temperature = 4),
            travel = AdditionalDataBO.TravelBO(traveled = true, city = "Madrid")
        )
    )
}


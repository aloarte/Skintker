package com.p4r4d0x.skintker.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.FoodScheduleBO
import java.util.*

@Entity(
    tableName = "logs_table",
)
class DailyLog(
    var date: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    fun toDomain() = DailyLogBO(
        date = date
    )

}

fun fromDomain(logBO: DailyLogBO) = DailyLog(date = logBO.date)


@Entity
data class DailyLogDetails(
    @Embedded
    val dailyLog: DailyLog,

    @Relation(
        parentColumn = "id",
        entityColumn = "log_id"
    )
    val irritation: Irritation,

    @Relation(
        parentColumn = "id",
        entityColumn = "log_id"
    )
    val additionalData: AdditionalData
) {
    fun toDomain() = DailyLogBO(
        date = dailyLog.date,
        irritation = irritation.toDomainObject(),
        foodSchedule = FoodScheduleBO(),
        additionalData = additionalData.toDomainObject()
    )

}


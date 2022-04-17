package com.p4r4d0x.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.p4r4d0x.domain.bo.DailyLogBO
import java.util.*

@Entity(
    tableName = "logs_table",
)
data class DailyLog(
    var date: Date,
    var foodList: List<String>,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) {
    fun toDomain() = DailyLogBO(
        date = date,
        foodList = foodList
    )
}

fun fromDomain(logBO: DailyLogBO): DailyLog {
    return DailyLog(date = Date(logBO.date.time), foodList = logBO.foodList)
}

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
        foodList = dailyLog.foodList,
        additionalData = additionalData.toDomainObject()
    )
}
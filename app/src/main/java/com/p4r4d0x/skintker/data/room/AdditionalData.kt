package com.p4r4d0x.skintker.data.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.p4r4d0x.skintker.data.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO

@Entity(
    tableName = "additional_data_table",
    foreignKeys = [
        ForeignKey(
            entity = DailyLog::class,
            parentColumns = ["id"],
            childColumns = ["log_id"]
        )
    ]
)
data class AdditionalData(
    @NonNull @ColumnInfo(name = "log_id")
    val logId: Long,
    val stressLevel: Int,
    val weather: AdditionalDataBO.WeatherBO,
    val travel: AdditionalDataBO.TravelBO,
    val alcoholLevel: AlcoholLevel
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "additional_data_id")
    var additionalDataId: Long = 0

    fun toDomainObject(): AdditionalDataBO {
        return AdditionalDataBO(
            stressLevel = stressLevel,
            weather = weather,
            travel = travel,
            alcoholLevel = alcoholLevel
        )
    }
}

fun fromDomainObject(additionalDataBO: AdditionalDataBO, logId: Long) =
    AdditionalData(
        stressLevel = additionalDataBO.stressLevel,
        logId = logId,
        weather = additionalDataBO.weather,
        travel = additionalDataBO.travel,
        alcoholLevel = additionalDataBO.alcoholLevel

    )


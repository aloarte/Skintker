package com.p4r4d0x.data.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.p4r4d0x.domain.bo.AdditionalDataBO
import com.p4r4d0x.domain.bo.AlcoholLevel

@Entity(
    tableName = "additional_data_table",
    foreignKeys = [
        ForeignKey(
            entity = DailyLog::class,
            parentColumns = ["id"],
            childColumns = ["log_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AdditionalData(
    @NonNull @ColumnInfo(name = "log_id")
    val logId: Long,
    val stressLevel: Int,
    val weather: AdditionalDataBO.WeatherBO,
    val travel:AdditionalDataBO.TravelBO,
    val alcoholLevel: AlcoholLevel,
    val beerType: List<String>,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "additional_data_id")
    var additionalDataId: Long = 0
) {


    fun toDomainObject(): AdditionalDataBO {
        return AdditionalDataBO(
            stressLevel = stressLevel,
            weather = weather,
            travel = travel,
            alcoholLevel = alcoholLevel,
            beerTypes = beerType
        )
    }
}

fun fromDomainObject(additionalDataBO: AdditionalDataBO, logId: Long) =
    AdditionalData(
        stressLevel = additionalDataBO.stressLevel,
        logId = logId,
        weather = additionalDataBO.weather,
        travel = additionalDataBO.travel,
        alcoholLevel = additionalDataBO.alcoholLevel,
        beerType = additionalDataBO.beerTypes

    )


package com.p4r4d0x.skintker.data.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.p4r4d0x.skintker.domain.bo.IrritationBO

@Entity(
    tableName = "irritation_table",
    foreignKeys = [
        ForeignKey(
            entity = DailyLog::class,
            parentColumns = ["id"],
            childColumns = ["log_id"]
        )
    ]
)
data class Irritation(
    val overallValue: Int,
    @NonNull @ColumnInfo(name = "log_id")
    val logId: Long,
    val zones: List<IrritatedZone>,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "irritation_id")
    var irritationId: Long = 0
) {


    fun toDomainObject(): IrritationBO {
        val irritatedZones = zones.map {
            it.toDomainObject()
        }
        return IrritationBO(overallValue = overallValue, zoneValues = irritatedZones)
    }
}

data class IrritatedZone(
    val name: String,
    val intensity: Int
) {
    fun toDomainObject() = IrritationBO.IrritatedZoneBO(name = name, intensity = intensity)
}

fun fromDomainObject(irritatedZone: IrritationBO.IrritatedZoneBO) =
    IrritatedZone(name = irritatedZone.name, intensity = irritatedZone.intensity)


fun fromDomainObject(irritationBo: IrritationBO, logId: Long) =
    Irritation(
        overallValue = irritationBo.overallValue,
        zones = irritationBo.zoneValues.map {
            fromDomainObject(it)
        },
        logId = logId
    )

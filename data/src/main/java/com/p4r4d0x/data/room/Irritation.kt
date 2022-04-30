package com.p4r4d0x.data.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.p4r4d0x.domain.bo.IrritationBO

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
    val zones: List<String>,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "irritation_id")
    var irritationId: Long = 0
) {

    fun toDomainObject(): IrritationBO {
        return IrritationBO(overallValue = overallValue, zoneValues = zones)
    }
}

fun fromDomainObject(irritationBo: IrritationBO, logId: Long) =
    Irritation(
        overallValue = irritationBo.overallValue,
        zones = irritationBo.zoneValues,
        logId = logId
    )

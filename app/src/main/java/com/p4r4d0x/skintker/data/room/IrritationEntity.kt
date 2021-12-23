package com.p4r4d0x.skintker.data.room

import androidx.annotation.NonNull
import androidx.room.*
import com.p4r4d0x.skintker.domain.bo.IrritationBO

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = DailyLog::class,
            parentColumns = ["log_id"],
            childColumns = ["parent_id"]
        )
    ]
)
class Irritation(
    val overallValue: Int,
    @NonNull @ColumnInfo(name = "parent_id")
    val parentId: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "irritation_id")
    var irritationId: Long = 0

}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Irritation::class,
            parentColumns = ["irritation_id"],
            childColumns = ["parent_id"]
        )
    ]
)
data class IrritatedZone(
    val name: String,
    val intensity: Int,
    @NonNull @ColumnInfo(name = "parent_id")
    val parentId: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "irritated_zone_id")
    var irritatedZoneId: Long = 0

    fun toDomainObject(): IrritationBO.IrritatedZoneBO =
        IrritationBO.IrritatedZoneBO(name = name, intensity = intensity)
}

data class IrritationDetails(
    @Embedded val irritation: Irritation,
    @Relation(
        parentColumn = "irritation_id",
        entityColumn = "irritated_zone_id"
    )
    val irritatedZones: List<IrritatedZone>
) {
    fun toDomain(): IrritationBO {
        val irritatedZones = irritatedZones.map {
            it.toDomainObject()
        }
        return IrritationBO(overallValue = irritation.overallValue, zoneValues = irritatedZones)
    }
}

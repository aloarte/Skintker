package com.p4r4d0x.skintker.domain.bo

data class IrritationBO(
    val overallValue: Int,
    val zoneValues: List<IrritatedZoneBO>
) {
    data class IrritatedZoneBO(val name: String, val intensity: Int)

}

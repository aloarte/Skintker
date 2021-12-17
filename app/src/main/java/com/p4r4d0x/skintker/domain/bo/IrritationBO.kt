package com.p4r4d0x.skintker.domain.bo

class IrritationBO(
    val overallValue: Int,
    val zoneValues: List<IrritatedZoneBO>
) {
    class IrritatedZoneBO(val name: String, val intensity: Int)

}

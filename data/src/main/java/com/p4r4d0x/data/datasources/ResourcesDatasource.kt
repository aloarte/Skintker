package com.p4r4d0x.data.datasources

interface ResourcesDatasource {

    fun getZonesReferenceMap(): Map<Int, Int>

    fun getFoodReferenceMap(): Map<Int, Int>

    fun getBeerTypesReferenceMap(): Map<Int, Int>
}
package com.p4r4d0x.domain.utils

import com.p4r4d0x.domain.bo.AlcoholLevel
import org.junit.Test
import kotlin.test.assertEquals

class ExtensionsTest {

    companion object {
        const val MAP_STRING_VALUE = "value"
        const val MAP_STRING_VALUE_2 = "value2"
        const val MAP_INT_VALUE = 1
        const val MAP_INT_VALUE_2 = 2
//        val date = Date(1641031810L * 1000) // 2022/01/01 10:10:10
    }

    @Test
    fun `test get max value (String,Int)`() {
        val map = mapOf(
            MAP_STRING_VALUE to 3,
            MAP_STRING_VALUE_2 to 5
        )

        assertEquals(MAP_STRING_VALUE_2, map.getMaxValue())
    }

    @Test
    fun `test get max value (Int,Int)`() {
        val map = mapOf(
            MAP_INT_VALUE to 10,
            MAP_INT_VALUE_2 to 5
        )

        assertEquals(MAP_INT_VALUE, map.getMaxValue())
    }

    @Test
    fun `test get key of max value (Int,Int)`() {
        val map = mapOf(
            MAP_INT_VALUE to 10,
            MAP_INT_VALUE_2 to 5
        )

        assertEquals(10, map.getKeyOfMaxValue())
    }

    @Test
    fun `test update value (String,Int)`() {
        val map = mutableMapOf(MAP_STRING_VALUE to 3)

        map.increaseValue(MAP_STRING_VALUE)

        assertEquals(4, map[MAP_STRING_VALUE])
    }

    @Test
    fun `test update value (Int,Int)`() {
        val map = mutableMapOf(MAP_INT_VALUE to 3)

        map.increaseValue(MAP_INT_VALUE)

        assertEquals(4, map[MAP_INT_VALUE])
    }

    @Test
    fun `test update value (Boolean,Int)`() {
        val map = mutableMapOf(true to 3)

        map.increaseValue(true)

        assertEquals(4, map[true])
    }

    @Test
    fun `test update value (AlcoholLevel,Int)`() {
        val map = mutableMapOf(AlcoholLevel.Beer to 3)

        map.increaseValue(AlcoholLevel.Beer)

        assertEquals(4, map[AlcoholLevel.Beer])
    }

    @Test
    fun `test clean string`() {
        assertEquals("Pineapple ", "Pineapple \uD83C\uDF4D".cleanString())
        assertEquals("Pineapple ", "Pinéapple ".cleanString())
        assertEquals("Pineapple", "Pineapple".cleanString())
    }

//    @Test
//    fun `get date formats`() {
//        assertEquals("01/01/2022", date.getDDMMYYYYDate())
//        assertEquals("sábado", date.getDayDate())
//        assertEquals(Date("01/01/2022"), date.getDateWithoutTime())
//    }
}
package com.p4r4d0x.skintker.data.room

import androidx.room.TypeConverter
import java.util.*

object Converters {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}
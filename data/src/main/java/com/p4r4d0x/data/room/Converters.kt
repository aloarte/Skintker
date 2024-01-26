package com.p4r4d0x.data.room

import androidx.room.TypeConverter
import com.p4r4d0x.domain.bo.AlcoholBO
import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.TravelBO
import com.p4r4d0x.domain.bo.WeatherBO
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type
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

    @TypeConverter
    fun toStringList(zonesStr: String?): List<String>? {
        if (zonesStr == null) {
            return null
        }

        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(type)

        return adapter.fromJson(zonesStr)
    }

    @TypeConverter
    fun fromStringList(zones: List<String>?): String? {
        if (zones == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(type)

        return adapter.toJson(zones)
    }


    @TypeConverter
    fun toWeather(weatherStr: String?): WeatherBO? {
        if (weatherStr == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(WeatherBO::class.java).fromJson(weatherStr)
    }

    @TypeConverter
    fun fromWeather(weather: WeatherBO?): String? {
        if (weather == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(WeatherBO::class.java).toJson(weather)
    }

    @TypeConverter
    fun toTravel(travelStr: String?): TravelBO? {
        if (travelStr == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(TravelBO::class.java).fromJson(travelStr)
    }

    @TypeConverter
    fun fromTravel(travel: TravelBO?): String? {
        if (travel == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(TravelBO::class.java).toJson(travel)

    }

    @TypeConverter
    fun toAlcohol(alcoholStr: String?): AlcoholBO? {
        if (alcoholStr == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(AlcoholBO::class.java).fromJson(alcoholStr)
    }

    @TypeConverter
    fun fromAlcohol(alcohol: AlcoholBO?): String? {
        if (alcohol == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(AlcoholBO::class.java).toJson(alcohol)
    }

    @TypeConverter
    fun toAlcoholLevel(alcoholLevelStr: String?): AlcoholLevel? {
        if (alcoholLevelStr == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(AlcoholLevel::class.java).fromJson(alcoholLevelStr)
    }

    @TypeConverter
    fun fromAlcoholLevel(alcoholLevel: AlcoholLevel?): String? {
        if (alcoholLevel == null) {
            return null
        }
        val moshi = Moshi.Builder().build()
        return moshi.adapter(AlcoholLevel::class.java).toJson(alcoholLevel)
    }
}
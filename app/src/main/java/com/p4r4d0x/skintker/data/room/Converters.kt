package com.p4r4d0x.skintker.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
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
    fun toToFoodList(zonesStr: String?): List<String>? {
        if (zonesStr == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(zonesStr, type)
    }

    @TypeConverter
    fun fromFoodList(zones: List<String>?): String? {
        if (zones == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.toJson(zones, type)
    }

    @TypeConverter
    fun toToZonesList(zonesStr: String?): List<IrritatedZone>? {
        if (zonesStr == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<IrritatedZone?>?>() {}.type
        return gson.fromJson(zonesStr, type)
    }

    @TypeConverter
    fun fromZonesList(zones: List<IrritatedZone>?): String? {
        if (zones == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<IrritatedZone?>?>() {}.type
        return gson.toJson(zones, type)
    }

    @TypeConverter
    fun toToWeather(weatherStr: String?): AdditionalDataBO.WeatherBO? {
        if (weatherStr == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(weatherStr, AdditionalDataBO.WeatherBO::class.java)
    }

    @TypeConverter
    fun fromWeather(weather: AdditionalDataBO.WeatherBO?): String? {
        if (weather == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(weather, AdditionalDataBO.WeatherBO::class.java)
    }

    @TypeConverter
    fun toTravel(travelStr: String?): AdditionalDataBO.TravelBO? {
        if (travelStr == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(travelStr, AdditionalDataBO.TravelBO::class.java)
    }

    @TypeConverter
    fun fromTravel(travel: AdditionalDataBO.TravelBO?): String? {
        if (travel == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(travel, AdditionalDataBO.TravelBO::class.java)
    }

    @TypeConverter
    fun toAlcoholLevel(alcoholLevelStr: String?): AlcoholLevel? {
        if (alcoholLevelStr == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(alcoholLevelStr, AlcoholLevel::class.java)
    }

    @TypeConverter
    fun fromAlcoholLevel(alcoholLevel: AlcoholLevel?): String? {
        if (alcoholLevel == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(alcoholLevel, AlcoholLevel::class.java)
    }
}
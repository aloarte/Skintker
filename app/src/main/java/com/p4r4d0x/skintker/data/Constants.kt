package com.p4r4d0x.skintker.data

object Constants {
    //Question constants
    const val FIRST_QUESTION_NUMBER = 1
    const val SECOND_QUESTION_NUMBER = 2
    const val THIRD_QUESTION_NUMBER = 3
    const val FOURTH_QUESTION_NUMBER = 4
    const val FIFTH_QUESTION_NUMBER = 5
    const val SIXTH_QUESTION_NUMBER = 6
    const val SEVENTH_QUESTION_NUMBER = 7
    const val EIGHT_QUESTION_NUMBER = 8

    //Preferences
    const val SKITNKER_PREFERENCES = "preferences_skintker"
    const val PREFERENCES_IRRITATION_NUMBER = "prefs_irritation_number"
    const val PREFERENCES_MIN_LOGS = "prefs_min_logs"
    const val PREFERENCES_FOOD_THRESHOLD = "prefs_food_threshold"
    const val PREFERENCES_ZONES_THRESHOLD = "prefs_zones_threshold"
    const val PREFERENCES_ALCOHOL_THRESHOLD = "prefs_alcohol_threshold"
    const val PREFERENCES_TRAVEL_THRESHOLD = "prefs_travel_threshold"
    const val PREFERENCES_STRESS_VALUE = "prefs_stress_value"
    const val PREFERENCES_STRESS_THRESHOLD = "prefs_stress_threshold"
    const val PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD = "prefs_weather_temperature_threshold"
    const val PREFERENCES_WEATHER_HUMIDITY_THRESHOLD = "prefs_weather_humidity_threshold"

    //Thresholds
    const val DEFAULT_IRRITATION_LEVEL_THRESHOLD = 5
    const val DEFAULT_MIN_LOGS = 12
    const val DEFAULT_FOOD_THRESHOLD = 0.5f
    const val DEFAULT_ZONES_THRESHOLD = 0.5f
    const val DEFAULT_ALCOHOL_THRESHOLD = 0.5f
    const val DEFAULT_TRAVEL_THRESHOLD = 0.5f
    const val DEFAULT_STRESS_VALUE = 7
    const val DEFAULT_STRESS_THRESHOLD = 0.5f
    const val DEFAULT_WEATHER_TEMPERATURE_THRESHOLD = 0.5f
    const val DEFAULT_WEATHER_HUMIDITY_THRESHOLD = 0.5f

    //Files
    const val EXPORT_FILE_NAME = "SkintkerDDBB.csv"

    //Regex
    const val CHARACTER_FILTER_REGEX = "[^\\p{L}\\p{Z}]"
    const val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+"
}
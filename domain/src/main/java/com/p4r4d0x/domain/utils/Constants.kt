package com.p4r4d0x.domain.utils

object Constants {
    //Question constants
    const val FIRST_QUESTION_NUMBER = 1
    const val SECOND_QUESTION_NUMBER = 2
    const val THIRD_QUESTION_NUMBER = 3
    const val FOURTH_QUESTION_NUMBER = 4
    const val FIFTH_QUESTION_NUMBER = 5
    const val SIXTH_QUESTION_NUMBER = 6
    const val SEVENTH_QUESTION_NUMBER = 7
    const val EIGHTH_QUESTION_NUMBER = 8
    const val NINTH_QUESTION_NUMBER = 9
    const val MAX_QUESTION_NUMBER = 9

    //Preferences
    const val SKINTKER_PREFERENCES = "preferences_skintker"
    const val PREFERENCES_USER_ID = "prefs_user_id"
    const val PREFERENCES_ALARM_CREATED = "prefs_alarm_created"
    const val PREFERENCES_ALARM_HOUR = "prefs_alarm_hour"
    const val PREFERENCES_ALARM_MINUTES = "prefs_alarm_minutes"

    //Thresholds
    const val DEFAULT_MIN_LOGS = 12

    //Files
    const val EXPORT_FILE_NAME = "SkintkerDDBB.csv"

    //Regex
    const val CHARACTER_FILTER_REGEX = "[^\\p{L}\\p{Z}]"
    const val REGEX_UNACCENTED = "\\p{InCombiningDiacriticalMarks}+"

    //Labels
    const val LABEL_ID = "id"
    const val LABEL_DATE = "date"
    const val LABEL_IRRITATION = "irritation"
    const val LABEL_IRRITATED_ZONES = "irritatedZones"
    const val LABEL_FOODS = "foods"
    const val LABEL_BEERS = "beers"
    const val LABEL_ALCOHOL = "alcohol"
    const val LABEL_STRESS = "stress"
    const val LABEL_CITY = "city"
    const val LABEL_TRAVELED = "traveled"
    const val LABEL_WEATHER_TEMPERATURE = "weatherTemperature"
    const val LABEL_WEATHER_HUMIDITY = "humidityTemperature"
    const val YEAR_DAYS = 365L

    //Intents
    const val BOOT_INTENT = "android.intent.action.BOOT_COMPLETED"

    //Notifications
    const val CHANNEL_ID = "NotificationChannelID"
    const val CHANNEL_NAME = "Skintker notification channel"
    const val CHANNEL_DESCRIPTION = "Channel to notify Skintker logs reminders"
    const val NOTIFICATION_ID = 1

}
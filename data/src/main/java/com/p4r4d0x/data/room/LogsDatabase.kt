package com.p4r4d0x.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [DailyLog::class, Irritation::class, AdditionalData::class], version = 8)
abstract class LogsDatabase : RoomDatabase() {
    abstract fun dailyLogDao(): DailyLogDao
}

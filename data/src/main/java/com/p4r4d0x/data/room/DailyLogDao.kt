package com.p4r4d0x.data.room

import androidx.room.*
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.utils.getDateWithoutTime

@Dao
interface DailyLogDao {

    @Transaction
    @Query("SELECT * FROM logs_table ORDER BY date DESC")
    suspend fun getAll(): List<DailyLogDetails>

    @Transaction
    @Query("SELECT * FROM logs_table ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getLogListPaginated(limit: Int, offset: Int): List<DailyLogDetails>

    @Transaction
    @Query("DELETE FROM logs_table")
    suspend fun deleteAllLogs()

    @Transaction
    @Query("DELETE FROM additional_data_table")
    suspend fun deleteAllAdditionalData()

    @Transaction
    @Query("DELETE FROM irritation_table")
    suspend fun deleteAllIrritationTable()

    @Transaction
    suspend fun deleteAll() {
        deleteAllAdditionalData()
        deleteAllIrritationTable()
        deleteAllLogs()
    }

    @Transaction
    @Query("SELECT * FROM logs_table WHERE date = :givenDateInLong")
    suspend fun loadLogByDate(givenDateInLong: Long): DailyLogDetails?

    @Transaction
    @Query("SELECT * FROM logs_table INNER JOIN  irritation_table ON logs_table.id=irritation_table.log_id WHERE overallValue>=:irritationLevel")
    suspend fun getLogsWithIrritationLevel(irritationLevel: Int): List<DailyLogDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DailyLog): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLog(log: DailyLog): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIrritation(irritation: Irritation): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateIrritation(irritation: Irritation): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdditionalData(additionalData: AdditionalData): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAdditionalData(additionalData: AdditionalData): Int

    suspend fun insertAllDailyLogs(logs: List<DailyLogBO>): Boolean {
        var allInserted = true
        logs.forEach { log ->
            if (loadLogByDate(log.date.getDateWithoutTime().time) == null) {
                allInserted = allInserted && insertDailyLog(log)
            }
        }
        return allInserted
    }

    suspend fun insertDailyLog(log: DailyLogBO): Boolean {
        val logId = insertLog(fromDomain(log))
        val irritationId = insertIrritation(
            fromDomainObject(irritationBo = log.irritation, logId = logId)
        )
        val additionalId = insertAdditionalData(
            fromDomainObject(additionalDataBO = log.additionalData, logId = logId)
        )

        return logId != -1L && irritationId != -1L && additionalId != -1L
    }

    suspend fun updateDailyLog(log: DailyLogBO): Boolean {
        //Get the already inserted log to get the rest of the item ids
        val insertedLog = loadLogByDate(log.date.time)
        return if (insertedLog != null) {
            val updateLog = updateLog(
                insertedLog.dailyLog.copy(
                    id = insertedLog.dailyLog.id,
                    foodList = log.foodList
                )
            )
            val irritationId = updateIrritation(
                fromDomainObject(
                    irritationBo = log.irritation,
                    logId = insertedLog.irritation.logId
                ).copy(irritationId = insertedLog.irritation.irritationId)
            )
            val additionalId = updateAdditionalData(
                fromDomainObject(
                    additionalDataBO = log.additionalData,
                    logId = insertedLog.additionalData.logId
                ).copy(additionalDataId = insertedLog.additionalData.additionalDataId)
            )
            return irritationId > 0 && additionalId > 0 && updateLog > 0
        } else false
    }


    @Transaction
    @Query("DELETE FROM logs_table WHERE date = :givenDateInLong")
    suspend fun delete(givenDateInLong: Long)

}
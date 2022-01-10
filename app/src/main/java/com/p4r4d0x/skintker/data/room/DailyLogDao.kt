package com.p4r4d0x.skintker.data.room

import androidx.room.*
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

@Dao
interface DailyLogDao {

    @Transaction
    @Query("SELECT * FROM logs_table")
    suspend fun getAll(): List<DailyLogDetails>

    @Transaction
    @Query("SELECT * FROM logs_table WHERE date = :givenDateInLong")
    suspend fun loadLogByDate(givenDateInLong: Long): DailyLogDetails?

//    @Query(
//        "SELECT * FROM user WHERE first_name LIKE :first AND " +
//                "last_name LIKE :last LIMIT 1"
//    )
//    suspend fun findByName(first: String, last: String): DailyLogEntity

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

    suspend fun insertDailyLog(log: DailyLogBO): Boolean {
        val logId = insertLog(fromDomain(log))
        val irritationId = log.irritation?.let { irritation ->
            insertIrritation(
                fromDomainObject(irritationBo = irritation, logId = logId)
            )
        } ?: -1L

        val additionalId = log.additionalData?.let { additionalData ->
            insertAdditionalData(
                fromDomainObject(additionalDataBO = additionalData, logId = logId)
            )
        } ?: -1L
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
            val irritationId = log.irritation?.let { irritation ->
                updateIrritation(
                    fromDomainObject(
                        irritationBo = irritation,
                        logId = insertedLog.irritation.logId
                    ).copy(irritationId = insertedLog.irritation.irritationId)
                )
            } ?: -1
            val additionalId = log.additionalData?.let { additionalData ->
                updateAdditionalData(
                    fromDomainObject(
                        additionalDataBO = additionalData,
                        logId = insertedLog.additionalData.logId
                    ).copy(additionalDataId = insertedLog.additionalData.additionalDataId)
                )
            } ?: -1
            return irritationId > 0 && additionalId > 0 && updateLog > 0
        } else false
    }

    @Delete
    suspend fun delete(user: DailyLog)

}
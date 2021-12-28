package com.p4r4d0x.skintker.data.room

import androidx.room.*
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

@Dao
interface DailyLogDao {

    @Transaction
    @Query("SELECT * FROM logs_table")
    suspend fun getAll(): List<DailyLogDetails>

    @Query("SELECT * FROM logs_table WHERE date = :givenDateInLong")
    suspend fun loadLogByDate(givenDateInLong: Long): DailyLog?

//    @Query(
//        "SELECT * FROM user WHERE first_name LIKE :first AND " +
//                "last_name LIKE :last LIMIT 1"
//    )
//    suspend fun findByName(first: String, last: String): DailyLogEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DailyLog): Long
//
//    @Update
//    suspend fun updateLog(log: DailyLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIrritation(irritation: Irritation): Long
//
//    @Update
//    suspend fun updateIrritation(irritation: Irritation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdditionalData(additionalData: AdditionalData): Long
//
//    @Update
//    suspend fun updateAdditionalData(additionalData: AdditionalData)

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

//    suspend fun updateDailyLog(log: DailyLogBO): Boolean {
//        val logId = updateLog(fromDomain(log))
//        val irritationId = log.irritation?.let { irritation ->
//            updateIrritation(
//                fromDomainObject(irritationBo = irritation, logId = logId)
//            )
//        } ?: -1L
//
//        val additionalId = log.additionalData?.let { additionalData ->
//            updateAdditionalData(
//                fromDomainObject(additionalDataBO = additionalData, logId = logId)
//            )
//        } ?: -1L
//        return logId != -1L && irritationId != -1L && additionalId != -1L
//    }

    @Delete
    suspend fun delete(user: DailyLog)

}
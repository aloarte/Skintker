package com.p4r4d0x.skintker.data.room

import androidx.room.*

@Dao
interface DailyLogDao {

    @Transaction
    @Query("SELECT * FROM DailyLog")
    suspend fun getAll(): List<DailyLogDetails>

    @Query("SELECT * FROM DailyLog WHERE log_id IN (:logIds)")
    suspend fun loadAllByIds(logIds: IntArray): List<DailyLog>

//    @Query(
//        "SELECT * FROM user WHERE first_name LIKE :first AND " +
//                "last_name LIKE :last LIMIT 1"
//    )
//    suspend fun findByName(first: String, last: String): DailyLogEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DailyLog)

    @Delete
    suspend fun delete(user: DailyLog)

}
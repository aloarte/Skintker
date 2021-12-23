package com.p4r4d0x.skintker.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface IrritationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addIrritation(irritation: Irritation)
}
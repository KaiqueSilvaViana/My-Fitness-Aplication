package com.rose.myfitnesslifestyle.modelBD

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CalcDao {

    @Insert
    fun insert(calc: Calc)

    @Query("SELECT * FROM Calc WHERE type = :type")
    fun getRegisterType(type: String) : List<Calc>

    @Query("SELECT * FROM Calc ORDER BY created_date DESC")
    fun getAllRegisters() : List<Calc>

    @Delete
    fun delete(calc: Calc): Int

    @Update
    fun update(calc: Calc)
}
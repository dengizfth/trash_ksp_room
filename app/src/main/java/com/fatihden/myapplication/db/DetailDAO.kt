package com.fatihden.myapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fatihden.myapplication.model.Detail
@Dao
interface DetailDAO {
    @Query("SELECT * FROM Detail")
    fun getAll() : List<Detail>

    @Query("SELECT * FROM DETAIL WHERE id = :id")
    fun findById(id : Int) : Detail

    @Insert
    fun insert(detail:Detail)

    @Delete
    fun delete(detail: Detail)



}
package com.fatihden.myapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fatihden.myapplication.model.Detail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface DetailDAO {
    @Query("SELECT * FROM Detail")
    fun getAll() : Flowable<List<Detail>>

    @Query("SELECT * FROM DETAIL WHERE id = :id")
    fun findById(id : Int) :Flowable<Detail>

    @Insert
    fun insert(detail:Detail) : Completable

    @Delete
    fun delete(detail: Detail) : Completable



}
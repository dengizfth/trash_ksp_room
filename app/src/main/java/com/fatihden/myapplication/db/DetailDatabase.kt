package com.fatihden.myapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fatihden.myapplication.model.Detail

@Database(entities = [Detail::class],version = 1)
abstract class DetailDatabase : RoomDatabase() {
    abstract fun detailDao() : DetailDAO

}
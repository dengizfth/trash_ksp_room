package com.fatihden.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Detail(
    /*@PrimaryKey
    val id:Int,
    */
    @ColumnInfo(name = "name")
    val name:String?,

    @ColumnInfo("detail")
    val detail:String?,

    @ColumnInfo("img")
    val img : ByteArray?
){
    // id'yi Kendimiz oluşturmak istiyorsak , primary Constructer içersine '@PrimaryKey val id:Int' şeklinde eklenmeli


    @PrimaryKey(autoGenerate = true) //Database kendisi id oluştursun .
    var id =0 // Default id
}

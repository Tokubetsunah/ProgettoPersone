package com.example.progettopersone

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persona_table")
data class Persona(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "nome") val nome:String?,
    @ColumnInfo(name = "cognome") val cognome:String?,
    @ColumnInfo(name = "dataNascita") val dataNascita:String?,
    @ColumnInfo(name = "sesso") val sesso:String?,
    @ColumnInfo(name = "cittaNascita") val cittaNascita:String?,
    @ColumnInfo(name = "provinciaNascita") val provinciaNascita:String?,

    )

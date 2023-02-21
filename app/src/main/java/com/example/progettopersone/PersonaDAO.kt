package com.example.progettopersone

import android.app.Person
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonaDAO {
    //Qui ci sono tutte le query implementate dal nostro database
    @Query("SELECT * FROM persona_table")
    fun getAll(): List<Persona>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(persona: Persona)
}
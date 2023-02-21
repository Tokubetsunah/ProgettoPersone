package com.example.progettopersone

import android.app.Person
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [Persona :: class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun personaDAO() : PersonaDAO

    companion object{

         @Volatile
         private var INSTANCE : AppDatabase? = null

         @OptIn(InternalCoroutinesApi::class)
         fun getDatabase(context: Context): AppDatabase{

             val tempInstance = INSTANCE
             if(tempInstance != null){

                 return tempInstance

             }
             synchronized(this){

                 val instance = Room.databaseBuilder(
                     context.applicationContext,
                     AppDatabase::class.java,
                     "app_database"
                 ).build()
                 INSTANCE = instance
                 return instance

             }
         }
    }

}
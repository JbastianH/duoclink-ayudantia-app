package com.joel.duoclinkayudantia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joel.duoclinkayudantia.model.Ayudantia

@Database(entities = [Ayudantia::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ayudantiaDao(): AyudantiaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ayudantias.db"
                ).build().also { INSTANCE = it }
            }
    }
}
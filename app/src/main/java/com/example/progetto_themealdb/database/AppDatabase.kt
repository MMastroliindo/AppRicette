package com.example.progetto_themealdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.progetto_themealdb.model.FavoriteMeal
import com.example.progetto_themealdb.model.BestScore
//Utilizzo di Room per salvare i dati in locale
@Database(entities = [FavoriteMeal::class, BestScore::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteMealDao(): FavoriteMealDao
    abstract fun bestScoreDao(): BestScoreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

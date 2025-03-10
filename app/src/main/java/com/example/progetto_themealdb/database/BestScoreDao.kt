package com.example.progetto_themealdb.database

import androidx.room.*
import com.example.progetto_themealdb.model.BestScore
//Interfaccia per la gestione della tabella best_score
@Dao
interface BestScoreDao {
    @Query("SELECT * FROM best_score WHERE id = 1")
    suspend fun getBestScore(): BestScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBestScore(score: BestScore)

    @Update
    suspend fun updateBestScore(score: BestScore)
}

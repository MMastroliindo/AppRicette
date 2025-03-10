package com.example.progetto_themealdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "best_score")
data class BestScore(
    @PrimaryKey val id: Int = 1,
    val score: Int = 0
)

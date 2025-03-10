package com.example.progetto_themealdb.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.progetto_themealdb.api.RetrofitInstance
import com.example.progetto_themealdb.database.AppDatabase
import com.example.progetto_themealdb.database.FavoriteMealDao
import com.example.progetto_themealdb.model.FavoriteMeal
import com.example.progetto_themealdb.model.Meal
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MealViewModel(application: Application) : AndroidViewModel(application) {

    //Database per salvare i preferiti
    private val favoriteMealDao: FavoriteMealDao = AppDatabase.getDatabase(application).favoriteMealDao()
    val favoriteMeals: Flow<List<FavoriteMeal>> = favoriteMealDao.getAllFavorites()
    private val Categories = MutableStateFlow<List<Meal>>(emptyList())
    val categoryMeals: StateFlow<List<Meal>> = Categories
    private val Results = MutableStateFlow<List<Meal>>(emptyList())
    private val Details = MutableStateFlow<Meal?>(null)
    val mealDetails: StateFlow<Meal?> = Details

    private val Meal = MutableStateFlow<Meal?>(null)
    val randomMeal: StateFlow<Meal?> = Meal
    private val Area = MutableStateFlow<List<Meal>>(emptyList())
    private val ResultsF = MutableStateFlow<List<Meal>>(emptyList())
    val filteredResults: StateFlow<List<Meal>> = ResultsF

    private var lastQuery: String = ""
    private var lastSelectedArea: String = "All"

    private val defaultCategory = "Beef"

    init {
        fetchRandomMeal()
        fetchMealsByCategory(defaultCategory)
    }

    fun fetchMealsByCategory(category: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.filterByCategory(category)
                if (response.isSuccessful) {
                    Categories.value = response.body()?.meals ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchMeals(query: String) {
        lastQuery = query
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchMeals(query)
                if (response.isSuccessful) {
                    Results.value = response.body()?.meals ?: emptyList()
                    applyFilters()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMealDetails(mealId)
                if (response.isSuccessful) {
                    Details.value = response.body()?.meals?.firstOrNull()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRandomMeal() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getRandomMeal()
                if (response.isSuccessful) {
                    Meal.value = response.body()?.meals?.firstOrNull()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun addToFavorites(meal: FavoriteMeal) {
        viewModelScope.launch {
            favoriteMealDao.aggiungiPreferiti(meal)
        }
    }

    fun removeFromFavorites(meal: FavoriteMeal) {
        viewModelScope.launch {
            favoriteMealDao.removeFromFavorites(meal)
        }
    }

    fun isFavorite(mealId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            favoriteMealDao.isFavorite(mealId).collect { isFav ->
                onResult(isFav)
            }
        }
    }
    fun fetchMealsByArea(area: String) {
        lastSelectedArea = area
        viewModelScope.launch {
            try {
                val response = if (area == "All") {
                    RetrofitInstance.api.searchMeals(lastQuery)
                } else {
                    RetrofitInstance.api.filterByArea(area)
                }

                if (response.isSuccessful) {
                    Area.value = response.body()?.meals ?: emptyList()
                    applyFilters()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun applyFilters() {
        val filtered = if (lastSelectedArea == "All") {
            Results.value
        } else {
            Results.value.filter { meal ->
                Area.value.any { it.idMeal == meal.idMeal }
            }
        }
        ResultsF.value = filtered
    }
}

package com.example.progetto_themealdb.model

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("idMeal") val idMeal: String,
    @SerializedName("strMeal") var strMeal: String,
    @SerializedName("strCategory") val strCategory: String?,
    @SerializedName("strArea") val strArea: String?,
    @SerializedName("strMealThumb") val strMealThumb: String?,
    @SerializedName("strInstructions") var strInstructions: String?,
    @SerializedName("strIngredient1") var strIngredient1: String?,
    @SerializedName("strIngredient2") var strIngredient2: String?,
    @SerializedName("strIngredient3") var strIngredient3: String?,
    @SerializedName("strIngredient4") var strIngredient4: String?,
    @SerializedName("strIngredient5") var strIngredient5: String?,
    @SerializedName("strIngredient6") var strIngredient6: String?,
    @SerializedName("strIngredient7") var strIngredient7: String?,
    @SerializedName("strIngredient8") var strIngredient8: String?,
    @SerializedName("strIngredient9") var strIngredient9: String?,
    @SerializedName("strIngredient10") var strIngredient10: String?
)

package com.example.progetto_themealdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.progetto_themealdb.UIFile.HomeScreen
import com.example.progetto_themealdb.UIFile.SearchResultsScreen
import com.example.progetto_themealdb.UIFile.MealDetailScreen
import com.example.progetto_themealdb.UIFile.FavoritesScreen
import com.example.progetto_themealdb.UIFile.QuizScreen
import com.example.progetto_themealdb.database.AppDatabase
import com.example.progetto_themealdb.ui.theme.Progetto_TheMealDBTheme
import com.example.progetto_themealdb.viewmodel.MealViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[MealViewModel::class.java]

        // Ottieni il database e il DAO
        val database = AppDatabase.getDatabase(this)
        val bestScoreDao = database.bestScoreDao()

        setContent {
            Progetto_TheMealDBTheme {
                val navController: NavHostController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController, viewModel)
                    }
                    composable("search/{query}") { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        SearchResultsScreen(
                            query = query,
                            viewModel = viewModel,
                            navController = navController,
                            onMealClick = { mealId -> navController.navigate("mealDetail/$mealId") }
                        )
                    }
                    composable("favorites") {
                        FavoritesScreen(viewModel, navController)
                    }
                    composable("quiz") {
                        QuizScreen(navController, viewModel, bestScoreDao)
                    }

                    composable("mealDetail/{mealId}") { backStackEntry ->
                        val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
                        MealDetailScreen(mealId, viewModel, navController)
                    }
                }
            }
        }
    }
}

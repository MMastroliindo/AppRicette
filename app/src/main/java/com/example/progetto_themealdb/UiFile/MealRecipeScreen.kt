package com.example.progetto_themealdb.UIFile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.progetto_themealdb.model.FavoriteMeal
import com.example.progetto_themealdb.viewmodel.MealViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(mealId: String, viewModel: MealViewModel, navController: NavController) {
    val meal by viewModel.mealDetails.collectAsState()
    val isFavorite = remember { mutableStateOf(false) }
    val showSuccessMessage = remember { mutableStateOf(false) }

    LaunchedEffect(mealId) {
        viewModel.fetchMealDetails(mealId)
        viewModel.isFavorite(mealId) { isFavorite.value = it }
    }
    LaunchedEffect(showSuccessMessage.value) {
        if (showSuccessMessage.value) {
            kotlinx.coroutines.delay(2000)
            showSuccessMessage.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Recipe Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(40.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            meal?.let { mealData ->
                                if (isFavorite.value) {
                                    mealData.strMealThumb?.let { thumb ->
                                        viewModel.removeFromFavorites(
                                            FavoriteMeal(mealData.idMeal, mealData.strMeal, thumb)
                                        )
                                    }
                                } else {
                                    mealData.strMealThumb?.let { thumb ->
                                        viewModel.addToFavorites(
                                            FavoriteMeal(mealData.idMeal, mealData.strMeal, thumb)
                                        )
                                    }
                                }
                                isFavorite.value = !isFavorite.value
                                showSuccessMessage.value = true
                            }
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                if (isFavorite.value) Color(0xFFFC7753)
                                else MaterialTheme.colorScheme.surface
                            )
                    ) {
                        Icon(
                            if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite.value) Color.White else Color(0xFFFC7753)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            meal?.let { mealData ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(mealData.strMealThumb),
                                contentDescription = mealData.strMeal,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.6f)
                                            ),
                                            startY = 0f,
                                            endY = 700f
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = mealData.strMeal ?: "Unknown Meal",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 26.sp,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp)),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFFC7753).copy(alpha = 0.9f)
                                        )
                                    ) {
                                        Text(
                                            text = mealData.strCategory ?: "Unknown",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Card(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp)),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                                        )
                                    ) {
                                        Text(
                                            text = mealData.strArea ?: "Unknown",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Content Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            SectionTitle(
                                title = "Ingredients",
                                icon = Icons.Rounded.Restaurant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Ingredients with measurements
                            val ingredients = mutableListOf<String>()

                            if (!mealData.strIngredient1.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient1!!)
                            }
                            if (!mealData.strIngredient2.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient2!!)
                            }
                            if (!mealData.strIngredient3.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient3!!)
                            }
                            if (!mealData.strIngredient4.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient4!!)
                            }
                            if (!mealData.strIngredient5.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient5!!)
                            }
                            if (!mealData.strIngredient6.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient6!!)
                            }
                            if (!mealData.strIngredient7.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient7!!)
                            }
                            if (!mealData.strIngredient8.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient8!!)
                            }
                            if (!mealData.strIngredient9.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient9!!)
                            }
                            if (!mealData.strIngredient10.isNullOrBlank()) {
                                ingredients.add(mealData.strIngredient10!!)
                            }

                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(animationSpec = tween(durationMillis = 500))
                            ) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 220.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        .padding(8.dp)
                                ) {
                                    items(ingredients) { ingredient ->
                                        Card(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .fillMaxWidth(),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                            ),
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = 2.dp
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(8.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = ingredient,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            SectionTitle(title = "Instructions")

                            Spacer(modifier = Modifier.height(16.dp))
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(durationMillis = 400)
                                ) + fadeIn()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 4.dp
                                    )
                                ) {
                                    Text(
                                        text = mealData.strInstructions ?: "No instructions available",
                                        fontSize = 15.sp,
                                        lineHeight = 24.sp,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }

                // Success message
                AnimatedVisibility(
                    visible = showSuccessMessage.value,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFavorite.value) Color(0xFFFC7753) else Color(0xFF4CAF50)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = if (isFavorite.value) "Added to favorites" else "Removed from favorites",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFFC7753),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }

@Composable
fun SectionTitle(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(24.dp)
                .background(
                    color = Color(0xFFFC7753),
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        icon?.let {
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color(0xFFFC7753),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
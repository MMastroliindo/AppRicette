package com.example.progetto_themealdb.UIFile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.example.progetto_themealdb.viewmodel.MealViewModel
import com.example.progetto_themealdb.database.BestScoreDao
import com.example.progetto_themealdb.model.BestScore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavController, viewModel: MealViewModel, bestScoreDao: BestScoreDao) {
    var score by remember { mutableStateOf(0) }
    var bestScore by remember { mutableStateOf(0) }
    var currentMeal by remember { mutableStateOf(viewModel.randomMeal.value) }
    var correctAnswer by remember { mutableStateOf(currentMeal?.strArea ?: "") }
    var options by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showNextButton by remember { mutableStateOf(false) }
    var gameStarted by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }
    var showCorrectAnswer by remember { mutableStateOf(false) }
    var incorrectChoice by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Colori per il gradiente
    val gradientColors = listOf(Color(0xFF1A237E), Color(0xFF3949AB), Color(0xFF5C6BC0))
    val accentColor = Color(0xFF00C853)
    val errorColor = Color(0xFFD50000)
    val neutralColor = Color(0xFF6200EE)
    val bestScoreColor = Color(0xFFFFD700)  // Gold color

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            bestScore = bestScoreDao.getBestScore()?.score ?: 0
        }
    }

    fun loadNewQuestion() {
        coroutineScope.launch {
            viewModel.fetchRandomMeal()
            currentMeal = viewModel.randomMeal.value
            correctAnswer = currentMeal?.strArea ?: ""
            options = generateOptions(correctAnswer)
            selectedAnswer = null
            showNextButton = false
            gameOver = false
            showCorrectAnswer = false
            incorrectChoice = null
        }
    }

    LaunchedEffect(gameStarted) {
        if (gameStarted) loadNewQuestion()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quiz: Guess the Country!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Best Score Card con dimensioni ridotte
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = bestScoreColor,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Best Score: $bestScore",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = bestScoreColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(
                    visible = !gameStarted,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "Welcome to the Quiz!",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "Test your knowledge of international cuisine!",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { gameStarted = true; score = 0 },
                            modifier = Modifier
                                .padding(16.dp)
                                .height(52.dp)
                                .shadow(8.dp, RoundedCornerShape(26.dp)),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text(
                                "Start Game",
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = gameStarted,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Score display con dimensioni ridotte
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(vertical = 4.dp)
                                .shadow(4.dp, CircleShape),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E88E5)
                            ),
                            shape = CircleShape
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Score: $score",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        currentMeal?.let { meal ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .shadow(8.dp),
                                elevation = CardDefaults.cardElevation(0.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Immagine più piccola
                                    Image(
                                        painter = rememberAsyncImagePainter(meal.strMealThumb),
                                        contentDescription = meal.strMeal,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(180.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .shadow(6.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        meal.strMeal ?: "Unknown",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )

                                    Text(
                                        "From which country is this dish?",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Mostra il messaggio di risposta corretta
                            AnimatedVisibility(visible = showCorrectAnswer) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFFF9C4)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "The correct answer is: $correctAnswer",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF33691E)
                                        )
                                    }
                                }
                            }

                            // Option buttons con altezza ridotta
                            options.forEach { option ->
                                val buttonColor = when {
                                    selectedAnswer == option && option == correctAnswer -> accentColor
                                    selectedAnswer == option -> errorColor
                                    selectedAnswer != null && option == correctAnswer && showCorrectAnswer -> accentColor
                                    else -> neutralColor
                                }

                                Button(
                                    onClick = {
                                        if (selectedAnswer == null) {
                                            selectedAnswer = option
                                            if (option == correctAnswer) {
                                                score++
                                                if (score > bestScore) {
                                                    bestScore = score
                                                    coroutineScope.launch {
                                                        bestScoreDao.updateBestScore(BestScore(id = 1, score = bestScore))
                                                    }
                                                }
                                                showNextButton = true
                                            } else {
                                                incorrectChoice = option
                                                gameOver = true
                                                showCorrectAnswer = true
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .height(48.dp)
                                        .shadow(3.dp, RoundedCornerShape(10.dp)),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                                    enabled = selectedAnswer == null
                                ) {
                                    Text(
                                        option,
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (showNextButton) {
                                    Button(
                                        onClick = { loadNewQuestion() },
                                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                                        modifier = Modifier
                                            .fillMaxWidth(0.7f)
                                            .height(48.dp)
                                            .shadow(6.dp, RoundedCornerShape(24.dp)),
                                        shape = RoundedCornerShape(24.dp)
                                    ) {
                                        Text(
                                            "Next Question",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (gameOver) {
                                    Button(
                                        onClick = { gameStarted = false },
                                        colors = ButtonDefaults.buttonColors(containerColor = errorColor),
                                        modifier = Modifier
                                            .fillMaxWidth(0.7f)
                                            .height(48.dp)
                                            .shadow(6.dp, RoundedCornerShape(24.dp)),
                                        shape = RoundedCornerShape(24.dp)
                                    ) {
                                        Text(
                                            "Try Again",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // Aggiunto spazio alla fine per scrollare oltre l'ultimo pulsante
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

fun generateOptions(correctAnswer: String): List<String> {
    val areas = listOf("American", "British", "Canadian", "Chinese", "Croatian", "Dutch", "Egyptian", "Filipino", "French", "Greek", "Indian", "Irish", "Italian", "Jamaican", "Japanese", "Kenyan", "Malaysian", "Mexican", "Moroccan", "Polish", "Portuguese", "Russian", "Spanish", "Thai", "Tunisian", "Turkish", "Ukrainian", "Uruguayan", "Vietnamese")
    return (areas - correctAnswer).shuffled().take(3) + correctAnswer
}
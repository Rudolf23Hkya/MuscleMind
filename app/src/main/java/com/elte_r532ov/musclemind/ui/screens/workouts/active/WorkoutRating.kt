package com.elte_r532ov.musclemind.ui.screens.workouts.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.ui.screens.workouts.shared.ExerciseItemItem
import com.elte_r532ov.musclemind.ui.util.BottomNavBar
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.handleUiEvent

@Composable
fun WorkoutRating(
    onNavigate: NavHostController,
    viewModel: SharedActiveWorkoutViewModel =
        hiltViewModel(onNavigate.getBackStackEntry(Routes.WORKOUT_ACTIVE_ROUTE))
) {
    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    val exercises = viewModel.selectedExercises.observeAsState(initial = emptyList())
    //val ratings by viewModel.ratings.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Rate Your Workout",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        bottomBar = {
            Button(
                onClick = { /* Save action here */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Save")
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            /*
            LazyColumn {
                itemsIndexed(exercises) { index, exercise ->
                    RatingItem(
                        exerciseName = exercise,
                        rating = ratings[index],
                        onRatingChange = { newRating ->
                            viewModel.updateRating(index, newRating)
                        }
                    )
                }

            }
             */
        }
    }
}

@Composable
fun RatingItem(
    exerciseName: String,
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = exerciseName, style = MaterialTheme.typography.bodyMedium)
        Row {
            (1..5).forEach { index ->
                IconButton(
                    onClick = { onRatingChange(index) }
                ) {
                    Icon(
                        imageVector = if (index <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
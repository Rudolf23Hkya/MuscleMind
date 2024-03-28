package com.elte_r532ov.musclemind.ui.screens.workouts.workoutInProgress

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.data.userData.ExperienceLevel
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Category
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Exercise
import com.elte_r532ov.musclemind.data.workoutsAndExercises.MuscleGroup
import com.elte_r532ov.musclemind.util.UiEvent

@Composable
fun WorkoutInProgress(
    onNavigate: NavHostController,
    viewModel: WorkoutInProgressViewModel = hiltViewModel()
) {
    val workoutId = 1L
    viewModel.initWorkoutId(workoutId)
    Text(text = workoutId.toString())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate.navigate(event.route)
                else -> Unit
            }
        }
    }
    val selectedExercises = viewModel.selectedExercise.observeAsState(initial =
    Exercise(0,"",
        Category.OWN_BODY_WEIGHT,MuscleGroup.UPPER_BODY,
        ExperienceLevel.INTERMEDIATE,0,0,""))

    val context = LocalContext.current

    val drawableResId =context.resources.getIdentifier(selectedExercises.value.drawablePicName,
        "drawable", context.packageName)
    val workoutName = selectedExercises.value.name
    val muscleGroup = selectedExercises.value.muscleGroup

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = drawableResId),
                contentDescription = null, // Describe the image for accessibility
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Text(
                text = workoutName,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Text(
                text = muscleGroup.toString(),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.skippedExercise() },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text("Skip", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = { viewModel.nextExercise() },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Next", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}

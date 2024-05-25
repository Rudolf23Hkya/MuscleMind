package com.elte_r532ov.musclemind.ui.screens.workouts.active

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toLowerCase
import androidx.core.content.res.TypedArrayUtils.getResourceId
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.handleUiEvent

@SuppressLint("DiscouragedApi")
@Composable
fun WorkoutInProgress(
    onNavigate: NavHostController,
    viewModel: SharedActiveWorkoutViewModel =
        hiltViewModel(onNavigate.getBackStackEntry(Routes.WORKOUT_ACTIVE_ROUTE))
) {
    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    val exerciseName by viewModel.exerciseName.observeAsState("default_exercise")
    val imageUrl by viewModel.imageUrl.observeAsState("")
    val remainingTime by viewModel.remainingTime.observeAsState(0)
    
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(imageUrl, "drawable", context.packageName)


    Scaffold(
        topBar = {
            Text(
                text = exerciseName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.onExerciseSkipped() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Skip")
                }
                Button(
                    onClick = { viewModel.onExerciseNext() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Next →")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                if (imageUrl.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                } else {
                    Text(text = "Image not found", color = MaterialTheme.colorScheme.onError)
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${remainingTime}s",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = remainingTime / 10f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
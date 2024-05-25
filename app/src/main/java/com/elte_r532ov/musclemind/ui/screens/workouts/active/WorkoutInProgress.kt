package com.elte_r532ov.musclemind.ui.screens.workouts.active

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = exerciseName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Skip",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Button(
                    onClick = { viewModel.onExerciseNext() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Next",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(Icons.Filled.ChevronRight, "Arrow")
                }
            }
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            )  {
                if (imageUrl.isNotEmpty() && imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = exerciseName,
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .fillMaxWidth()
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
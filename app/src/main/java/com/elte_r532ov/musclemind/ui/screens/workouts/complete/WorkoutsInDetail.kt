package com.elte_r532ov.musclemind.ui.screens.workouts.complete

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.sp
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.ui.util.BottomNavBar
import com.elte_r532ov.musclemind.ui.screens.workouts.shared.ExerciseItemItem
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import com.elte_r532ov.musclemind.ui.util.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WorkoutInDetail(
    workoutId : Long,
    onNavigate: NavHostController,
    viewModel: WorkoutInDetailSharedViewModel = hiltViewModel(onNavigate.getBackStackEntry(Routes.WORKOUTS_IN_PROGRESS))
){
    viewModel.initWorkoutId(workoutId)

    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    val selectedExercises = viewModel.selectedExercises.observeAsState(initial = emptyList())
    var showFAB by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            onNavigate.currentDestination?.route?.let {
                BottomNavBar(it, onNavigate)
            }
        },
        snackbarHost ={ SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            // Only render the Floating Action Btn if the exercises are loaded
            if (showFAB) {
                FloatingActionButton(
                    onClick = { onNavigate.navigate(Routes.WORKOUTS_START) },
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                ) {
                    Icon(Icons.Filled.PlayArrow, "Start")
                }
            }
        }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 54.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "",
                fontSize = 32.sp,
                fontFamily = myFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        LazyColumn {
            items(selectedExercises.value) { ex ->
                ExerciseItemItem(exercise = ex)
            }
        }
    }
}
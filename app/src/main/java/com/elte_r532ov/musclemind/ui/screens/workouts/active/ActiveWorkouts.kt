package com.elte_r532ov.musclemind.ui.screens.workouts.active

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.myFontFamily
import androidx.compose.runtime.livedata.observeAsState
import com.elte_r532ov.musclemind.ui.util.BottomNavBar
import com.elte_r532ov.musclemind.ui.screens.workouts.sharedElements.WorkoutItem
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import com.elte_r532ov.musclemind.util.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ActiveWorkouts(
    onNavigate: NavHostController,
    viewModel: ActiveWorkoutsViewModel = hiltViewModel()
) {
    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    val userName by viewModel.userNameLiveData.observeAsState("")
    val activeWorkouts = viewModel.activeWorkoutLiveData.observeAsState(initial = emptyList())


    Scaffold(
        bottomBar = {
            onNavigate.currentDestination?.route?.let {
                BottomNavBar(it, onNavigate)
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Text(
                        "Welcome  ",
                        fontSize = 32.sp,
                        fontFamily = myFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 40.sp
                    )
                    Text(
                        text = userName,
                        fontSize = 32.sp,
                        fontFamily = myFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        lineHeight = 40.sp
                    )
                }
            }
        },
        floatingActionButton = {
            // Only render the Floating Action Btn if the exercises are loaded
            FloatingActionButton(
                onClick = { onNavigate.navigate(Routes.CREATE_WORKOUT_DATA) },
                containerColor = MaterialTheme.colorScheme.inversePrimary
            ) {
                Icon(Icons.Filled.Add, "Create Workout")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Use a gradient instead for the background
                .padding(innerPadding), // Padding for insets
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn {
                items(activeWorkouts.value) { workout ->
                    WorkoutItem(workout = workout, navigation = onNavigate)
                }
            }
        }
    }
}

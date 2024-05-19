package com.elte_r532ov.musclemind.ui.screens.workouts.active

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.util.UiEvent
import androidx.compose.runtime.livedata.observeAsState
import com.elte_r532ov.musclemind.ui.BottomNavBar
import com.elte_r532ov.musclemind.ui.screens.workouts.sharedElements.WorkoutItem
import com.elte_r532ov.musclemind.ui.util.handleUiEvent

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
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Use a gradient instead for the background
                .padding(innerPadding), // Padding for insets
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 54.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Welcome $userName",
                    fontSize = 32.sp,
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 40.sp
                )
            }
            LazyColumn {
                items(activeWorkouts.value) { workout ->
                    WorkoutItem(workout = workout, navigation = onNavigate)
                }
            }
        }
    }
}

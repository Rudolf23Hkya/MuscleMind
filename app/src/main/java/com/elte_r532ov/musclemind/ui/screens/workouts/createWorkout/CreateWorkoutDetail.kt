package com.elte_r532ov.musclemind.ui.screens.workouts.createWorkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.ui.BottomNavBar
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import com.elte_r532ov.musclemind.util.Routes

@Composable
fun CreateWorkoutDetail(
    onNavigate: NavHostController,
    viewModel: SharedCreateWorkoutViewModel =
        hiltViewModel(onNavigate.getBackStackEntry(Routes.CREATE_WORKOUT_ROUTE))
) {
    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)


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
                }
            }
        }
    ){ contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
        }
    }
}
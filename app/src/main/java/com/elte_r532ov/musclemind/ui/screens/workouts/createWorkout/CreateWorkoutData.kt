package com.elte_r532ov.musclemind.ui.screens.workouts.createWorkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.ui.screens.register.RegisterEvent
import com.elte_r532ov.musclemind.ui.screens.register.UserInfoTextField
import com.elte_r532ov.musclemind.ui.util.BottomNavBar
import com.elte_r532ov.musclemind.ui.util.OptiListElement
import com.elte_r532ov.musclemind.ui.util.OptionalOptionList
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import com.elte_r532ov.musclemind.util.Routes

@Composable
fun CreateWorkoutData(
    onNavigate: NavHostController,
    viewModel: SharedCreateWorkoutViewModel =
        hiltViewModel(onNavigate.getBackStackEntry(Routes.CREATE_WORKOUT_ROUTE))
) {
    // Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    var equipmentList by remember {
        mutableStateOf(
            listOf(
                OptiListElement("Trx", false),
                OptiListElement("Dumbbells", false),
            )
        )
    }
    var doWeekly by remember { mutableStateOf("") }

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
                Text(
                    "What kind of workout would you like to create?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    "For first, do you have any equipment?",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(44.dp))

                // Change Row to Column
                equipmentList.forEachIndexed { index, equipment ->
                    OptionalOptionList(
                        text = equipment.name,
                        isSelected = equipment.isSelected,
                        onSelect = {
                            equipmentList = equipmentList.mapIndexed { i, d ->
                                if (i == index) d.copy(isSelected = !d.isSelected) else d
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                UserInfoTextField(
                    value = doWeekly,
                    onValueChange = { doWeekly = it },
                    label = "How often do you work out weekly? (1-7)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(80.dp))

                Button(
                    onClick = { viewModel.workoutDataSet(
                        equipmentList.filter { it.isSelected },doWeekly) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Next", fontSize = 18.sp)
                }
            }
        }
    }
}
package com.elte_r532ov.musclemind.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.data.ExperienceLevel
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent

@Composable
fun ExperienceSelectionScreen(
    onNavigate: NavHostController,
    viewModel: SharedRegisterViewModel = hiltViewModel(onNavigate.getBackStackEntry(Routes.REGISTRATION_ROUTE))
) {
    //Snack bar
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // This LaunchedEffect listens to the UI events and performs actions accordingly
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate.navigate(event.route)
                is UiEvent.ShowSnackbar -> snackbarMessage = event.message
                is UiEvent.ErrorOccured -> snackbarMessage = event.errMsg
            }
        }
    }

    val options = ExperienceLevel.entries.toTypedArray()
    var selectedOption by remember { mutableStateOf<ExperienceLevel?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "How experienced are you?",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            options.forEach { option ->
                ExperienceOption(
                    text = option.toString(),
                    isSelected = selectedOption == option,
                    onSelect = { selectedOption = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(
                onClick = { viewModel.onEvent(RegisterEvent.onExperienceChosen(selectedOption)) },
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

@Composable
fun ExperienceOption(
    text: String,
    isSelected: Boolean,
    onSelect: (ExperienceLevel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = if (isSelected) Color.DarkGray else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                val enumValue = enumValueOf<ExperienceLevel>(text)
                onSelect(enumValue)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

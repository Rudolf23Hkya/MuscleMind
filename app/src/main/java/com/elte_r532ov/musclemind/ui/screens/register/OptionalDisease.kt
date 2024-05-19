package com.elte_r532ov.musclemind.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
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
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import com.elte_r532ov.musclemind.util.Routes

// This data class is only part of the view
data class DiseaseListElemet(
    val name: String,
    val isSelected: Boolean
)

@Composable
fun DiseaseSelectionScreen(
    onNavigate: NavHostController,
    viewModel: SharedRegisterViewModel = hiltViewModel(onNavigate.getBackStackEntry(Routes.REGISTRATION_ROUTE))
) {
    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    var diseases by remember {
        mutableStateOf(
            listOf(
                DiseaseListElemet("Asthma", false),
                DiseaseListElemet("Bad Knee", false),
                DiseaseListElemet("Cardiovascular Disease", false),
                DiseaseListElemet("Osteoporosis", false)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select your diseases",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            diseases.forEachIndexed { index, disease ->
                DiseaseOption(
                    text = disease.name,
                    isSelected = disease.isSelected,
                    onSelect = {
                        diseases = diseases.mapIndexed { i, d ->
                            if (i == index) d.copy(isSelected = !d.isSelected) else d
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(
                onClick = { viewModel.onEvent(RegisterEvent.onDiseasesChosen(diseases.filter { it.isSelected })) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Next", fontSize = 18.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    SnackbarHost(hostState = snackBarHostState)
}

@Composable
fun DiseaseOption(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelect() }
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
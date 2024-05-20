package com.elte_r532ov.musclemind.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.ui.util.OptiListElement
import com.elte_r532ov.musclemind.ui.util.OptionalOptionList
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import com.elte_r532ov.musclemind.util.Routes

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
                OptiListElement("Asthma", false),
                OptiListElement("Bad Knee", false),
                OptiListElement("Cardiovascular Disease", false),
                OptiListElement("Osteoporosis", false)
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

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "(Optional)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            diseases.forEachIndexed { index, disease ->
                OptionalOptionList(
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
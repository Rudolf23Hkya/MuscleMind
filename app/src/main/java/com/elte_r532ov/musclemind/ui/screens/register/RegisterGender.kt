package com.elte_r532ov.musclemind.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.R
import com.elte_r532ov.musclemind.data.enums.Gender
import androidx.compose.ui.input.pointer.pointerInput
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import com.elte_r532ov.musclemind.ui.util.Routes


@Composable
fun RegisterGender(
    onNavigate: NavHostController,
    viewModel: SharedRegisterViewModel = hiltViewModel(onNavigate.getBackStackEntry(Routes.REGISTRATION_ROUTE))
) {
    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)


    val maleImagePainter = painterResource(id = R.drawable.boy)
    val femaleImagePainter = painterResource(id = R.drawable.girl)


    val selectedGender = remember { mutableStateOf<Gender?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tell us about yourself",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We need to know you better to provide you with the best training experience.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Male Option
        GenderOption(
            imagePainter = maleImagePainter,
            label = "Male",
            isSelected = selectedGender.value == Gender.MALE,
            onSelect = { selectedGender.value = Gender.MALE }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Female Option
        GenderOption(
            imagePainter = femaleImagePainter,
            label = "Female",
            isSelected = selectedGender.value == Gender.FEMALE,
            onSelect = { selectedGender.value = Gender.FEMALE }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick =  {
                       viewModel.onEvent(RegisterEvent.onGenderChosen(selectedGender.value))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Next", fontSize = 18.sp)
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    SnackbarHost(hostState = snackBarHostState)
}

@Composable
fun GenderOption(
    imagePainter: Painter,
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(160.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onSelect() })
            }
    ) {
        Image(
            painter = imagePainter,
            contentDescription = label,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer {
                    // Scale up the size for the selected option
                    scaleX = if (isSelected) 1.2f else 1f
                    scaleY = if (isSelected) 1.2f else 1f
                }
                .then(
                    if (isSelected) Modifier.border(
                        2.dp, MaterialTheme.colorScheme.primary,
                        CircleShape
                    ) else Modifier
                )
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    CircleShape
                )
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
    }
}



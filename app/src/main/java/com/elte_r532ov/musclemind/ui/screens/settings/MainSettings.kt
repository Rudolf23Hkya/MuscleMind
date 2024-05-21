package com.elte_r532ov.musclemind.ui.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.ui.util.BottomNavBar
import com.elte_r532ov.musclemind.ui.util.handleUiEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainSettingsScreen(
    onNavigate: NavHostController,
    viewModel: MainSettingsViewModel = hiltViewModel()
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
                Text(
                    "Settings",
                    fontSize = 32.sp,
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {viewModel.onEvent(MainSettingsEvent.onLogOutClicked)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "Log Out",
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

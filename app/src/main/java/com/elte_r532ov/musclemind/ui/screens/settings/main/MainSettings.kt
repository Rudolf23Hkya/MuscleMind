package com.elte_r532ov.musclemind.ui.screens.settings.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.elte_r532ov.musclemind.ui.BottomNavBar
import com.elte_r532ov.musclemind.ui.screens.login.LoginEvent
import com.elte_r532ov.musclemind.util.UiEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainSettingsScreen(
    onNavigate: NavHostController,
    viewModel: MainSettingsViewModel = hiltViewModel()
) {
    //SnackBar
    var snackBarMessage by remember { mutableStateOf<String?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate.navigate(event.route)
                is UiEvent.ShowSnackbar -> snackBarMessage = event.message
                is UiEvent.ErrorOccured -> snackBarMessage = event.errMsg
                else -> Unit
            }
        }
    }

    Scaffold(
        bottomBar = {
            onNavigate.currentDestination?.route?.let {
                BottomNavBar(it, onNavigate)
            }
        },
        topBar={
            Text(
                "Settings",
                fontSize = 32.sp,
                fontFamily = myFontFamily,
                fontWeight = FontWeight.Bold,
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFCDD2)), // A pink background
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
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {viewModel.onEvent(MainSettingsEvent.onChangeAccountDataClicked)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "Change Account Data",
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {viewModel.onEvent(MainSettingsEvent.onChangePasswordClicked)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "Change Password",
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SettingItem(text: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(50), // Highly rounded corners
        modifier = Modifier
            .fillMaxWidth(0.8f) // Fill 80% of the width
            .height(50.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Shadow effect
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.ui.BottomNavBar
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
            Text(text = "Settings")
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
            SettingItem("Log Out") {
                viewModel.onEvent(MainSettingsEvent.onLogOutClicked) }
            Spacer(modifier = Modifier.height(20.dp))
            SettingItem("Change Profile Data") {
                viewModel.onEvent(MainSettingsEvent.onChangeProfileDataClicked) }
            Spacer(modifier = Modifier.height(20.dp))
            SettingItem("Account Settings") {
                viewModel.onEvent(MainSettingsEvent.onAccountSettingsClicked) }
            Spacer(modifier = Modifier.height(20.dp))
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

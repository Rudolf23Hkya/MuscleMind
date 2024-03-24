package com.elte_r532ov.musclemind.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun RegisterFizData(
    onNavigate: NavHostController,
    viewModel: SharedRegisterViewModel = hiltViewModel()
) {

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
                "Tell us about yourself",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "We need to know you better to provide you with the best training experience.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            UserInfoTextField(label = "Weight", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(16.dp))
            UserInfoTextField(label = "Age", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(16.dp))
            UserInfoTextField(label = "Height", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /* TODO: Handle the next button click here */ },
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
fun UserInfoTextField(label: String, keyboardOptions: KeyboardOptions) {
    OutlinedTextField(
        value = "",
        onValueChange = { /* TODO: Handle the text change here */ },
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

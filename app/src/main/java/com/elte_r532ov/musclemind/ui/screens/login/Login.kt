package com.elte_r532ov.musclemind.ui.screens.login
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.util.UiEvent

@Composable
fun LoginScreen(
    //onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1D5E7)), // Use a gradient instead for the background
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 54.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Login",
                fontSize = 32.sp,
                fontFamily = myFontFamily,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emailState.value,
            onValueChange = {emailState.value = it},
            label = { Text("Email", fontFamily = myFontFamily, fontWeight = FontWeight.Medium) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {passwordState.value = it},
            label = { Text("Password",fontFamily = myFontFamily, fontWeight = FontWeight.Medium) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {}
                )
                Text("Remember me",fontFamily = myFontFamily, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 1.dp))
            }
            TextButton(
                onClick = { /* Handle forgot password */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Forgot password?",fontFamily = myFontFamily, fontWeight = FontWeight.SemiBold)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onEvent(LoginEvent.onLoginClicked(emailState.value,passwordState.value))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("LOGIN",
                fontFamily = myFontFamily,
                fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don’t have an account?",fontFamily = myFontFamily, fontWeight = FontWeight.Medium)
            TextButton(
                onClick = { /* TODO: Handle Sign Up */ }
            ) {
                Text("Sign Up",fontFamily = myFontFamily, fontWeight = FontWeight.SemiBold)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Or",
                fontFamily = myFontFamily,
                fontWeight = FontWeight.Bold,)
        }


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* TODO: Handle Google Sign-In */ },
        ) {
            Text("Continue with Google",
                fontFamily = myFontFamily,
                fontWeight = FontWeight.Bold)
        }
    }
}
package com.elte_r532ov.musclemind.ui.screens.login
import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.myFontFamily
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.elte_r532ov.musclemind.ui.util.handleUiEvent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import com.elte_r532ov.musclemind.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

@Composable
fun LoginScreen(
    onNavigate: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    //Handle UiEvent:
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 54.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Login",
                    fontSize = 32.sp,
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = {
                    Text(
                        "Email",
                        fontFamily = myFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = {
                    Text(
                        "Password",
                        fontFamily = myFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onEvent(
                        LoginEvent.onLoginClicked(
                            emailState.value,
                            passwordState.value
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "LOGIN",
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Don’t have an account?",
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(
                    onClick = { viewModel.onEvent(LoginEvent.onSignUpClicked) }
                ) {
                    Text("Sign Up", fontFamily = myFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color =  MaterialTheme.colorScheme.primaryContainer
                        )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Or",
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }


            Spacer(modifier = Modifier.height(20.dp))

            GoogleSignInButton(viewModel)

            Spacer(modifier = Modifier.height(10.dp))
            SnackbarHost(hostState = snackBarHostState)
        }
    }

@SuppressLint("ResourceType")
@Composable
fun GoogleSignInButton(viewModel: LoginViewModel) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val clientId = "1078166345846-pkuc1hn3m5tp4q2lsbh5a1able65q796.apps.googleusercontent.com"

    // Configure Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(clientId)
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task, viewModel)
    }

    Button(
        onClick = { signIn(googleSignInClient, launcher) },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            "Continue with Google",
            fontFamily = myFontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private fun signIn(
    googleSignInClient: GoogleSignInClient,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}

private fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>,
    viewModel: LoginViewModel
) {
    try {
        val account = completedTask.getResult(ApiException::class.java)
        val idToken = account?.idToken
        if (idToken != null) {
            viewModel.sendTokenToServer(idToken)
        }
    } catch (e: ApiException) {
        // Handle the exception
        viewModel.onError(e)
    }
}
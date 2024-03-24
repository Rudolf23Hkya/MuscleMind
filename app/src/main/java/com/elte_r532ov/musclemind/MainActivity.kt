package com.elte_r532ov.musclemind
import com.elte_r532ov.musclemind.ui.screens.login.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elte_r532ov.musclemind.ui.screens.register.ExperienceSelectionScreen
import com.elte_r532ov.musclemind.ui.screens.register.RegisterData
import com.elte_r532ov.musclemind.ui.screens.register.RegisterFizData
import com.elte_r532ov.musclemind.ui.screens.register.RegisterGender
import com.elte_r532ov.musclemind.util.Routes
import dagger.hilt.android.AndroidEntryPoint


val myFontFamily = FontFamily(
    Font(R.font.inter_black,FontWeight.Black),
    Font(R.font.inter_medium,FontWeight.Medium),
    Font(R.font.inter_light,FontWeight.Light),
    Font(R.font.inter_bold,FontWeight.Bold),
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Routes.LOGIN) {
                composable(Routes.LOGIN) {
                    LoginScreen(navController)
                }
                composable(Routes.REGISTER_ACCOUNT_DATA){
                    RegisterData(navController)
                }
                composable(Routes.REGISTER_GENDER){
                    RegisterGender(navController)
                }
                composable(Routes.REGISTER_FIZ_DATA){
                    RegisterFizData(navController)
                }
                composable(Routes.REGISTER_EXP){
                    ExperienceSelectionScreen(navController)
                }
            }
        }
    }
}




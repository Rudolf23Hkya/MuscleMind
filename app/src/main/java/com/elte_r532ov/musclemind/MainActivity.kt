package com.elte_r532ov.musclemind
import StatsMainScreen
import com.elte_r532ov.musclemind.ui.screens.login.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elte_r532ov.musclemind.ui.screens.register.ExperienceSelectionScreen
import com.elte_r532ov.musclemind.ui.screens.register.RegisterData
import com.elte_r532ov.musclemind.ui.screens.register.RegisterFizData
import com.elte_r532ov.musclemind.ui.screens.register.RegisterGender
import com.elte_r532ov.musclemind.ui.screens.workouts.active.ActiveWorkouts
import com.elte_r532ov.musclemind.util.Routes
import dagger.hilt.android.AndroidEntryPoint
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.ui.screens.calories.CalorieCounterScreen
import com.elte_r532ov.musclemind.ui.screens.register.DiseaseSelectionScreen
import com.elte_r532ov.musclemind.ui.screens.settings.ModifyAccountData
import com.elte_r532ov.musclemind.ui.screens.settings.main.MainSettingsScreen
import com.elte_r532ov.musclemind.ui.screens.workouts.workoutInDetail.WorkoutInDetail
import com.elte_r532ov.musclemind.ui.screens.workouts.workoutInProgress.WorkoutInProgress
import com.elte_r532ov.musclemind.ui.theme.MuscleMindTheme
import javax.inject.Inject

val myFontFamily = FontFamily(
    Font(R.font.inter_black,FontWeight.Black),
    Font(R.font.inter_medium,FontWeight.Medium),
    Font(R.font.inter_light,FontWeight.Light),
    Font(R.font.inter_bold,FontWeight.Bold),
)

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    @Inject
    lateinit var sessionManagement: SessionManagement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuscleMindTheme {
                MyApp(sessionManagement)
            }
        }
    }

}

@Composable
fun MyApp(sessionManagement: SessionManagement) {

    // Determine the start destination based on session token presence
    val appStartDestination = if (sessionManagement.isLoggedIn()) {
        //Routes.LOGIN
        Routes.WORKOUTS_ACTIVE
    } else {
        Routes.LOGIN
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = appStartDestination)
    {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        navigation(
            startDestination = Routes.REGISTER_GENDER,
            route = Routes.REGISTRATION_ROUTE
        ) {
            composable(Routes.REGISTER_GENDER) {
                RegisterGender( navController)
            }
            composable(Routes.REGISTER_FIZ_DATA) {
                RegisterFizData(navController)
            }
            composable(Routes.REGISTER_EXP) {
                ExperienceSelectionScreen(navController)
            }
            composable(Routes.REGISTER_DISEASE) {
                DiseaseSelectionScreen(navController)
            }
            composable(Routes.REGISTER_ACCOUNT_DATA) {
                RegisterData(navController)
            }
        }
        composable(Routes.WORKOUTS_ACTIVE) {
            ActiveWorkouts(navController)
        }

        composable(Routes.SETTINGS_MAIN){
            MainSettingsScreen(navController)
        }
        composable(Routes.SETTINGS_CHANGE_ACCOUNT_DATA){
            ModifyAccountData(navController)
        }
        composable(Routes.CALORIES_OVERVIEW){
            CalorieCounterScreen(navController)
        }
        composable(Routes.STATS_OVERVIEW){
            val progressValues = floatArrayOf(0.3f, 0.5f, 0.2f, 0.8f, 0.4f, 0.0f, 0.0f)
            StatsMainScreen(navController,progressValues)
        }
        navigation(
            startDestination = Routes.WORKOUTS_IN_DETAIL,
            route = Routes.WORKOUTS_ROUTE
        ){
            composable(route= Routes.WORKOUTS_IN_DETAIL,
                arguments = listOf(navArgument("workoutId"){
                    type = NavType.LongType
                })){
                //Passing parameter with Navigation
                val workoutID = it.arguments?.getLong("workoutId") ?:""
                WorkoutInDetail(workoutID.toString().toLong(),navController)
            }
            composable(Routes.WORKOUTS_START){
                WorkoutInProgress(navController)
            }
        }
    }
}




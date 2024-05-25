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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.elte_r532ov.musclemind.ui.screens.register.ExperienceSelectionScreen
import com.elte_r532ov.musclemind.ui.screens.register.RegisterData
import com.elte_r532ov.musclemind.ui.screens.register.RegisterFizData
import com.elte_r532ov.musclemind.ui.screens.register.RegisterGender
import com.elte_r532ov.musclemind.ui.screens.workouts.active.ActiveWorkouts
import com.elte_r532ov.musclemind.ui.util.Routes
import dagger.hilt.android.AndroidEntryPoint
import com.elte_r532ov.musclemind.data.local.SessionManagement
import com.elte_r532ov.musclemind.ui.screens.calories.CalorieCounterScreen
import com.elte_r532ov.musclemind.ui.screens.register.DiseaseSelectionScreen
import com.elte_r532ov.musclemind.ui.screens.settings.MainSettingsScreen
import com.elte_r532ov.musclemind.ui.screens.workouts.active.WorkoutBeforeStart
import com.elte_r532ov.musclemind.ui.screens.workouts.active.WorkoutInProgress
import com.elte_r532ov.musclemind.ui.screens.workouts.active.WorkoutRating
import com.elte_r532ov.musclemind.ui.screens.workouts.create.CreateWorkoutData
import com.elte_r532ov.musclemind.ui.screens.workouts.create.CreateWorkoutDetail
import com.elte_r532ov.musclemind.ui.screens.workouts.create.CreateWorkoutSelect
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
        Routes.WORKOUT_ACTIVE_ROUTE
    } else {
        Routes.LOGIN
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = appStartDestination)
    {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        // Register Route
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
        // Create Workout Route
        navigation(
            startDestination = Routes.CREATE_WORKOUT_DATA,
            route = Routes.CREATE_WORKOUT_ROUTE
        ) {
            composable(Routes.CREATE_WORKOUT_DATA) {
                CreateWorkoutData(navController)
            }
            composable(Routes.CREATE_WORKOUT_SELECT) {
                CreateWorkoutSelect(navController)
            }
            composable(Routes.CREATE_WORKOUT_SELECT_DETAIL) {
                CreateWorkoutDetail(navController)
            }
        }
        // Active Workout Route
        navigation(
            startDestination = Routes.WORKOUT_ACTIVE,
            route = Routes.WORKOUT_ACTIVE_ROUTE
        ) {
            composable(Routes.WORKOUT_ACTIVE) {
                ActiveWorkouts(navController)
            }
            composable(Routes.WORKOUT_BEFORE_START) {
                WorkoutBeforeStart(navController)
            }
            composable(Routes.WORKOUT_IN_PROGRESS) {
                WorkoutInProgress(navController)
            }
            composable(Routes.WORKOUT_RATING) {
                WorkoutRating(navController)
            }
        }

        composable(Routes.SETTINGS_MAIN){
            MainSettingsScreen(navController)
        }
        composable(Routes.CALORIES_OVERVIEW){
            CalorieCounterScreen(navController)
        }
        composable(Routes.STATS_OVERVIEW){
            StatsMainScreen(navController)
        }
    }
}




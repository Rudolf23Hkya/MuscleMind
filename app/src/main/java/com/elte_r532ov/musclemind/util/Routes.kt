package com.elte_r532ov.musclemind.util

object Routes {
    const val LOGIN = "login"

    //Registration
    const val REGISTRATION_ROUTE = "registration_route"//Nested root

    const val REGISTER_EXP = "registerExperience"
    const val REGISTER_FIZ_DATA = "registerFizData"
    const val REGISTER_GENDER = "registerGender"
    const val REGISTER_DISEASE = "registerDisease"
    const val REGISTER_ACCOUNT_DATA = "registerACCData"

    //Workout

    const val WORKOUTS_ACTIVE = "workouts_active"

    const val WORKOUTS_IN_DETAIL = "workouts_in_detail/{workoutId}"
    const val WORKOUTS_START = "workouts_start"
    const val WORKOUTS_IN_PROGRESS = "workouts_in_progress"
    const val WORKOUTS_RATING = "workouts_rating"

    //Create Workout
    const val CREATE_WORKOUT_ROUTE = "create_workout_route"//Nested root

    const val CREATE_WORKOUT_DATA = "create_workout_data"
    const val CREATE_WORKOUT_SELECT = "create_workout_select"
    const val CREATE_WORKOUT_SELECT_DETAIL = "create_workout_select_detail"

    //Other
    const val STATS_OVERVIEW = "stats_overview"
    const val CALORIES_OVERVIEW = "calories_overview"
    const val SETTINGS_MAIN = "settings_main"
}
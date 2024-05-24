package com.elte_r532ov.musclemind.ui.util

object Routes {
    const val LOGIN = "login"

    //Registration
    const val REGISTRATION_ROUTE = "registration"//Nested root

    const val REGISTER_EXP = "registerExperience"
    const val REGISTER_FIZ_DATA = "registerFizData"
    const val REGISTER_GENDER = "registerGender"
    const val REGISTER_DISEASE = "registerDisease"
    const val REGISTER_ACCOUNT_DATA = "registerACCData"

    //Workout
    const val WORKOUT_ACTIVE_ROUTE ="workout_active_route"

    const val WORKOUT_ACTIVE = "workout_active"
    const val WORKOUT_BEFORE_START = "workout_before_start"
    const val WORKOUT_IN_PROGRESS = "workout_in_progress"
    const val WORKOUT_RATING = "workout_rating"

    //Create Workout
    const val CREATE_WORKOUT_ROUTE = "create_workout_route"//Nested root

    const val CREATE_WORKOUT_DATA = "create_workout_data"
    const val CREATE_WORKOUT_SELECT = "create_workout_select"
    const val CREATE_WORKOUT_SELECT_DETAIL = "create_workout_detail"

    //Other
    const val STATS_OVERVIEW = "stats_overview"
    const val CALORIES_OVERVIEW = "calories_overview"
    const val SETTINGS_MAIN = "settings_main"
}
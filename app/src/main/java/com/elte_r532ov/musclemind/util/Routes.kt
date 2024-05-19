package com.elte_r532ov.musclemind.util

object Routes {
    const val LOGIN = "login"

    //Registration
    const val REGISTRATION_ROUTE = "registration"//Nested root

    const val REGISTER_EXP = "registerExperience"
    const val REGISTER_FIZ_DATA = "registerFizData"
    const val REGISTER_GENDER = "registerGender"
    const val REGISTER_DISEASE = "registerDisease"
    const val REGISTER_ACCOUNT_DATA = "registerACCData"

    const val STATS_OVERVIEW = "stats_overview"
    const val CALORIES_OVERVIEW = "calories_overview"
    //Workouts
    const val WORKOUTS_ACTIVE = "workouts_active"
    const val WORKOUTS_IN_DETAIL = "workouts_in_detail/{workoutId}"
    const val WORKOUTS_START = "workouts_start"

    const val WORKOUTS_ROUTE = "working_out"


    //Settings
    const val SETTINGS_MAIN = "settings_main"
    const val SETTINGS_CHANGE_ACCOUNT_DATA = "settings_change_account_data"
    const val SETTINGS_PASSWORD = "settings_password"
}
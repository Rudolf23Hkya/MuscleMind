package com.elte_r532ov.musclemind.util

import com.elte_r532ov.musclemind.R

object BottomNavMenu {
    val STATS = BottomNavItem("Stats", R.drawable.line_up,
        Routes.STATS_OVERVIEW,"stats")
    val CALORIES = BottomNavItem("Calories", R.drawable.calories,
        Routes.CALORIES_OVERVIEW,"calories")
    val SETTINGS = BottomNavItem("Settings", R.drawable.setting_line,
        Routes.SETTINGS_MAIN,"settings")
    val WORKOUTS = BottomNavItem("Workouts", R.drawable.lightning,
        Routes.WORKOUTS_ACTIVE,"workouts")
}
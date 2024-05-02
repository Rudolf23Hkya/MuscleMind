package com.elte_r532ov.musclemind.ui.screens.workouts.sharedElements

import com.elte_r532ov.musclemind.R
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel

fun getPictureIdByExperienceLevel(level: ExperienceLevel): Int {
    return when(level) {
        ExperienceLevel.NEW -> R.drawable.bar1
        ExperienceLevel.INTERMEDIATE -> R.drawable.bar2
        ExperienceLevel.EXPERIENCED -> R.drawable.bar3
        ExperienceLevel.PROFESSIONAL -> R.drawable.bar4
    }
}
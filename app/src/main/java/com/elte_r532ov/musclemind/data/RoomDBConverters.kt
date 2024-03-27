package com.elte_r532ov.musclemind.data

import androidx.room.TypeConverter
import com.elte_r532ov.musclemind.data.userData.ExperienceLevel
import com.elte_r532ov.musclemind.data.userData.Gender
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Category
import com.elte_r532ov.musclemind.data.workoutsAndExercises.MuscleGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomDBConverters {
    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }
    @TypeConverter
    fun toCategory(category: String): Category {
        return Category.valueOf(category)
    }
    @TypeConverter
    fun fromExperienceLevel(exp: ExperienceLevel): String {
        return exp.name
    }
    @TypeConverter
    fun toExperienceLevel(exp: String): ExperienceLevel {
        return ExperienceLevel.valueOf(exp)
    }
    @TypeConverter
    fun fromSex(gender: Gender): String {
        return gender.name
    }
    @TypeConverter
    fun toSex(gender: String): Gender {
        return Gender.valueOf(gender)
    }
    @TypeConverter
    fun fromMuscleGroup(muscleGroup: MuscleGroup): String {
        return muscleGroup.name
    }
    @TypeConverter
    fun toMuscleGroup(muscleGroup: String): MuscleGroup {
        return MuscleGroup.valueOf(muscleGroup)
    }
    //Converts List to String and String to List
    @TypeConverter
    fun fromListOfLongToJson(value: List<Long>?): String = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToListOfLong(value: String): List<Long>? {
        val listType = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson(value, listType)
    }

}
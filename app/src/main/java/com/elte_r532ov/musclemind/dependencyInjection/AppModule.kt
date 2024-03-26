package com.elte_r532ov.musclemind.dependencyInjection

import android.app.Application
import androidx.room.Room
import com.elte_r532ov.musclemind.data.userData.MuscleMindDB
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepoImpl
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepository
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutDao
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutExcRepoImpl
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutExcRepository
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutExerciseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Defines the dependency s lifetime
//This is SINGLETON
@Module
@InstallIn(SingletonComponent :: class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDataDatabase(app : Application) : MuscleMindDB {
        return Room.databaseBuilder(
            app.applicationContext,
            MuscleMindDB::class.java,
            "muscle_mind_db"
        ).fallbackToDestructiveMigration().build()
    }

    //Context not needed, only the database instance
    @Provides
    @Singleton
    fun provideMuscleMindRepository(db : MuscleMindDB) : MuscleMindRepository {
        return MuscleMindRepoImpl(db.dao)
    }
    // Provide the new WorkoutDatabase
    @Provides
    @Singleton
    fun provideWorkoutDatabase(app: Application): WorkoutExerciseDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            WorkoutExerciseDatabase::class.java,
            "workout_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideWorkoutExcRepository(db : WorkoutExerciseDatabase) : WorkoutExcRepository {
        return WorkoutExcRepoImpl(db.workoutDao())
    }
}
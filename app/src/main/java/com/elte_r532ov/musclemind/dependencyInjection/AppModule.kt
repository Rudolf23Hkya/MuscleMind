package com.elte_r532ov.musclemind.dependencyInjection

import android.app.Application
import androidx.room.Room
import com.elte_r532ov.musclemind.data.MuscleMindDB
import com.elte_r532ov.musclemind.data.MuscleMindRepoImpl
import com.elte_r532ov.musclemind.data.MuscleMindRepository
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
    fun provideUserDataDatabase(app : Application) : MuscleMindDB{
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
}
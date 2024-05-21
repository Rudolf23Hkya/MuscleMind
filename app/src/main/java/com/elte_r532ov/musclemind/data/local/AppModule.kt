package com.elte_r532ov.musclemind.data.local

import android.app.Application
import com.elte_r532ov.musclemind.data.api.ApiDao
import com.elte_r532ov.musclemind.data.api.MuscleMindRepoImplApi
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
object AppModule{

    @Singleton
    @Provides
    fun provideMuscleMindRepository(apiDao: ApiDao,app: Application): MuscleMindRepository {
        return MuscleMindRepoImplApi(apiDao, SessionManagement(app.applicationContext),app.applicationContext)
    }
}
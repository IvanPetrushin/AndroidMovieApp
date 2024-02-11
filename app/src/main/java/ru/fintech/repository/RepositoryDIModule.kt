package ru.fintech.repository

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.fintech.database.AppDatabase
import ru.fintech.entities.dao.FilmInfoDAO
import ru.fintech.services.ApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryDIModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        apiService: ApiService, filmInfoDao: FilmInfoDAO
    ): AppRepository = AppRepository(apiService = apiService, filmInfoDao = filmInfoDao)

    @Provides
    @Singleton
    fun provideApiService(): ApiService = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech")
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "appDatabase"
        ).build()

    @Provides
    @Singleton
    fun provideBinDao(appDatabase: AppDatabase): FilmInfoDAO = appDatabase.filmInfoDao()
}
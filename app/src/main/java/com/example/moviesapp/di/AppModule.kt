package com.example.moviesapp.di

import android.app.Application
import androidx.room.Room
import com.example.moviesapp.datasource.data.local.dao.UserDao
import com.example.moviesapp.datasource.data.local.database.MovieDatabase
import com.example.moviesapp.datasource.data.remote.RetrofitClient
import com.example.moviesapp.datasource.data.remote.services.MovieService
import com.example.moviesapp.datasource.data.remote.services.UserService
import com.example.moviesapp.ui.core.presentation.paging.UserPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideUserDao(database: MovieDatabase): UserDao {
        return database.userDao
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "moviedb.db"
        ).fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    @Singleton
    fun providesMovieApi() : MovieService {
        return RetrofitClient.getClient(MovieService.MOVIE_BASE_URL).create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserApi() : UserService {
        return RetrofitClient.getClient(UserService.USER_BASE_URL).create(UserService::class.java)
    }

}
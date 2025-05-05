package com.example.moviesapp.di

import com.example.moviesapp.datasource.data.repository.MovieRepositoryImpl
import com.example.moviesapp.datasource.data.repository.UserRepositoryImpl
import com.example.moviesapp.datasource.domain.repository.MovieListRepository
import com.example.moviesapp.datasource.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieListRepository



    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository


}
package com.example.moviesapp.datasource.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapp.datasource.data.local.dao.MovieDao
import com.example.moviesapp.datasource.data.local.dao.UserDao
import com.example.moviesapp.datasource.data.local.entity.MovieEntity
import com.example.moviesapp.datasource.data.local.entity.UserEntity


@Database(
    entities = [MovieEntity::class,UserEntity::class],
    version = 1
)
abstract class MovieDatabase :RoomDatabase(){
    abstract val userDao:UserDao
}
package com.example.moviesapp.ui.core.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moviesapp.datasource.data.local.dao.UserDao
import com.example.moviesapp.datasource.data.remote.request.AddUserRequest
import com.example.moviesapp.datasource.data.remote.services.UserService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltWorker
class SyncUserWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userDao: UserDao,
    private val apiService: UserService
) : CoroutineWorker(context, params) {

    private val TAG = "SyncWorker"
    override suspend fun doWork(): Result {
        return try {
            Log.e(TAG,"Starting  Background Works...")
            val allUsers = userDao.getAllUsers()
            if (allUsers.isNotEmpty()) {
                Log.e(TAG,"User is Not Empty going to Upload the Users")
                allUsers.forEach { user ->
                    try {
                        apiService.addNewUser(
                            request = AddUserRequest(user.name, user.job),
                            apiKey = UserService.USER_API_KEY
                        )
                        userDao.deleteUser(user)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                Log.e(TAG,"All Users are Uploaded..")
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}

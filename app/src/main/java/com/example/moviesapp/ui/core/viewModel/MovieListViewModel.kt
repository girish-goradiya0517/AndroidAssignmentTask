package com.example.moviesapp.ui.core.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moviesapp.util.Category
import com.example.moviesapp.util.NetworkUtils
import com.example.moviesapp.util.Resource
import com.example.moviesapp.datasource.data.local.entity.UserEntity
import com.example.moviesapp.datasource.domain.repository.MovieListRepository
import com.example.moviesapp.datasource.domain.repository.UserRepository
import com.example.moviesapp.ui.core.state.MovieListState
import com.example.moviesapp.ui.core.state.UserState
import com.example.moviesapp.ui.core.workers.SyncUserWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    private var _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    val popularMoviesPager = movieListRepository
        .getMoviePager()
        .cachedIn(viewModelScope)


    val userPager = userRepository.getUserPager().cachedIn(viewModelScope)

    fun getPopularMovieList() {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                Category.TRENDING,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList
                                            + popularList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }


    fun addUser(userName: String, jobTitle: String,context: Context) {
        _userState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            if (NetworkUtils.isNetworkAvailable(context)) {
                userRepository.addNewUser(userName, jobTitle).collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _userState.update {
                                it.copy(isLoading = false, userMessage = "Error ${result.message}")
                            }
                        }

                        is Resource.Loading -> {
                            _userState.update { it.copy(isLoading = result.isLoading) }
                        }

                        is Resource.Success -> {
                            _userState.update {
                                it.copy(isLoading = false, userMessage = "New User Added Successfully")
                            }
                        }
                    }
                }
            } else {
                val user = UserEntity(name = userName, job = jobTitle)
                userRepository.insertUserLocally(user)
                _userState.update {
                    it.copy(isLoading = false, userMessage = "Saved locally, will sync later")
                }
                startBackgroundUploadProcess(context)
            }
        }
    }

    fun startBackgroundUploadProcess(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<SyncUserWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "SyncUserWork",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
    fun clearMessages()
    {
        _userState.update { it.copy(userMessage = null, isLoading = false) }
    }
}
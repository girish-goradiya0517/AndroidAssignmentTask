package com.example.moviesapp.ui.core.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.util.Resource
import com.example.moviesapp.datasource.domain.repository.MovieListRepository
import com.example.moviesapp.ui.core.state.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Girish Coders
 */

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId = savedStateHandle.get<Int>("movieId")
    private var _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState.asStateFlow()

    init {
        getMovie(movieId?:-1)
    }

    private fun getMovie(id:Int){
        viewModelScope.launch {
            _detailState.update {
                it.copy(isLoading = true)
            }

            movieRepository.getMovieById(id).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _detailState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _detailState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _detailState.update{
                                it.copy(isLoading = false, movie = movie)
                            }
                        }
                    }
                }
            }
        }
    }

}
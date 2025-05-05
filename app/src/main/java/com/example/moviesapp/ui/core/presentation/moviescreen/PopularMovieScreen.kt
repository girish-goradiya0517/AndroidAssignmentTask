package com.example.moviesapp.ui.core.presentation.moviescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.ui.core.components.LoadingBar
import com.example.moviesapp.ui.core.components.Movieitem
import com.example.moviesapp.ui.core.viewModel.MovieListViewModel

@Composable
fun PopularMoviesScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val state = viewModel.movieListState.collectAsState()
    // Collect paginated movies
    val movies = viewModel.popularMoviesPager.collectAsLazyPagingItems()

    // Remember the grid scroll state
    val listState = rememberLazyGridState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LoadingBar(isVisible = state.value.isLoading)

        when (movies.loadState.refresh) {

            is LoadState.Loading -> {

            }

            is LoadState.Error -> {
                Text(
                    text = "Failed to load movies. Please try again.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is LoadState.NotLoading -> {

                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
                ) {
                    items(movies.itemCount) { index ->
                        val movie = movies[index]
                        movie?.let {
                            Movieitem(
                                movie = it,
                                navHostController = navController
                            )
                        }
                    }


                    if (movies.loadState.append is LoadState.Error) {
                        item {
                            Text(
                                text = "Error loading more movies.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

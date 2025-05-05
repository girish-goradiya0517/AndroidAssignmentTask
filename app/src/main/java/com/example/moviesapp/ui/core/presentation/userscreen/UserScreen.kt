package com.example.moviesapp.ui.core.presentation.userscreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.datasource.domain.model.User
import com.example.moviesapp.ui.core.components.AddUserDialog
import com.example.moviesapp.ui.core.components.ErrorMessage
import com.example.moviesapp.ui.core.components.LoadingBar
import com.example.moviesapp.ui.core.components.UserAvatar
import com.example.moviesapp.ui.core.viewModel.MovieListViewModel
import com.example.moviesapp.util.UserPagingException
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(onClick: (User) -> Unit) {
    val viewModel = hiltViewModel<MovieListViewModel>()

    val context = LocalContext.current
    val userPagingItems = viewModel.userPager.collectAsLazyPagingItems()

    val loadState = userPagingItems.loadState

    val isLoading = viewModel.movieListState.collectAsState().value.isLoading



    val userState by viewModel.userState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
       viewModel.startBackgroundUploadProcess(context)
    }


    LaunchedEffect(userState.userMessage) {
        userState.userMessage?.let {
            Toast.makeText(context, userState.userMessage, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add User",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        LoadingBar(isVisible = isLoading)
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(
                    userPagingItems.itemCount
                ) { index ->
                    val user = userPagingItems[index]
                    user?.let {
                        UserListItem(user = it, onClick = onClick)
                    }
                }
            }
            userPagingItems.apply {
                when {
                    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {

                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = (loadState.refresh as LoadState.Error).error
                        ErrorMessage(
                            message = when (error) {
                                is UserPagingException.NetworkException -> "Please check your internet connection"
                                is UserPagingException.HttpErrorException -> "Server error! Try again later"
                                is UserPagingException.UnknownException -> "Something went wrong"
                                else -> "Unexpected error occurred"
                            },
                            alignment = Alignment.Center
                        )
                    }

                    loadState.append is LoadState.Error -> {
                        val errorMessage = (loadState.append as LoadState.Error).error.localizedMessage ?: "Error occurred"
                        ErrorMessage(
                            message = errorMessage,
                            alignment = Alignment.BottomCenter
                        )
                    }
                }
            }

        }
    }

    if (showDialog) {
        AddUserDialog(
            onDismiss = { showDialog = false },
            onAddUser = { name,jobTitle ->
                coroutineScope.launch {
                    viewModel.addUser(name,jobTitle,context)
                    showDialog = false
                }
            }
        )
    }
}
@Composable
fun UserListItem(
    user: User,
    onClick: (User) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(user) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                user = user
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user.first_name} ${user.last_name}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
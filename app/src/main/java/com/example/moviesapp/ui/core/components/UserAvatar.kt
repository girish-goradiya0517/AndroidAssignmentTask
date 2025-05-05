package com.example.moviesapp.ui.core.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.moviesapp.datasource.domain.model.User


@Composable
fun UserAvatar(user: User) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    val isLoading = imageState is AsyncImagePainter.State.Loading
    val isError = imageState is AsyncImagePainter.State.Error

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
    ) {
        if (isLoading) {
            ShimmerEffect()
        } else if (isError) {
            // Handle error state
        } else {
            AsyncImage(
                model = user.avatar,
                contentDescription = "${user.first_name} ${user.last_name}'s Avatar",
                modifier = Modifier.matchParentSize(),
                onState = { state -> imageState = state }
            )
        }
    }
}

@Composable
fun ShimmerEffect() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "Shimmer")
    val translateX by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "Shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset(translateX, 0f),
        end = androidx.compose.ui.geometry.Offset(translateX + 200f, 200f)
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(brush)
    )
}
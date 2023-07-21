package com.riza.example.detail.movie.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.riza.example.commonui.theme.MyTheme
import com.riza.example.detail.movie.MovieDetailViewModel
import com.riza.example.detail.movie.state.MovieDetailItemModel
import com.riza.example.detail.movie.state.MovieDetailItemModel.Detail.Success.Genre

/**
 * Created by ahmadriza on 21/07/23.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    state: MovieDetailViewModel.State,
    onBackPress: () -> Unit,
    sendIntent: (MovieDetailViewModel.Intent) -> Unit
) {

    val lazyListState = rememberLazyListState()
    val isLoadMoreItemVisible by remember(state.displayItems) {
        derivedStateOf {
            val loadMoreItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull {
                it.key == "load_more_review"
            }
            loadMoreItem != null
        }
    }

    MyTheme {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onBackPress) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(
                    items = state.displayItems,
                    key = { _: Int, item: MovieDetailItemModel ->
                        when (item) {
                            MovieDetailItemModel.Detail.Loading -> "detail_load"
                            is MovieDetailItemModel.Detail.Success -> "detail"
                            MovieDetailItemModel.ErrorLoadMoreReview -> "error_load_review"
                            MovieDetailItemModel.LoadMoreReview -> "load_more_review"
                            MovieDetailItemModel.Overview.Error -> "overview_error"
                            MovieDetailItemModel.Overview.Loading -> "overview_load"
                            is MovieDetailItemModel.Overview.Success -> "overview"
                            is MovieDetailItemModel.Review -> "review_${item.id}"
                            MovieDetailItemModel.ReviewTitle.Error -> "review_tile_error"
                            MovieDetailItemModel.ReviewTitle.Loading -> "review_title_load"
                            is MovieDetailItemModel.ReviewTitle.Success -> "review_title"
                            MovieDetailItemModel.Trailers.Error -> "trailer_error"
                            MovieDetailItemModel.Trailers.Loading -> "trailer_loading"
                            is MovieDetailItemModel.Trailers.Success -> "trailer"
                            MovieDetailItemModel.Detail.Error -> "detail_error"
                        }
                    },
                    itemContent = { _: Int, item: MovieDetailItemModel ->
                        when (item) {
                            MovieDetailItemModel.Detail.Loading -> {

                            }

                            is MovieDetailItemModel.Detail.Success -> {
                                MovieDetailRow(
                                    detail = item,
                                    onGenreClick = {
                                        sendIntent(
                                            MovieDetailViewModel.Intent.OnGenreClick(it)
                                        )
                                    }
                                )
                            }

                            MovieDetailItemModel.ErrorLoadMoreReview -> {
                                ErrorLoadMoreReviewRow(
                                    onRetry = {
                                        sendIntent(MovieDetailViewModel.Intent.LoadMoreReviews)
                                    }
                                )
                            }
                            MovieDetailItemModel.LoadMoreReview -> {
                                LoadMoreReviewRow()
                            }
                            MovieDetailItemModel.Overview.Error -> {}
                            MovieDetailItemModel.Overview.Loading -> {}
                            is MovieDetailItemModel.Overview.Success -> {
                                MovieOverviewSection(model = item)
                            }

                            is MovieDetailItemModel.Review -> {
                                ReviewRow(item)
                            }

                            MovieDetailItemModel.ReviewTitle.Error -> {}
                            MovieDetailItemModel.ReviewTitle.Loading -> {}
                            is MovieDetailItemModel.ReviewTitle.Success -> {
                                ReviewHeaderRow(model = item)
                            }

                            MovieDetailItemModel.Trailers.Error -> {}
                            MovieDetailItemModel.Trailers.Loading -> {}
                            is MovieDetailItemModel.Trailers.Success -> {
                                MovieTrailersSection(
                                    trailer = item,
                                    onClickTrailer = {
                                        sendIntent(MovieDetailViewModel.Intent.OnTrailerClick(it))
                                    }
                                )
                            }

                            MovieDetailItemModel.Detail.Error -> {
                                
                            }
                        }
                    }
                )
            }
        }
    }
    LaunchedEffect(isLoadMoreItemVisible) {
        if (isLoadMoreItemVisible) {
            sendIntent(MovieDetailViewModel.Intent.LoadMoreReviews)
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailRow(
    detail: MovieDetailItemModel.Detail.Success,
    onGenreClick: (Genre) -> Unit
) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(0.6f)
                .clip(RoundedCornerShape(12.dp)),
            model = detail.thumbnail,
            contentDescription = null
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = detail.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                detail.genres.forEach {
                    SuggestionChip(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .height(26.dp),
                        onClick = { onGenreClick(it) },
                        label = {
                            Text(text = "# ${it.name}", style = MaterialTheme.typography.bodySmall)
                        }
                    )
                }
            }
            Text(
                text = "Release date",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Text(
                text = detail.releaseDate,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Production countries",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Text(
                text = detail.countries,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }

}

@Composable
fun MovieTrailersSection(
    trailer: MovieDetailItemModel.Trailers.Success,
    onClickTrailer: (videoId: String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Trailers and Clips",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                items = trailer.trailers,
                key = { _, item -> "trailer_${item.id}" }
            ) { _, item ->
                MovieTrailersRow(
                    item,
                    onClick = {
                        onClickTrailer(item.youtubeKey)
                    }
                )
            }

        }

    }
}

@Composable
fun MovieOverviewSection(model: MovieDetailItemModel.Overview.Success) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Overview",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = model.overview,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Composable
fun MovieTrailersRow(
    trailer: MovieDetailItemModel.Trailers.Success.Video,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.inverseSurface),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .alpha(0.8f),
                model = trailer.thumbnail,
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )

            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
                imageVector = Icons.Rounded.PlayArrow,
                tint = Color.White,
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = trailer.title,
            style = MaterialTheme.typography.labelMedium,
        )

    }
}

@Composable
fun ReviewHeaderRow(model: MovieDetailItemModel.ReviewTitle.Success) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Reviews (${model.total})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Average ${model.average}",
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.6f
                )
            )
        )
    }
}

@Composable
fun ReviewRow(review: MovieDetailItemModel.Review) {
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = review.username,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = review.time,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    )
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (review.review > 0) {
                Row {
                    repeat(review.review) { Star(enabled = true) }
                    repeat(10 - review.review) { Star(enabled = false) }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.description,
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }
}

@Composable
private fun Star(enabled: Boolean) {
    Icon(
        modifier = Modifier.size(16.dp),
        imageVector = Icons.Rounded.Star,
        tint = if (enabled) Color(0xFFFF9800) else MaterialTheme.colorScheme.outline,
        contentDescription = null
    )
}


@Preview
@Composable
private fun Preview() {

    val items = listOf(
        MovieDetailItemModel.Detail.Success(
            title = "Barbie",
            thumbnail = "",
            releaseDate = "10 October 2021",
            countries = "US",
            genres = listOf(
                Genre("Comedy", 1)
            )
        ),
        MovieDetailItemModel.Trailers.Success(
            trailers = listOf(
                MovieDetailItemModel.Trailers.Success.Video(
                    thumbnail = "", youtubeKey = "", title = "Trailer 1", id = ""
                )
            )
        ),
        MovieDetailItemModel.Overview.Success(
            LoremIpsum().values.first().take(300)
        ),
        MovieDetailItemModel.ReviewTitle.Success(
            average = "9.0/10", total = "1991001"
        ),
        MovieDetailItemModel.Review(
            id = "",
            username = "@AhmadRiza",
            time = "Yesterday",
            review = 9,
            description = LoremIpsum().values.first().take(50)
        )

    )

    MovieDetailScreen(
        state = MovieDetailViewModel.State(displayItems = items),
        onBackPress = {},
        sendIntent = {}
    )

}


@Preview(showBackground = true)
@Composable
private fun LoadMoreReviewRow(onTryAgain: () -> Unit = {}) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "More reviews incoming...",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }

}

@Preview(showBackground = true)
@Composable
fun ErrorLoadMoreReviewRow(onRetry: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "💁🏼‍ Failed to load more reviews.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.error
            )
        )
        TextButton(onClick = onRetry) {
            Text(text = "Try again")
        }
    }

}
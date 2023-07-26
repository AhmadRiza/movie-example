package com.riza.example.explore.genredetail.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riza.example.commonui.shared.ErrorStateContent
import com.riza.example.commonui.theme.MyTheme
import com.riza.example.explore.genredetail.GenreDetailViewModel
import com.riza.example.explore.genredetail.state.GenreDetailItemModel

/**
 * Created by ahmadriza on 20/07/23.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreDetailScreen(
    state: GenreDetailViewModel.State,
    onBackPress: () -> Unit,
    sendIntent: (GenreDetailViewModel.Intent) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyGridState = rememberLazyGridState()
    val isLoadMoreItemVisible by remember(state.items) {
        derivedStateOf {
            val loadMoreItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull {
                it.key == "load_more"
            }
            loadMoreItem != null
        }
    }

    MyTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Column {
                    MediumTopAppBar(
                        title = {
                            Text(
                                text = state.title,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackPress) {
                                Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                        scrollBehavior = scrollBehavior
                    )
                }
            }
        ) { innerPadding ->

            LazyVerticalGrid(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                state = lazyGridState,
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                itemsIndexed(
                    items = state.items,
                    key = { i, item ->
                        when (item) {
                            GenreDetailItemModel.LoadMore -> "load_more"
                            is GenreDetailItemModel.Movie -> "movie_${item.id}"
                            GenreDetailItemModel.ErrorMore -> "error_load_more"
                            GenreDetailItemModel.InitialError -> "initial_error"
                            GenreDetailItemModel.InitialShimmer -> "initial_shimmer_$i"
                        }
                    },
                    span = { _, item ->
                        when (item) {
                            GenreDetailItemModel.InitialShimmer,
                            is GenreDetailItemModel.Movie -> GridItemSpan(1)
                            GenreDetailItemModel.LoadMore,
                            GenreDetailItemModel.InitialError,
                            GenreDetailItemModel.ErrorMore -> GridItemSpan(maxLineSpan)
                        }
                    }
                ) { _, item ->
                    when (item) {
                        GenreDetailItemModel.LoadMore -> {
                            LoadMoreMovieRow()
                        }
                        is GenreDetailItemModel.Movie -> {
                            MovieCell(
                                movie = item,
                                onClick = {
                                    sendIntent(GenreDetailViewModel.Intent.OnMovieClick(it))
                                }
                            )
                        }

                        GenreDetailItemModel.ErrorMore -> {
                            ErrorLoadMoreMovieRow(
                                onRetry = { sendIntent(GenreDetailViewModel.Intent.RetryLoadMore) }
                            )
                        }

                        GenreDetailItemModel.InitialError -> {
                            ErrorStateContent(
                                onTryAgain = {
                                    sendIntent(GenreDetailViewModel.Intent.RetryInitialLoad)
                                }
                            )
                        }

                        GenreDetailItemModel.InitialShimmer -> {
                            MovieShimmer()
                        }
                    }
                }
            }
        }
        LaunchedEffect(isLoadMoreItemVisible) {
            if (isLoadMoreItemVisible) {
                sendIntent(GenreDetailViewModel.Intent.LoadMoreMovies)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadMoreMovieRow(onTryAgain: () -> Unit = {}) {

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
            text = "More movies incoming...",
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
fun ErrorLoadMoreMovieRow(onRetry: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "💁🏼‍ Failed to load more movie.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.error
            )
        )
        TextButton(onClick = onRetry) {
            Text(text = "Try again")
        }
    }
}

package com.riza.example.explore.genres.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riza.example.commonui.shared.ErrorStateContent
import com.riza.example.commonui.theme.MyTheme
import com.riza.example.explore.data.model.Genre
import com.riza.example.explore.genres.GenresViewModel
import com.riza.example.explore.genres.GenresViewModel.State
import com.riza.example.explore.genres.state.GenresDisplayState

/**
 * Created by ahmadriza on 18/07/23.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresScreen(state: State, sendIntent: (GenresViewModel.Intent) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    MyTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Column {
                    LargeTopAppBar(
                        title = {
                            Text(
                                text = "Select Movie Genre",
                                fontWeight = FontWeight.SemiBold
                            )
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
            when (val display = state.displayState) {
                GenresDisplayState.Loading -> {
                    GenresLoadingContent(modifier = Modifier.padding(innerPadding))
                }
                is GenresDisplayState.SuccessLoadGenres -> {
                    GenresSuccessContent(
                        modifier = Modifier.padding(innerPadding),
                        genres = { display.genres },
                        onGenreClicked = {
                            sendIntent(GenresViewModel.Intent.OnGenreClick(it))
                        }
                    )
                }
                is GenresDisplayState.ErrorLoadGenres -> {
                    ErrorStateContent(
                        onTryAgain = {
                            sendIntent(GenresViewModel.Intent.OnTryAgain)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GenresLoadingContent(modifier: Modifier) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(12) {
            GenreShimmer()
        }
    }
}

@Composable
private fun GenresSuccessContent(
    modifier: Modifier = Modifier,
    genres: () -> List<Genre>,
    onGenreClicked: (genre: Genre) -> Unit
) {
    val items = genres()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            count = items.size,
            key = { items[it].name },
            itemContent = { idx: Int ->
                val genre = items[idx]
                GenreRow(
                    genre = genre,
                    onClick = {
                        onGenreClicked(genre)
                    }
                )
            }

        )
    }
}

@Preview
@Composable
private fun Preview() {
    val genre = Genre(id = 0, name = "Action", emoticon = "✊")
    val genres = listOf(genre)
    GenresScreen(
        state = State().copy(displayState = GenresDisplayState.SuccessLoadGenres(genres)),
        sendIntent = {}
    )
}

package com.riza.example.explore.genres.compose

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.LiveData
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
            when(val display = state.displayState) {
                GenresDisplayState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is GenresDisplayState.SuccessLoadGenres -> {
                    LazyVerticalGrid(
                        modifier = Modifier.padding(innerPadding),
                        columns = GridCells.Adaptive(minSize = 100.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        val items = display.genres
                        items(
                            count = items.size,
                            key = { items[it].name },
                            itemContent = { idx: Int ->
                                GenreRow(
                                    genre = items[idx],
                                    onClick = {
                                        sendIntent(
                                            GenresViewModel.Intent.OnGenreClick(display.genres[idx])
                                        )
                                    }
                                )
                            }

                        )
                    }
                }
                is GenresDisplayState.ErrorLoadGenres -> {

                }
            }

        }
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
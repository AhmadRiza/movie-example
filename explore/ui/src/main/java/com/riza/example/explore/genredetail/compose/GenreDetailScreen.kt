package com.riza.example.explore.genredetail.compose

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    onBackPress: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                modifier = Modifier.padding(innerPadding),
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                val items = state.items
                items(
                    count = items.size,
                    key = {items[it].key},
                    itemContent = {idx: Int ->
                        when (val item = items[idx]) {
                            GenreDetailItemModel.LoadMore -> {
                            }
                            is GenreDetailItemModel.Movie -> {
                                MovieCell(movie = item, onClick = {})
                            }
                        }
                    }
                )

            }
        }

    }
}

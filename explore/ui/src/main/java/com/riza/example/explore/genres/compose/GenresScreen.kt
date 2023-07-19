package com.riza.example.explore.genres.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.riza.example.commonui.theme.MyTheme

/**
 * Created by ahmadriza on 18/07/23.
 */

data class State(
    val isLoading: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GenresScreen(
) {
    val state = State()

    MyTheme {
        Scaffold(
            topBar = {
                Column {
                    LargeTopAppBar(
                        title = {
                            Text(text = "Select Movie Genre")
                        },
                        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding
            ) {

            }
        }
    }

}

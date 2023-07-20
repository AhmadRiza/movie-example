package com.riza.example.explore.genres.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.riza.example.explore.data.model.Genre

/**
 * Created by ahmadriza on 19/07/23.
 */


@Preview
@Composable
private fun Preview() {
    GenreRow(genre = Genre(id = 0, name = "Mystery", emoticon = "🕵️‍"), onClick = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreRow(genre: Genre, onClick: () -> Unit) {

    ElevatedCard(
        modifier = Modifier.aspectRatio(1.0f),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = genre.emoticon, fontSize = 36.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = genre.name,
                style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center)
            )
        }

    }

}

@Preview
@Composable
fun GenreShimmer() {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .placeholder(
                visible = true,
                shape = RoundedCornerShape(12.dp),
                highlight = PlaceholderHighlight.shimmer()
            )

    )
}

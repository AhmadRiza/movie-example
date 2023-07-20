package com.riza.example.explore.genredetail.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import coil.compose.AsyncImage
import com.riza.example.explore.genredetail.state.GenreDetailItemModel
import com.riza.example.explore.genredetail.state.GenreDetailItemModel.Movie
import com.riza.example.explore.ui.R

/**
 * Created by ahmadriza on 20/07/23.
 */

@Preview
@Composable
fun Preview() {
    MovieCell(
        movie = Movie(
            id = 0,
            thumbnail = "",
            title = "Barbie The Movie",
            rating = "9.0/10",
            releaseDate = "June 2023"
        ),
        onClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCell(
    movie: Movie,
    onClick: (Int) -> Unit
) {
    OutlinedCard(
        onClick = {
            onClick(movie.id)
        }
    ) {
        Column {
            AsyncImage(
                model = movie.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.0.em
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier =Modifier.height(8.dp))
            Text(
                text = movie.releaseDate,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {

                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFFFF9800)
                )

                Text(text = movie.rating, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

        }


    }

}
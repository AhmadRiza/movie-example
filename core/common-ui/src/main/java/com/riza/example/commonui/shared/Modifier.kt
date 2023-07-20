package com.riza.example.commonui.shared

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

/**
 * Created by ahmadriza on 20/07/23.
 */
fun Modifier.shimmering(): Modifier = composed { this.then(
    placeholder(
        visible = true,
        shape = RoundedCornerShape(12.dp),
        highlight = PlaceholderHighlight.shimmer()
    )
) }
package com.chandra.syncnote.github

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun UpdateDownloadingScreen(viewModel: UpdateViewModel) {
    val progress = viewModel.progress
    val downloadedMB = viewModel.downloadedMB
    val totalMB = viewModel.totalMB

    val animatedProgress by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = tween(700, easing =
            FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier.size(140.dp),
                strokeWidth = 10.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(20.dp))

            // % with shimmer when progress = 0
            Text(
                "$progress%",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.placeholder(
                    visible = progress == 0,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer()
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "$downloadedMB MB / $totalMB MB",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(10.dp))

            Text("Downloading update...")
        }
    }
}

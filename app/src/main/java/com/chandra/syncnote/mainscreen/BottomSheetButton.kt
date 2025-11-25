package com.chandra.syncnote.mainscreen

import android.util.Log
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DefaultCustomChip(
    icon: ImageVector,
    text: String,
) {
    AssistChip(
        onClick = { Log.d("AssistChip", "hello world") },
        label = {
            Text(
                text = text,
                color = Color.Black
            )
        },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = "Settings Icon",
                tint = Color.Blue
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            labelColor = Color.White
        )
    )
}

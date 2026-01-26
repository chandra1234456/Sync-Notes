package com.chandra.syncnote.mainscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconWithText(
    icon : ImageVector,
    text: String
) {
    Row {
        Icon(
            imageVector = icon, // Use a pre-defined Material icon
            contentDescription = "Information Icon",
            modifier = Modifier.size(24.dp) // Set the icon size
        )
        Spacer(modifier = Modifier.size(8.dp)) // Add space between icon and text
        Text(
            text = text,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterVertically))
    }
}
@Composable
@Preview
fun DefaultCustomChip(
    icon: ImageVector = Icons.Default.Info,
    text: String = "Hello",
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        leadingIcon = { Icon(icon, contentDescription = null) }
    )
}

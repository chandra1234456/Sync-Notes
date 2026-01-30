package com.chandra.syncnote.widget

import android.content.Context
import android.text.style.StyleSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.background
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import com.chandra.syncnote.MainActivity
import com.chandra.syncnote.R

class NotesWidget : GlanceAppWidget() {

    @Composable
    fun Content() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(5.dp)
                .clickable(actionStartActivity<MainActivity>()), // Tap anywhere
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            // App Icon
            Image(
                provider = ImageProvider(R.drawable.ic_app), // Your app icon
                contentDescription = "App Icon",
                modifier = GlanceModifier.size(28.dp)       // Icon size
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            // App Name
            Text(
                text = "Sync Note",
                modifier = GlanceModifier.padding(bottom = 4.dp)
            )

            // Subtext
            Text(text = "Tap to add notes",)
        }
    }

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
       provideContent {
           Content()
       }
    }
}


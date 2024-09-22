package com.cistus.hunter.android.scenes.homeview

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cistus.hunter.UISize
import com.cistus.hunter.android.MainActivity
import com.cistus.hunter.android.UIDraw

class NotificationView(private val context: Context,
                       private val shareData: MutableState<MainActivity.ShareData>,
                       private val screenSize: MutableState<UISize>) {

    init {
        shareData.value = MainActivity.ShareData(shareData.value.notifications, false)
    }

    @Composable
    fun Draw() {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
            if (shareData.value.notifications.isEmpty())
                UIDraw.DrawText("お知らせはありません。", color = Color.Black)
            else
                UIDraw.DrawNotificationBoard(screenSize, shareData.value.notifications).Draw()
        }
    }
}
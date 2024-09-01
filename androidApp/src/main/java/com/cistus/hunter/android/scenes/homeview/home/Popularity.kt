package com.cistus.hunter.android.scenes.homeview.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UISize
import com.cistus.hunter.android.GiftContents
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.UIDraw

class Popularity(private val screenSize: MutableState<UISize>, private val isTablet: Boolean):
    TabItem {
    override val label: String = "人気"
    override val icon: Int? = null

    @Composable
    override fun Draw() {
        UIDraw.CustomColumn(style = "Top", spacing = 10.dp, modifier = Modifier.verticalScroll(
            rememberScrollState())) {
            UIDraw.CustomColumn(style = "TopStart", fillStyle = UIDraw.FILLSTYLE_MAXWIDTH,
                modifier = Modifier.padding(start = 16.dp)) {
                UIDraw.DrawText("人気", fontSize = 32f, color = Color.Black, style = "Bold")
            }
            GiftContents.DrawAttention(width = screenSize.value.width, height = screenSize.value.width,
                image = R.drawable.dummygift_attention, isTablet) {
            }
            GiftContents.DrawAttention(width = screenSize.value.width, height = screenSize.value.width,
                image = R.drawable.dummygift_attention, isTablet) {
            }
        }
    }

}
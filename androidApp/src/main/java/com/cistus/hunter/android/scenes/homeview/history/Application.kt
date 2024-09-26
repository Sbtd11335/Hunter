package com.cistus.hunter.android.scenes.homeview.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.android.GiftContents
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.UIDraw

class Application(private val screenSize: MutableState<UISize>, private val isTablet: Boolean): TabItem {
    override val label = "応募履歴"
    override val icon: Int? = null
    override val badge: Any? = null

    @Composable
    override fun Draw() {
        UIDraw.CustomColumn(style = "Top", spacing = 10.dp,
            modifier = Modifier.verticalScroll(rememberScrollState())) {
            UIDraw.CustomColumn(style = "TopStart", fillStyle = UIDraw.FILLSTYLE_MAXWIDTH,
                modifier = Modifier.padding(start = 16.dp)) {
                UIDraw.DrawText(label, fontSize = 32f, color = Color.Black, style = "Bold")
            }
            if (!isTablet) {
                GiftContents.DrawHistory(screenSize.value.width, screenSize.value.height,
                    R.drawable.dummygift_icon, isTablet) {
                }
                GiftContents.DrawHistory(screenSize.value.width, screenSize.value.height,
                    R.drawable.dummygift_icon, isTablet) {
                }
                GiftContents.DrawHistory(screenSize.value.width, screenSize.value.height,
                    R.drawable.dummygift_icon, isTablet) {
                }
            }
            else {
                UIDraw.CenterRow(spacing = 10.dp, fillStyle = UIDraw.FILLSTYLE_MAXWIDTH) {
                    GiftContents.DrawHistory(screenSize.value.width, screenSize.value.height,
                        R.drawable.dummygift_icon, isTablet) {
                    }
                    GiftContents.DrawHistory(screenSize.value.width, screenSize.value.height,
                        R.drawable.dummygift_icon, isTablet) {
                    }
                }
                UIDraw.CenterRow(spacing = 10.dp, fillStyle = UIDraw.FILLSTYLE_MAXWIDTH) {
                    GiftContents.DrawHistory(screenSize.value.width, screenSize.value.height,
                        R.drawable.dummygift_icon, isTablet) {
                    }
                    Box(modifier = Modifier.width(UIConfig.History.getContentFrameSize(screenSize.value, isTablet).width.dp))
                }
            }
        }
    }
}
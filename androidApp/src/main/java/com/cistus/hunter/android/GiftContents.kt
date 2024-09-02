package com.cistus.hunter.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.toDpSize

class GiftContents {
    companion object {
        @Composable
        fun DrawAttention(width: Double, height: Double, image: Int, isTablet: Boolean = false,
                          navigateTo: @Composable (Float) -> Unit) {
            val screenSize = UISize(width, height)
            val frameSize = UIConfig.Home.getAttentionFrameSize(screenSize, isTablet)
            val bottomBarSize = UIConfig.Home.getBottomBarFrame(frameSize)
            val showDialog = rememberSaveable { mutableStateOf(false) }

            UIDraw.DrawRCFrame(frameSize.toDpSize(),
                modifier = Modifier.shadow(3.dp, shape = RoundedCornerShape(15.dp)),
                onTapped = { showDialog.value = true }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(painterResource(image), "")
                    UIDraw.CustomColumn(style = "Bottom", modifier = Modifier.blur(5.dp)) {
                        Box(modifier = Modifier
                            .width(bottomBarSize.width.dp)
                            .height(bottomBarSize.height.dp)
                            .background(Color(1f, 1f, 1f, .25f)))
                    }
                }
            }
            if (showDialog.value)
                UIDraw.DrawDialog(showDialog, "Back", true) {
                    navigateTo(it)
                }
        }
        @Composable
        fun DrawLargeAttention(width: Double, height: Double, image: Int,
                               navigateTo: @Composable (Float) -> Unit) {
            val screenSize = UISize(width, height)
            val frameSize = UIConfig.Home.getLargeAttentionFrameSize(screenSize)
            val bottomBarSize = UIConfig.Home.getBottomBarFrame(frameSize)
            val showDialog = rememberSaveable { mutableStateOf(false) }

            UIDraw.DrawRCFrame(frameSize.toDpSize(), modifier = Modifier.shadow(3.dp),
                onTapped = { showDialog.value = true }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(painterResource(image), "")
                    UIDraw.CustomColumn(style = "Bottom", modifier = Modifier.blur(5.dp)) {
                        Box(modifier = Modifier
                            .width(bottomBarSize.width.dp)
                            .height(bottomBarSize.height.dp)
                            .background(Color(1f, 1f, 1f, .25f)))
                    }
                }
            }
            if (showDialog.value)
                UIDraw.DrawDialog(showDialog, "Back", true) {
                    navigateTo(it)
                }
        }
        @Composable
        fun DrawHistory(width: Double, height: Double, image: Int, isTablet: Boolean = false,
                        navigateTo: @Composable (Float) -> Unit) {
            val screenSize = UISize(width, height)
            val frameSize = UIConfig.History.getContentFrameSize(screenSize, isTablet)
            val imageSize = UIConfig.History.getContentIconFrameSize(screenSize, isTablet)
            val showDialog = rememberSaveable { mutableStateOf(false) }
            Box {
                UIDraw.DrawRCFrame(frameSize.toDpSize(), color = Color.White,
                    modifier = Modifier.border(1.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
                        .shadow(3.dp, shape = RoundedCornerShape(15.dp)),
                    onTapped = { showDialog.value = true }) {
                    UIDraw.CustomRow(style = "CenterStart", spacing = 5.dp,
                        modifier = Modifier.padding(5.dp)) {
                        Image(painterResource(image), "",
                            modifier = Modifier.width(imageSize.width.dp).height(imageSize.height.dp)
                                .padding(5.dp).clip(RoundedCornerShape(15.dp)))
                        UIDraw.CustomColumn(style = "TopStart") {
                            UIDraw.DrawText("ダミー", color = Color.Black, style = "Bold")
                            UIDraw.DrawText("ダミーテキスト", color = Color.Black)
                        }
                    }
                }
            }
            if (showDialog.value)
                UIDraw.DrawDialog(showDialog, closeText = "Back", true) {
                    navigateTo(it)
                }
        }
    }
}
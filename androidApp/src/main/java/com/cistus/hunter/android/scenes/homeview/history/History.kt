package com.cistus.hunter.android.scenes.homeview.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.android.MainActivity
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.toDpSize
import kotlinx.coroutines.launch

class History(private val shareData: MutableState<MainActivity.ShareData>,
              private val screenSize: MutableState<UISize>,
              private val showNotification: MutableState<Boolean>,isTablet: Boolean): TabItem {
    override val label: String = "履歴"
    override val icon: Int = R.drawable.book_fill
    private val tabItems = ArrayList<TabItem>()

    init {
        tabItems.add(Browsing(screenSize, isTablet))
        tabItems.add(Application(screenSize, isTablet))
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Draw() {
        val pagerState = rememberPagerState { tabItems.size }
        val coroutineScope = rememberCoroutineScope()
        val tabItemSize = UISize(screenSize.value.width / tabItems.size, 60.0)

        UIDraw.CustomColumn(style = "Top") {
            Box(modifier = Modifier.height((UIConfig.textlogoHeight + UIConfig.textlogoPadding).dp)) {
                UIDraw.CustomRow(style = "CenterStart", modifier = Modifier.padding(start = 10.dp)) {
                    Image(painterResource(R.drawable.textlogo), "",
                        modifier = Modifier.height((UIConfig.textlogoHeight + UIConfig.textlogoPadding).dp)
                            .padding(vertical = UIConfig.textlogoPadding.dp))
                }
                UIDraw.CustomRow(style = "CenterEnd", modifier = Modifier.padding(end = 10.dp)) {
                    Box {
                        Image(painterResource(R.drawable.bell), "",
                            modifier = Modifier.clickable { showNotification.value = true })
                        Box(modifier = Modifier.alpha(if (shareData.value.unreadNotifications) 1f else 0f)
                            .width(10.dp).height(10.dp).clip(CircleShape).background(color = Color.Red)
                            .align(Alignment.TopEnd))
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
                    UIDraw.CustomRow(style = "CenterStart") {
                        for (i in tabItems.indices) {
                            UIDraw.DrawRCFrame(tabItemSize.toDpSize(), radius = 0.dp,
                                color = Color.Transparent, onTapped = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(i)
                                    }
                                }) {
                                UIDraw.CenterColumn {
                                    UIDraw.DrawText(tabItems[i].label, color = Color.Black)
                                }
                            }
                        }
                    }
                }
                UIDraw.CustomColumn(style = "BottomStart",
                    modifier = Modifier.width(tabItemSize.width.dp).fillMaxHeight()
                        .offset(x = tabItemSize.width.dp * (pagerState.currentPage + pagerState.currentPageOffsetFraction))) {
                    Box(modifier = Modifier.width(tabItemSize.width.dp).height(4.dp)
                        .background(color = Color.ThemeColor))
                }
            }
            HorizontalPager(pagerState) {
                tabItems[it].Draw()
            }
        }
    }
}
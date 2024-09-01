package com.cistus.hunter.android.scenes.homeview.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UISize
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.toDpSize
import kotlinx.coroutines.launch

class Home(private val screenSize: MutableState<UISize>, isTablet: Boolean): TabItem {
    override val label: String = "ホーム"
    override val icon: Int = R.drawable.house
    private val tabItems = ArrayList<TabItem>()

    init {
        tabItems.add(Recommendation(screenSize, isTablet))
        tabItems.add(Popularity(screenSize, isTablet))
        tabItems.add(Expensive(screenSize, isTablet))
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Draw() {
        val pagerState = rememberPagerState { tabItems.size }
        val coroutineScope = rememberCoroutineScope()
        val tabItemSize = UISize(screenSize.value.width / tabItems.size, 60.0)
        UIDraw.CustomColumn(style = "Top") {
            UIDraw.DrawImage(R.drawable.textlogo, scale = .5f, bigger = false,
                modifier = Modifier.padding(vertical = 20.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)) {
                CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
                    UIDraw.CustomRow(style = "TopStart") {
                        for (i in tabItems.indices) {
                            UIDraw.DrawRCFrame(tabItemSize.toDpSize(), color = Color.Transparent,
                                onTapped = {
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
                Box(modifier = Modifier.offset(x = tabItemSize.toDpSize().width *
                        (pagerState.currentPage + pagerState.currentPageOffsetFraction))
                    .width(tabItemSize.toDpSize().width).fillMaxHeight()) {
                    UIDraw.CustomColumn(style = "Bottom") {
                        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color.ThemeColor))
                    }
                }
            }
            HorizontalPager(state = pagerState) {
                tabItems[it].Draw()
            }
        }
    }
}
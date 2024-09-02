package com.cistus.hunter.android.scenes.homeview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.android.SceneID
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.scenes.Scene
import com.cistus.hunter.android.scenes.homeview.history.History
import com.cistus.hunter.android.scenes.homeview.home.Home

class HomeContents(private val isTablet: Boolean): Scene {
    override val route = SceneID.home
    private val tabItems = ArrayList<TabItem>()

    @Composable
    override fun Draw() {
        val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }
        val screenSize = remember { mutableStateOf(UISize(0.0, 0.0)) }
        UIDraw.DrawBackGround {
            screenSize.value = UISize(UIDraw.toDp(it.width).value.toDouble(),
            UIDraw.toDp(it.height).value.toDouble())
        }
        if (tabItems.isEmpty()) {
            tabItems.add(Home(screenSize, isTablet))
            tabItems.add(History(screenSize, isTablet))
        }

        UIDraw.DrawBackGround()
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = UIConfig.Home.bottomTabHeight.dp)) {
            for (i in tabItems.indices) {
                if (selectedTabIndex.intValue == i)
                    tabItems[i].Draw()
            }
        }
        UIDraw.CustomColumn(style = "Bottom") {
            CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
                TabRow(selectedTabIndex = selectedTabIndex.intValue,
                    modifier = Modifier.height(UIConfig.Home.bottomTabHeight.dp),
                    indicator = { Box(modifier = Modifier.alpha(0f)) },
                    containerColor = Color.Transparent,
                    divider = { Box(modifier = Modifier.alpha(0f)) }) {
                    for (i in tabItems.indices) {
                        val itemColor = if (selectedTabIndex.intValue != i)
                            Color.Gray
                        else
                            Color.ThemeColor
                        Tab(selected = selectedTabIndex.intValue == i,
                            onClick = { selectedTabIndex.intValue = i },
                            modifier = Modifier.height(UIConfig.Home.bottomTabHeight.dp)) {
                            val drawContent = @Composable {
                                if (tabItems[i].icon != null)
                                    Image(painterResource(tabItems[i].icon as Int), "",
                                        colorFilter = ColorFilter.tint(itemColor))
                                UIDraw.DrawText(tabItems[i].label, color = itemColor, fontSize = 10f)
                            }
                            if (!isTablet)
                                UIDraw.CustomColumn(style = "Top",
                                    modifier = Modifier.padding(top = 7.dp)) {
                                    drawContent()
                                }
                            else
                                UIDraw.CustomRow(style = "Center",
                                    modifier = Modifier.padding(top = 7.dp)) {
                                    drawContent()
                                }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(device = "id:pixel_8", showSystemUi = true, showBackground = true)
fun Draw() {
    HomeContents(false).Draw()
}

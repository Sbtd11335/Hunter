package com.cistus.hunter.android.scenes.homeview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.android.MainActivity
import com.cistus.hunter.android.SceneID
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.data1.FirebaseDatabaseData1
import com.cistus.hunter.android.firebase.data3.FirebaseDatabaseData3
import com.cistus.hunter.android.scenes.Scene
import com.cistus.hunter.android.scenes.ToS
import com.cistus.hunter.android.scenes.homeview.history.History
import com.cistus.hunter.android.scenes.homeview.home.Home
import com.cistus.hunter.android.scenes.homeview.setting.Setting

class HomeContents(private val navController: NavController,
                   private val shareData: MutableState<MainActivity.ShareData>,
                   private val isTablet: Boolean): Scene {
    override val route = SceneID.home

    @Composable
    override fun Draw() {
        val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }
        val screenSize = remember { mutableStateOf(UISize(0.0, 0.0)) }
        val isToSUpdate = ToS.checkUpdate()
        val showToS = rememberSaveable { mutableStateOf(isToSUpdate) }
        val context = LocalContext.current
        val showNotification = rememberSaveable { mutableStateOf(false) }
        val tabItems = ArrayList<TabItem>()

        UIDraw.DrawBackGround {
            screenSize.value = UISize(UIDraw.toDp(it.width).value.toDouble(),
            UIDraw.toDp(it.height).value.toDouble())
        }
        if (tabItems.isEmpty()) {
            tabItems.add(Home(shareData, screenSize, showNotification, isTablet))
            tabItems.add(History(shareData, screenSize, showNotification, isTablet))
            tabItems.add(Message(shareData, screenSize, selectedTabIndex, showNotification))
            tabItems.add(Setting(shareData, navController, screenSize, tabItems, showNotification))
        }
        UIDraw.DrawBackGround()
        Box(modifier = Modifier.fillMaxSize().padding(bottom = UIConfig.Home.bottomTabHeight.dp)) {
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
                                Box {
                                    if (tabItems[i].icon != null) {
                                        Image(painterResource(tabItems[i].icon as Int), "",
                                            colorFilter = ColorFilter.tint(itemColor))
                                        tabItems[i].badge?.let { shareDataBadge ->
                                            val unreadMessages = (shareDataBadge as MutableState<MainActivity.ShareData>).value.unreadMessages
                                            val badge = unreadMessages.toString()
                                            Box(modifier = Modifier.offset(x = 10.dp, y = (-5).dp).alpha(if (unreadMessages == 0)0f else 1f)
                                                .width(17.dp + 12.dp * (badge.length - 1)).height(17.dp).clip(CircleShape).background(color = Color.Red)
                                                .align(Alignment.TopEnd)) {
                                                UIDraw.DrawText(badge, color = Color.White, fontSize = 12f,
                                                    modifier = Modifier.align(Alignment.Center))
                                            }
                                        }
                                    }
                                }
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
        UIDraw.DrawDialog(showToS, fullScreen = true, closeText = "Back") {
             ToS(LocalContext.current).Draw(it)
        }
        UIDraw.DrawDialog(showNotification, closeText = "Back", fullScreen = true) { padding ->
            UIDraw.DrawBackGround {
                UIDraw.CustomColumn(style = "Top") {
                    UIDraw.CenterRow(fillStyle = UIDraw.FILLSTYLE_MAXWIDTH,
                        modifier = Modifier.height(UIDraw.toDp(padding))) {
                        UIDraw.DrawText("お知らせ", color = Color.Black, style = "Bold")
                    }
                    NotificationView(context, shareData, screenSize).Draw()
                }
            }
        }
        LaunchedEffect(Unit) {
            val data1 = FirebaseDatabaseData1(context)
            val data3 = FirebaseDatabaseData3(context)
            data1.getData1(shareData)
            data3.getData1(shareData)
        }
    }
}
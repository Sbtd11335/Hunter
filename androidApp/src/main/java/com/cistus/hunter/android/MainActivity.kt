package com.cistus.hunter.android

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cistus.hunter.AndroidScreen
import com.cistus.hunter.MessageData
import com.cistus.hunter.android.scenes.Boot
import com.cistus.hunter.android.scenes.Scene
import com.cistus.hunter.android.scenes.homeview.HomeContents
import com.cistus.hunter.android.scenes.signinview.SignIn
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val hideSystemBars = true

    @SuppressLint("MutableCollectionMutableState")
    class ShareData(notifications: ArrayList<UIDraw.ListItem> = arrayListOf(),
                    unreadNotifications: Boolean = false,
                    messages: ArrayList<MessageData> = arrayListOf(),
                    unreadMessages: Int = 0) {

        val notifications = mutableStateListOf<UIDraw.ListItem>()
        var unreadNotifications by mutableStateOf(unreadNotifications)
        var messages = mutableStateListOf<MessageData>()
        var unreadMessages by mutableIntStateOf(unreadMessages)
        var data3 by mutableStateOf(ShareDatabase.Data3())

        init {
            this.notifications.addAll(notifications)
            this.messages.addAll(messages)
        }

        class ShareDatabase {
            class Data3 {
                var data1_new = 0
                var data1_count = 0
            }
        }

        fun clear() {
            notifications.clear()
            unreadNotifications = false
            messages.clear()
            unreadMessages = 0
            data3 = ShareDatabase.Data3()
        }
    }

    companion object {
        val shareDataSaver = mapSaver(save = {
            mapOf("notifications" to ArrayList(it.notifications),
                "unreadNotifications" to it.unreadNotifications,
                "messages" to ArrayList(it.messages),
                "unreadMessages" to it.unreadMessages)
        }, restore = {
            ShareData(it["notifications"] as ArrayList<UIDraw.ListItem>,
                it["unreadNotifications"] as Boolean,
                it["messages"] as ArrayList<MessageData>,
                it["unreadMessages"] as Int)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isTablet: Boolean

        if (resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK < Configuration.SCREENLAYOUT_SIZE_LARGE) {
            isTablet = false
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        else
            isTablet = true

        setContent {
            AndroidScreen.Initialize(windowManager)
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scenes = ArrayList<Scene>()
                    val navController = rememberNavController()
                    val shareData = rememberSaveable(stateSaver = shareDataSaver) {
                        mutableStateOf(ShareData())
                    }

                    if (scenes.isEmpty()) {
                        scenes.add(Boot(navController, shareData))
                        scenes.add(SignIn(navController, shareData))
                        scenes.add(HomeContents(navController, shareData, isTablet))
                    }
                    NavHost(navController = navController, startDestination = SceneID.boot) {
                        for (scene in scenes)
                            composable(scene.route) { scene.Draw() }
                    }
                }
            }
            if (hideSystemBars)
                LaunchedEffect(Unit) {
                    while(true) {
                        AndroidScreen.hideSystemBars(window)
                        delay(1000)
                    }
                }
        }
    }
}

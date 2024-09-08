package com.cistus.hunter.android

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cistus.hunter.AndroidScreen
import com.cistus.hunter.android.scenes.Boot
import com.cistus.hunter.android.scenes.Scene
import com.cistus.hunter.android.scenes.homeview.HomeContents
import com.cistus.hunter.android.scenes.loginview.Login
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val hideSystemBars = true

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
                    if (scenes.isEmpty()) {
                        scenes.add(Boot(navController))
                        scenes.add(Login(navController))
                        scenes.add(HomeContents(navController, isTablet))
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

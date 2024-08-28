package com.cistus.hunter.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cistus.hunter.AndroidScreen
import com.cistus.hunter.android.scenes.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidScreen.initialize(windowManager)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scenes = ArrayList<Scene>()
                    val navController = rememberNavController()
                    if (scenes.isEmpty()) {
                        scenes.add(Boot())
                    }
                    NavHost(navController = navController, startDestination = SceneID.boot) {
                        for (scene in scenes)
                            composable(scene.route) { scene.Draw() }
                    }
                }
            }
            LaunchedEffect(Unit) {
                while(true) {
                    AndroidScreen.hideSystemBars(window)
                    delay(1000)
                }
            }
        }
    }
}

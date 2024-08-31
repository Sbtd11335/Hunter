package com.cistus.hunter.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cistus.hunter.AndroidScreen
import com.cistus.hunter.android.scenes.Boot
import com.cistus.hunter.android.scenes.Scene
import com.cistus.hunter.android.scenes.loginview.Login

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK < Configuration.SCREENLAYOUT_SIZE_LARGE)
            requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


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
                        scenes.add(Boot())
                        scenes.add(Login())
                    }
                    NavHost(navController = navController, startDestination = SceneID.login) {
                        for (scene in scenes)
                            composable(scene.route) { scene.Draw() }
                    }
                }
            }

        }
    }
}

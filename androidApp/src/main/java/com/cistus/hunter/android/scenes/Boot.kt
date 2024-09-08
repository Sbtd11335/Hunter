package com.cistus.hunter.android.scenes

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.cistus.hunter.DeviceTime
import com.cistus.hunter.Screen
import com.cistus.hunter.android.R
import com.cistus.hunter.android.SceneID
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.FirebaseAuth
import kotlinx.coroutines.delay
import kotlin.math.pow

class Boot(private val navController: NavController): Scene {
    override val route = SceneID.boot

    private class TextLogoShape(private val dx: Float): Shape {
        override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
            val path = Path().apply {
                addRect(Rect(0f, 0f, size.width * dx, size.height))
            }
            return Outline.Generic(path)
        }
    }

    @Composable
    override fun Draw() {
        val textlogoMax = Screen.smallerSize / 2
        val textlogoDx = rememberSaveable { mutableFloatStateOf(0f) }

        UIDraw.DrawBackGround {
            UIDraw.CenterColumn {
                UIDraw.CenterRow(fillStyle = UIDraw.FILLSTYLE_MAXWIDTH) {
                    UIDraw.DrawImage(R.drawable.logo, scale = .28f, bigger = false,
                        modifier = Modifier.offset(x = -UIDraw.toDp(textlogoMax * textlogoDx.floatValue - textlogoMax)))
                    UIDraw.DrawImage(R.drawable.textlogo, scale = .5f, bigger = false,
                        modifier = Modifier
                            .clip(TextLogoShape(textlogoDx.floatValue))
                            .offset(x = -UIDraw.toDp(textlogoMax * textlogoDx.floatValue / 2 - textlogoMax / 2)))
                }
                UIDraw.CenterColumn(fillStyle = UIDraw.FILLSTYLE_MAXWIDTH, hide = textlogoDx.floatValue != 1f) {
                    UIDraw.DrawProgress(color = Color.Black)
                }
            }
            UIDraw.DrawVersion()
        }

        LaunchedEffect(Unit) {
            delay(400)
            val start = DeviceTime.nanoTime()
            while(DeviceTime.nanoTime() - start < 1.0) {
                textlogoDx.floatValue = 1f - (1f - (DeviceTime.nanoTime() - start)).pow(4).toFloat()
                delay(1)
            }
            textlogoDx.floatValue = 1f
            login()
        }
    }

    private fun login() {
        val auth = FirebaseAuth()
        if (auth.currentUser() == null)
            navController.navigate(SceneID.login)
        auth.reload { result ->
            if (result != null)
                navController.navigate(SceneID.login)
            else {
                auth.isEmailVerified()?.also { isEmailVerified ->
                    if (!isEmailVerified)
                        navController.navigate(SceneID.login)
                    else
                        navController.navigate(SceneID.home)
                } ?: run {
                    navController.navigate(SceneID.login)
                }
            }
        }
    }

}
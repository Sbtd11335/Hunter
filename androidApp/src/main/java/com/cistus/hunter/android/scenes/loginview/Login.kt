package com.cistus.hunter.android.scenes.loginview

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.android.R
import com.cistus.hunter.android.SceneID
import com.cistus.hunter.android.TextButton
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.scenes.Scene
import com.cistus.hunter.toDpSize

class Login: Scene {
    override val route = SceneID.login
    @Composable
    override fun Draw() {
        val emailAddress = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        val statusText = rememberSaveable { mutableStateOf("") }
        val hidePassword = rememberSaveable { mutableStateOf(true) }
        val forgotPasswordView = rememberSaveable { mutableStateOf(false) }
        val createAccountView = rememberSaveable { mutableStateOf(false) }
        val textFieldFocus = LocalFocusManager.current

        CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
            UIDraw.DrawBackGround (onTapped = { textFieldFocus.clearFocus() }){
                UIDraw.CenterColumn(spacing = 10.dp) {
                    UIDraw.DrawImage(R.drawable.textlogo, .5f)
                    UIDraw.DrawText("アカウントにログイン", color = Color.Black)
                    UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false)
                    UIDraw.DrawTextField(emailAddress, UIConfig.Login.rcFrameSize.toDpSize(),
                        label = "メールアドレス", singleLine = true)
                    UIDraw.DrawSecureField(password, hidePassword, UIConfig.Login.rcFrameSize.toDpSize(),
                        label = "パスワード", singleLine = true)
                    UIDraw.DrawRCFrame(UIConfig.Login.rcFrameSize.toDpSize(), color = Color.ThemeColor,
                        onTapped = {
                            textFieldFocus.clearFocus()
                            login(emailAddress, password, statusText)
                        }) {
                        UIDraw.CenterColumn {
                            UIDraw.DrawText("ログイン", color = Color.White)
                        }
                    }
                    UIDraw.DrawText("パスワードを忘れた場合", color = Color.TextButton) {
                        forgotPasswordView.value = true
                        createAccountView.value = false
                    }
                    UIDraw.DrawText("アカウントを新規作成", color = Color.TextButton) {
                        forgotPasswordView.value = false
                        createAccountView.value = true
                    }
                    UIDraw.DrawDialog(forgotPasswordView, "完了", fullScreen = true) {
                        ForgotPassword().Draw()
                    }
                    UIDraw.DrawDialog(createAccountView, "完了", fullScreen = true) {
                        CreateAccount().Draw()
                    }
                }
                UIDraw.DrawVersion()
            }
        }
    }

    private fun login(emailAddress: MutableState<String>, password: MutableState<String>,
                      statusText: MutableState<String>) {

    }
}
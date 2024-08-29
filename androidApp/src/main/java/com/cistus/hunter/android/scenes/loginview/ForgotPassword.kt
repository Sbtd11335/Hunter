package com.cistus.hunter.android.scenes.loginview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TextButton
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.toDpSize

class ForgotPassword {
    @Composable
    fun Draw() {
        val emailAddress = rememberSaveable { mutableStateOf("") }
        val statusText = rememberSaveable { mutableStateOf("") }
        val textFieldFocus = LocalFocusManager.current
        val clipBoard = LocalClipboardManager.current

        UIDraw.DrawBackGround(onTapped = { textFieldFocus.clearFocus() }) {
            UIDraw.CenterColumn(spacing = 10.dp) {
                UIDraw.DrawImage(R.drawable.textlogo, .5f, bigger = false)
                UIDraw.DrawText("パスワードを再設定", color = Color.Black)
                UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false)
                UIDraw.DrawTextField(emailAddress, UIConfig.Login.rcFrameSize.toDpSize(),
                    label = "メールアドレス", singleLine = true)
                UIDraw.DrawRCFrame(UIConfig.Login.rcFrameSize.toDpSize(), color = Color.ThemeColor,
                    onTapped = {
                        textFieldFocus.clearFocus()
                        request(emailAddress, statusText)
                    }) {
                    UIDraw.CenterColumn {
                        UIDraw.DrawText("リクエスト", color = Color.White)
                    }
                }
                UIDraw.DrawText("ご不明な点がございましたら、お手数ですが、", color = Color.Black)
                UIDraw.DrawText("CistusSystem@gmail.com", color = Color.TextButton) {
                    clipBoard.setText(AnnotatedString("CistusSystem@gmail.com"))
                    statusText.value = "クリップボードにコピーしました。"
                }
                UIDraw.DrawText("までご連絡ください。", color = Color.Black)
            }
            UIDraw.DrawVersion()
        }
    }

    private fun request(emailAddress: MutableState<String>, statusText: MutableState<String>) {

    }
}
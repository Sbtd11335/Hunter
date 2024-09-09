package com.cistus.hunter.android.scenes.homeview.setting.account

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TextButton
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.FirebaseAuth
import com.cistus.hunter.toDpSize

class UpdatePassword(private val height: Float) {
    private val rcFrameSize = UIConfig.Login.rcFrameSize

    @Composable
    fun Draw() {
        val statusText = rememberSaveable { mutableStateOf("") }
        val clipBoard = LocalClipboardManager.current
        val context = LocalContext.current
        CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
            UIDraw.DrawBackGround {
                UIDraw.CenterColumn(spacing = 10.dp,
                    modifier = Modifier.padding(top = UIDraw.toDp(px = height))) {
                    UIDraw.DrawImage(R.drawable.textlogo, scale = .5f, bigger = false)
                    UIDraw.DrawText("パスワードの変更", color = Color.Black)
                    UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false,
                        textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = UIConfig.Login.rcFrameSize.width.dp))
                    UIDraw.DrawRCFrame(rcFrameSize.toDpSize(), color = Color.ThemeColor, onTapped = {
                        request(context, statusText)
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
                    UIDraw.DrawText("もしくはメッセージまでご連絡ください。", color = Color.Black)
                }
            }
        }
    }

    private fun request(context: Context, statusText: MutableState<String>) {
        val auth = FirebaseAuth()
        statusText.value = "情報を確認しています..."
        auth.currentUser()?.email?.also { currentUserEmailAddress ->
            auth.sendPasswordReset(currentUserEmailAddress) { result ->
                result?.also { error ->
                    when(error) {
                        "ERROR_NETWORK_ERROR" -> statusText.value = context.getString(R.string.AuthErrorCodes_NetworkError)
                        "ERROR_INVALID_API_KEY" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidAPIKey)
                        "ERROR_INVALID_EMAIL" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidEmail)
                        else -> statusText.value = error
                    }
                } ?: run {
                    statusText.value = "設定されたメールアドレス宛に変更用メールを送信しました。"
                }
            }
        } ?: run {
            statusText.value = context.getString(R.string.AuthErrorCodes_UserNotFound)
        }
    }

}
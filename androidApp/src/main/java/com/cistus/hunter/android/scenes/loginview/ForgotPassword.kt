package com.cistus.hunter.android.scenes.loginview

import android.content.Context
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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

class ForgotPassword {
    @Composable
    fun Draw() {
        val emailAddress = rememberSaveable { mutableStateOf("") }
        val statusText = rememberSaveable { mutableStateOf("") }
        val textFieldFocus = LocalFocusManager.current
        val clipBoard = LocalClipboardManager.current
        val localContext = LocalContext.current

        UIDraw.DrawBackGround(onTapped = { textFieldFocus.clearFocus() }) {
            UIDraw.CenterColumn(spacing = 10.dp) {
                UIDraw.DrawImage(R.drawable.textlogo, .5f, bigger = false)
                UIDraw.DrawText("パスワードを再設定", color = Color.Black)
                UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false,
                    textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = UIConfig.Login.rcFrameSize.width.dp))
                UIDraw.DrawTextField(emailAddress, UIConfig.Login.rcFrameSize.toDpSize(),
                    label = "メールアドレス", singleLine = true)
                UIDraw.DrawRCFrame(UIConfig.Login.rcFrameSize.toDpSize(), color = Color.ThemeColor,
                    onTapped = {
                        textFieldFocus.clearFocus()
                        request(localContext, emailAddress, statusText)
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

    private fun request(context: Context, emailAddress: MutableState<String>, statusText: MutableState<String>) {
        val auth = FirebaseAuth()
        statusText.value = "情報を確認しています..."
        if (emailAddress.value.isEmpty()) {
            statusText.value = "メールアドレスを入力してください。"
            return
        }
        auth.sendPasswordReset(emailAddress.value) { resultOptional ->
            resultOptional?.also { result ->
                when(result) {
                    "ERROR_NETWORK_ERROR" -> statusText.value = context.getString(R.string.AuthErrorCodes_NetworkError)
                    "ERROR_INVALID_API_KEY" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidAPIKey)
                    "ERROR_INVALID_EMAIL" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidEmail)
                    else -> statusText.value = result
                }
            } ?: run {
                statusText.value = "入力されたメールアドレス宛に再設定用メールを送信しました。"
            }
        }
    }

}
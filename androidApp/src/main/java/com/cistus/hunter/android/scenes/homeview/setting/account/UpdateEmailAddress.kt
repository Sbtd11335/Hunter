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
import com.google.firebase.auth.EmailAuthProvider

class UpdateEmailAddress(private val height: Float) {
    private val rcFrameSize = UIConfig.Login.rcFrameSize

    @Composable
    fun Draw() {
        val emailAddress = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        val statusText = rememberSaveable { mutableStateOf("") }
        val hidePassword = rememberSaveable { mutableStateOf(true) }
        val clipBoard = LocalClipboardManager.current
        val context = LocalContext.current
        val textFieldFocus = LocalFocusManager.current

        CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
            UIDraw.DrawBackGround(onTapped = { textFieldFocus.clearFocus() }) {
                UIDraw.CenterColumn(spacing = 10.dp,
                    modifier = Modifier.padding(top = UIDraw.toDp(height))) {
                    UIDraw.DrawImage(R.drawable.textlogo, scale = .5f, bigger = false)
                    UIDraw.DrawText("メールアドレスの変更", color = Color.Black)
                    UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false,
                        textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = UIConfig.Login.rcFrameSize.width.dp))
                    UIDraw.DrawTextField(emailAddress, rcFrameSize.toDpSize(), label = "新しいメールアドレス", singleLine = true)
                    UIDraw.DrawSecureField(password, hidePassword, rcFrameSize.toDpSize(), label = "現在のパスワード", singleLine = true)
                    UIDraw.DrawRCFrame(rcFrameSize.toDpSize(), color = Color.ThemeColor, onTapped = {
                        request(context, statusText, emailAddress, password)
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

    private fun request(context: Context, statusText: MutableState<String>,
                        emailAddress: MutableState<String>, password: MutableState<String>) {
        val auth = FirebaseAuth()
        statusText.value = "情報を確認しています..."
        auth.currentUser()?.email?.also { currentUserEmailAddress ->
            if (emailAddress.value.isEmpty()) {
                statusText.value = "メールアドレスを入力してください。"
                return
            }
            if (password.value.isEmpty()) {
                statusText.value = "パスワードを入力してください。"
                return
            }
            val emailAuthCredential = EmailAuthProvider.getCredential(currentUserEmailAddress, password.value)
            auth.reAuthenticate(emailAuthCredential) { result ->
                result?.also {
                    when(result) {
                        "ERROR_NETWORK_ERROR" -> statusText.value = context.getString(R.string.AuthErrorCodes_NetworkError)
                        "ERROR_INVALID_API_KEY" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidAPIKey)
                        "ERROR_INVALID_EMAIL" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidEmail)
                        "ERROR_USER_DISABLED" -> statusText.value = context.getString(R.string.AuthErrorCodes_UserDisabled)
                        "ERROR_WRONG_PASSWORD" -> statusText.value = context.getString(R.string.AuthErrorCodes_WrongPassword)
                        "ERROR_INVALID_CREDENTIAL" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidCredential)
                        else -> statusText.value = result
                    }
                } ?: run {
                    auth.updateEmailAddress(emailAddress.value) { updateEmailResult ->
                        updateEmailResult?.also {
                            when(updateEmailResult) {
                                "ERROR_NETWORK_ERROR" -> statusText.value = context.getString(R.string.AuthErrorCodes_NetworkError)
                                "ERROR_INVALID_API_KEY" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidAPIKey)
                                "ERROR_INVALID_EMAIL" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidEmail)
                                "ERROR_EMAIL_ALREADY_IN_USE" -> statusText.value = context.getString(R.string.AuthErrorCodes_EmailAlreadyInUse)
                                "ERROR_WEAK_PASSWORD" -> statusText.value = context.getString(R.string.AuthErrorCodes_WeakPassword)
                                else -> statusText.value = updateEmailResult
                            }
                        } ?: run {
                            statusText.value = "入力されたメールアドレス宛に認証用メールを送信しました。認証が完了したら自動でサインアウトされるため、再起動してください。"
                        }
                    }
                }
            }
        } ?: run {
            statusText.value = context.getString(R.string.AuthErrorCodes_UserNotFound)
        }
    }
}
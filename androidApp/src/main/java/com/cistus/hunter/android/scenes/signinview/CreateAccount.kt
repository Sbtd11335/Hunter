package com.cistus.hunter.android.scenes.signinview

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TextButton
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.FirebaseAuth
import com.cistus.hunter.android.scenes.ToS
import com.cistus.hunter.toDpSize

class CreateAccount {
    private val rcFrameSize = UIConfig.SignIn.rcFrameSize
    
    @Composable
    fun Draw() {
        val emailAddress = rememberSaveable { mutableStateOf("") }
        val password1 = rememberSaveable { mutableStateOf("") }
        val password2 = rememberSaveable { mutableStateOf("") }
        val statusText = rememberSaveable { mutableStateOf("") }
        val hidePassword = rememberSaveable { mutableStateOf(true) }
        val isChecked = rememberSaveable { mutableStateOf(false) }
        val showToS = rememberSaveable { mutableStateOf(false) }
        val textFieldFocus = LocalFocusManager.current
        val clipBoard = LocalClipboardManager.current
        val localContext = LocalContext.current

        UIDraw.DrawBackGround(onTapped = { textFieldFocus.clearFocus() }) {
            UIDraw.CenterColumn(spacing = 10.dp) {
                UIDraw.DrawImage(R.drawable.textlogo, .5f, bigger = false)
                UIDraw.DrawText("アカウントを新規作成", color = Color.Black)
                UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false,
                    textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = rcFrameSize.width.dp))
                UIDraw.DrawTextField(emailAddress, rcFrameSize.toDpSize(),
                    label = "メールアドレス", singleLine = true)
                UIDraw.DrawSecureField(password1, hidePassword, rcFrameSize.toDpSize(),
                    label = "パスワード", singleLine = true)
                UIDraw.DrawSecureField(password2, hidePassword, rcFrameSize.toDpSize(),
                    label = "パスワード(確認用)", singleLine = true)
                UIDraw.CenterRow(spacing = 20.dp, fillStyle = UIDraw.FILLSTYLE_MAXWIDTH) {
                    Row {
                        UIDraw.DrawText("利用規約", color = Color.TextButton) {
                            showToS.value = true
                        }
                        UIDraw.DrawText("に同意する", color = Color.Black)
                    }
                    UIDraw.DrawRCFrame(UIConfig.SignIn.checkBoxSize.toDpSize(), color = Color.White,
                        radius = 7.dp, onTapped = { isChecked.value = !isChecked.value }) {
                        UIDraw.CenterColumn(hide = !isChecked.value) {
                            Image(painterResource(R.drawable.checkmark), "",
                                modifier = Modifier.padding(8.dp))
                        }
                    }
                }
                UIDraw.DrawRCFrame(rcFrameSize.toDpSize(), color = Color.ThemeColor,
                    onTapped = {
                        textFieldFocus.clearFocus()
                        request(localContext, emailAddress, password1, password2, isChecked, statusText)
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
        UIDraw.DrawDialog(showDialog = showToS, closeText = "Back", fullScreen = true) {
            ToS(LocalContext.current).Draw(it)
        }
    }

    private fun request(context: Context, emailAddress: MutableState<String>, password1: MutableState<String>,
                        password2: MutableState<String>, isChecked: MutableState<Boolean>,
                        statusText: MutableState<String>) {
        val auth = FirebaseAuth()
        statusText.value = "情報を確認しています..."
        if (emailAddress.value.isEmpty()) {
            statusText.value = "メールアドレスを入力してください。"
            return
        }
        if (password1.value != password2.value) {
            statusText.value = "確認用パスワードが一致しません。"
            return
        }
        if (!isChecked.value) {
            statusText.value = "利用規約に同意してください。"
            return
        }
        auth.createAccount(emailAddress.value, password1.value) { resultOptional ->
            resultOptional?.also { result ->
                when(result) {
                    "ERROR_NETWORK_ERROR" -> statusText.value = context.getString(R.string.AuthErrorCodes_NetworkError)
                    "ERROR_INVALID_API_KEY" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidAPIKey)
                    "ERROR_INVALID_EMAIL" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidEmail)
                    "ERROR_EMAIL_ALREADY_IN_USE" -> statusText.value = context.getString(R.string.AuthErrorCodes_EmailAlreadyInUse)
                    "ERROR_WEAK_PASSWORD" -> statusText.value = context.getString(R.string.AuthErrorCodes_WeakPassword)
                    else -> statusText.value = result
                }
            } ?: run {
                auth.sendEmailVerification { sendEmailResultOptional ->
                    sendEmailResultOptional?.also { result ->
                        when(result) {
                            "ERROR_NETWORK_ERROR" -> statusText.value = context.getString(R.string.AuthErrorCodes_NetworkError)
                            "ERROR_INVALID_API_KEY" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidAPIKey)
                            "ERROR_USER_NOT_FOUND" -> statusText.value = context.getString(R.string.AuthErrorCodes_UserNotFound)
                            else -> statusText.value = result
                        }
                    } ?: run {
                        statusText.value = "入力されたメールアドレス宛に確認用メールを送信しました。"
                    }
                }
            }
        }
    }

}

package com.cistus.hunter.android.scenes.loginview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.cistus.hunter.UIConfig
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TextButton
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.toDpSize

class CreateAccount {
    @Composable
    fun Draw() {
        val emailAddress = rememberSaveable { mutableStateOf("") }
        val password1 = rememberSaveable { mutableStateOf("") }
        val password2 = rememberSaveable { mutableStateOf("") }
        val statusText = rememberSaveable { mutableStateOf("") }
        val hidePassword = rememberSaveable { mutableStateOf(true) }
        val isChecked = rememberSaveable { mutableStateOf(false) }
        val textFieldFocus = LocalFocusManager.current
        val clipBoard = LocalClipboardManager.current

        UIDraw.DrawBackGround(onTapped = { textFieldFocus.clearFocus() }) {
            UIDraw.CenterColumn(spacing = 10.dp) {
                UIDraw.DrawImage(R.drawable.textlogo, .5f, bigger = false)
                UIDraw.DrawText("アカウントを新規作成", color = Color.Black)
                UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false)
                UIDraw.DrawTextField(emailAddress, UIConfig.Login.rcFrameSize.toDpSize(),
                    label = "メールアドレス", singleLine = true)
                UIDraw.DrawSecureField(password1, hidePassword, UIConfig.Login.rcFrameSize.toDpSize(),
                    label = "パスワード", singleLine = true)
                UIDraw.DrawSecureField(password2, hidePassword, UIConfig.Login.rcFrameSize.toDpSize(),
                    label = "パスワード(確認用)", singleLine = true)
                UIDraw.CenterRow(spacing = 20.dp, fillStyle = UIDraw.FILLSTYLE_MAXWIDTH) {
                    Row {
                        UIDraw.DrawText("利用規約", color = Color.TextButton)
                        UIDraw.DrawText("に同意する", color = Color.Black)
                    }
                    UIDraw.DrawRCFrame(UIConfig.Login.checkBoxSize.toDpSize(), color = Color.White,
                        radius = 7.dp, onTapped = { isChecked.value = !isChecked.value }) {
                        UIDraw.CenterColumn(hide = !isChecked.value) {
                            Image(painterResource(R.drawable.checkmark), "",
                                modifier = Modifier.padding(8.dp))
                        }
                    }
                }
                UIDraw.DrawRCFrame(UIConfig.Login.rcFrameSize.toDpSize(), color = Color.ThemeColor,
                    onTapped = {
                        textFieldFocus.clearFocus()
                        request(emailAddress, password1, password2, isChecked, statusText)
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

    private fun request(emailAddress: MutableState<String>, password1: MutableState<String>,
                        password2: MutableState<String>, isChecked: MutableState<Boolean>,
                        statusText: MutableState<String>) {

    }

}
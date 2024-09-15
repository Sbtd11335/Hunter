package com.cistus.hunter.android.scenes.signinview

import android.content.Context
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cistus.hunter.UIConfig
import com.cistus.hunter.android.MainActivity
import com.cistus.hunter.android.R
import com.cistus.hunter.android.SceneID
import com.cistus.hunter.android.TextButton
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.FirebaseAuth
import com.cistus.hunter.android.scenes.Scene
import com.cistus.hunter.toDpSize

class SignIn(private val navController: NavController,
             private val shareData: MutableState<MainActivity.ShareData>): Scene {
    private val rcFrameSize = UIConfig.SignIn.rcFrameSize
    override val route = SceneID.signIn
    @Composable
    override fun Draw() {
        val emailAddress = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        val statusText = rememberSaveable { mutableStateOf("") }
        val hidePassword = rememberSaveable { mutableStateOf(true) }
        val forgotPasswordView = rememberSaveable { mutableStateOf(false) }
        val createAccountView = rememberSaveable { mutableStateOf(false) }
        val textFieldFocus = LocalFocusManager.current
        val localContext = LocalContext.current

        CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
            UIDraw.DrawBackGround (onTapped = { textFieldFocus.clearFocus() }){
                UIDraw.CenterColumn(spacing = 10.dp) {
                    UIDraw.DrawImage(R.drawable.textlogo, .5f)
                    UIDraw.DrawText("アカウントにサインイン", color = Color.Black)
                    UIDraw.DrawText(statusText.value, color = Color.Red, emptyDraw = false,
                        textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = rcFrameSize.width.dp))
                    UIDraw.DrawTextField(emailAddress, rcFrameSize.toDpSize(),
                        label = "メールアドレス", singleLine = true)
                    UIDraw.DrawSecureField(password, hidePassword, rcFrameSize.toDpSize(),
                        label = "パスワード", singleLine = true)
                    UIDraw.DrawRCFrame(rcFrameSize.toDpSize(), color = Color.ThemeColor,
                        onTapped = {
                            textFieldFocus.clearFocus()
                            signIn(localContext, emailAddress, password, statusText)
                        }) {
                        UIDraw.CenterColumn {
                            UIDraw.DrawText("サインイン", color = Color.White)
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
    private fun signIn(context: Context, emailAddress: MutableState<String>,
                      password: MutableState<String>, statusText: MutableState<String>) {
        val auth = FirebaseAuth()
        statusText.value = "情報を確認しています..."
        if (emailAddress.value.isEmpty()) {
            statusText.value = "メールアドレスを入力してください。"
            return
        }
        if (password.value.isEmpty()) {
            statusText.value = "パスワードを入力してください。"
            return
        }
        auth.signIn(emailAddress.value, password.value) { resultOptional ->
            resultOptional?.also { result ->
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
                auth.isEmailVerified()?.also { isEmailVerified ->
                    if (!isEmailVerified) {
                        auth.sendEmailVerification { sendEmailResult ->
                            sendEmailResult?.also {
                                when(sendEmailResult) {
                                    "ERROR_NETWORK_ERROR" -> statusText.value = context.getString(R.string.AuthErrorCodes_NetworkError)
                                    "ERROR_INVALID_API_KEY" -> statusText.value = context.getString(R.string.AuthErrorCodes_InvalidAPIKey)
                                    "ERROR_USER_NOT_FOUND" -> statusText.value = context.getString(R.string.AuthErrorCodes_UserNotFound)
                                    else -> statusText.value = sendEmailResult
                                }
                            } ?: run {
                                statusText.value = "入力されたメールアドレス宛に確認用メールを送信しました。"
                            }
                        }
                    }
                    else
                        navController.navigate(SceneID.home)
                } ?: run {
                    statusText.value = context.getString(R.string.AuthErrorCodes_Etc)
                }
            }
        }
    }
}
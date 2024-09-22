package com.cistus.hunter.android.scenes.homeview.setting

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cistus.hunter.AppInfo
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.android.MainActivity
import com.cistus.hunter.android.R
import com.cistus.hunter.android.SceneID
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.FirebaseAuth
import com.cistus.hunter.android.scenes.ToS
import com.cistus.hunter.android.scenes.homeview.setting.account.UpdateEmailAddress
import com.cistus.hunter.android.scenes.homeview.setting.account.UpdatePassword

class Setting(private val shareData: MutableState<MainActivity.ShareData>,
              private val navController: NavController,
              private val showNotification: MutableState<Boolean>,
              private val screenSize: MutableState<UISize>): TabItem {
    override val label: String = "設定"
    override val icon: Int = R.drawable.gearshape_fill
    private val auth = FirebaseAuth()

    @Composable
    override fun Draw() {
        val accountList = ArrayList<UIDraw.ListItem>()
        val appInfoList = ArrayList<UIDraw.ListItem>()
        val etcList = ArrayList<UIDraw.ListItem>()
        val context = LocalContext.current
        val showSignOutDialog = rememberSaveable { mutableStateOf(false) }
        val showDeleteCacheDialog = rememberSaveable { mutableStateOf(false) }

        if (accountList.isEmpty()) {
            // Account
            accountList.add(UIDraw.ListItem("ユーザーID", content = auth.currentUser()?.uid ?: "Unknown"))
            accountList.add(UIDraw.ListItem("メールアドレス", content = auth.currentUser()?.email ?: "Unknown",
                navigateTo = { UpdateEmailAddress(it).Draw() }))
            accountList.add(UIDraw.ListItem("パスワード", navigateTo = { UpdatePassword(it).Draw() }))
            // AppInfo
            appInfoList.add(UIDraw.ListItem("バージョン", content = AppInfo.version))
            appInfoList.add(UIDraw.ListItem("ビルド", content = AppInfo.build))
            appInfoList.add(UIDraw.ListItem("利用規約", navigateTo = { ToS(LocalContext.current).Draw(it) }))
            // Etc
            etcList.add(UIDraw.ListItem("キャッシュ削除",
                onTapped = { showDeleteCacheDialog.value = true }))
            etcList.add(UIDraw.ListItem("サインアウト", textColor = Color.Red,
                onTapped = { showSignOutDialog.value = true }))
        }
        UIDraw.DrawBackGround {
            UIDraw.CustomColumn(style = "Top") {
                Box(modifier = Modifier.height((UIConfig.textlogoHeight + UIConfig.textlogoPadding).dp)) {
                    UIDraw.CustomRow(style = "CenterStart", modifier = Modifier.padding(start = 10.dp)) {
                        Image(painterResource(R.drawable.textlogo), "",
                            modifier = Modifier.height((UIConfig.textlogoHeight + UIConfig.textlogoPadding).dp)
                                .padding(vertical = UIConfig.textlogoPadding.dp))
                    }
                    UIDraw.CustomRow(style = "CenterEnd", modifier = Modifier.padding(end = 10.dp)) {
                        Box {
                            Image(painterResource(R.drawable.bell), "",
                                modifier = Modifier.clickable { showNotification.value = true })
                            Box(modifier = Modifier.alpha(if (shareData.value.unreadNotifications) 1f else 0f)
                                .width(10.dp).height(10.dp).clip(CircleShape).background(color = Color.Red)
                                .align(Alignment.TopEnd))
                        }
                    }
                }
                UIDraw.CustomColumn(style = "Top", spacing = 10.dp,
                    modifier = Modifier.verticalScroll(rememberScrollState())) {
                    UIDraw.CustomColumn(style = "TopStart", fillStyle = UIDraw.FILLSTYLE_MAXWIDTH,
                        modifier = Modifier.padding(start = 16.dp)) {
                        UIDraw.DrawText(label, fontSize = 32f, color = Color.Black, style = "Bold")
                    }
                    UIDraw.DrawList(screenSize, accountList.toList(), header = "アカウント").Draw()
                    UIDraw.DrawList(screenSize, appInfoList.toList(), header = "アプリ情報").Draw()
                    UIDraw.DrawList(screenSize, etcList.toList(), header = "その他").Draw()
                }
            }
        }
        UIDraw.DrawAlertDialog(showDialog = showDeleteCacheDialog, title = "キャッシュを削除",
            message = "キャッシュを削除しますか?", confirmText = "削除",
            confirmTextColor = Color.Red, confirm = { deleteCache(context) },
            dismissText = "キャンセル")
        UIDraw.DrawAlertDialog(showDialog = showSignOutDialog, title = "サインアウトしますか?",
            message = "一部のデータが失われる可能性があります。", confirmText = "サインアウト",
            confirmTextColor = Color.Red, confirm = { signOut() },
            dismissText = "キャンセル")
    }

    private fun signOut() {
        auth.signOut()
        navController.navigate(SceneID.boot)
    }
    private fun deleteCache(context: Context) {
        context.cacheDir.listFiles()?.let {
            for (file in it) {
                if (file.isDirectory)
                    file.deleteRecursively()
                else
                    file.delete()
            }
        }
    }
}

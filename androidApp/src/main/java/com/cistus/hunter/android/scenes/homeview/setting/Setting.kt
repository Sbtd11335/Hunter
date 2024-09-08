package com.cistus.hunter.android.scenes.homeview.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.android.R
import com.cistus.hunter.android.SceneID
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.FirebaseAuth

class Setting(private val navController: NavController,
              private val screenSize: MutableState<UISize>): TabItem {
    override val label: String = "設定"
    override val icon: Int = R.drawable.gearshape_fill
    private val auth = FirebaseAuth()

    @Composable
    override fun Draw() {
        val accountList = ArrayList<UIDraw.ListItem>()
        val etcList = ArrayList<UIDraw.ListItem>()
        val showSignOutDialog = rememberSaveable { mutableStateOf(false) }

        if (accountList.isEmpty()) {
            // Account
            accountList.add(UIDraw.ListItem("ユーザーID", content = auth.currentUser()?.uid ?: "Unknown"))
            accountList.add(UIDraw.ListItem("アカウント", content = auth.currentUser()?.email ?: "Unknown",
                navigateTo = {  }))
            accountList.add(UIDraw.ListItem("パスワード", navigateTo = {  }))
            // Etc
            etcList.add(UIDraw.ListItem("サインアウト", textColor = Color.Red,
                onTapped = { showSignOutDialog.value = true }))
        }
        UIDraw.DrawBackGround {
            UIDraw.CustomColumn(style = "Top") {
                Image(painterResource(R.drawable.textlogo), "",
                    modifier = Modifier.height((UIConfig.textlogoHeight + UIConfig.textlogoPadding).dp)
                        .padding(vertical = UIConfig.textlogoPadding.dp))
                UIDraw.CustomColumn(style = "Top", spacing = 10.dp,
                    modifier = Modifier.verticalScroll(rememberScrollState())) {
                    UIDraw.CustomColumn(style = "TopStart", fillStyle = UIDraw.FILLSTYLE_MAXWIDTH,
                        modifier = Modifier.padding(start = 16.dp)) {
                        UIDraw.DrawText(label, fontSize = 32f, color = Color.Black, style = "Bold")
                    }
                    UIDraw.DrawList(screenSize, accountList.toList(), header = "アカウント").Draw()
                    UIDraw.DrawList(screenSize, etcList.toList(), header = "その他").Draw()
                }
            }
        }
        UIDraw.DrawAlertDialog(showDialog = showSignOutDialog, title = "ログアウトしますか?",
            message = "一部のデータが失われる可能性があります。", confirmText = "ログアウト",
            confirmTextColor = Color.Red, confirm = { signOut() },
            dismissText = "キャンセル")
    }

    private fun signOut() {
        auth.signOut()
        navController.navigate(SceneID.boot)
    }

}

package com.cistus.hunter.android.scenes.homeview

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.cistus.hunter.MessageData
import com.cistus.hunter.UIConfig
import com.cistus.hunter.UISize
import com.cistus.hunter.android.MainActivity
import com.cistus.hunter.android.R
import com.cistus.hunter.android.TabItem
import com.cistus.hunter.android.ThemeColor
import com.cistus.hunter.android.UIDraw
import com.cistus.hunter.android.firebase.data3.FirebaseDatabaseData3
import java.time.Instant
import kotlin.math.pow

class Message(private val shareData: MutableState<MainActivity.ShareData>,
              private val screenSize: MutableState<UISize>,
              private val selectedTabIndex: MutableState<Int>,
              private val showNotification: MutableState<Boolean>): TabItem {
    override val label: String = "メッセージ"
    override val icon: Int = R.drawable.message_fill
    override val badge: Any = shareData

    @Composable
    override fun Draw() {
        val messageFrameMaxWidth = screenSize.value.width * 0.8
        val messageBoxSize = UISize(messageFrameMaxWidth.dp.value.toDouble(), 140.0)
        var currentMessageBoxSize by remember { mutableStateOf(IntSize(0, 0)) }
        val scrollState = rememberScrollState()
        val localDensity = LocalDensity.current
        val textFieldFocus = LocalFocusManager.current
        val context = LocalContext.current

        CompositionLocalProvider(LocalRippleTheme provides UIDraw.NoRipple()) {
            UIDraw.DrawBackGround(listOf(Color.Transparent, Color.Transparent)) {
                UIDraw.CustomColumn(style = "Top", fillStyle = UIDraw.FILLSTYLE_MAXWIDTH, onTapped = { textFieldFocus.clearFocus() }) {
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
                    Box(modifier = Modifier.padding(bottom = UIDraw.toDp(currentMessageBoxSize.height) + 10.dp)) {
                        UIDraw.CustomColumn(style = "Top", spacing = 10.dp, fillStyle = UIDraw.FILLSTYLE_NONE,
                            modifier = Modifier.verticalScroll(scrollState)) {
                            if (shareData.value.messages.isEmpty())
                                UIDraw.DrawText("メッセージはありません。", color = Color.Black)
                            else
                                for (messageData in shareData.value.messages)
                                    DrawMessage(messageData, messageFrameMaxWidth)
                        }
                    }
                }
                UIDraw.CustomColumn(style = "Bottom") {
                    DrawMessageBox(messageBoxSize, "ここにメッセージを入力", 64) {
                        currentMessageBoxSize = it
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            scrollState.scrollTo(scrollState.maxValue)
        }
        LaunchedEffect(currentMessageBoxSize) {
            with(localDensity) {
                scrollState.scrollBy(((currentMessageBoxSize.height).toDp() + 10.dp).toPx())
            }
        }
        LaunchedEffect(shareData.value.unreadMessages) {
            if (selectedTabIndex.value == 2) {
                val data3 = FirebaseDatabaseData3(context)
                shareData.value.unreadMessages = 0
                data3.data1_edit(shareData)
            }
        }
    }
    @Composable
    private fun DrawMessage(messageData: MessageData, size: Double) {
        val backColor = if (!messageData.fromUser) Color.White else Color.ThemeColor
        val foreColor = if (!messageData.fromUser) Color.Black else Color.White
        val style = if (!messageData.fromUser) "BottomStart" else "BottomEnd"
        val modifier = if (!messageData.fromUser) Modifier.padding(start = 16.dp) else Modifier.padding(end = 16.dp)
        val dateModifier = if (!messageData.fromUser) Modifier.padding(start = 5.dp) else Modifier.padding(end = 5.dp)
        val draw = @Composable {
            val md = messageData.toFormattedTime("MM/dd")
            val hm = messageData.toFormattedTime("HH:mm")
            Column(modifier = dateModifier) {
                UIDraw.DrawText(md, color = Color.Black, fontSize = 12f)
                UIDraw.DrawText(hm, color = Color.Black, fontSize = 12f)
            }
        }
        UIDraw.CustomRow(style = style, modifier = modifier, fillStyle = UIDraw.FILLSTYLE_MAXWIDTH) {
            if (messageData.fromUser)
                draw()
            Box(modifier = Modifier.background(color = backColor, shape = RoundedCornerShape(15.dp))) {
                UIDraw.DrawText(messageData.message, color = foreColor,
                    modifier = Modifier.padding(10.dp).widthIn(max = size.dp))
            }
            if (!messageData.fromUser)
                draw()
        }
    }
    @Composable
    private fun DrawMessageBox(size: UISize, label: String, maxChars: Int? = null,
                               callback: (IntSize) -> Unit) {
        val message = rememberSaveable { mutableStateOf("") }
        val textFieldFocus = LocalFocusManager.current
        val context = LocalContext.current

        UIDraw.CustomRow(style = "Bottom", fillStyle = UIDraw.FILLSTYLE_MAXWIDTH, spacing = 10.dp,
            modifier = Modifier.onGloballyPositioned {
                callback(it.size)
            }) {
            Box(modifier = Modifier.background(color = Color.White, shape = RoundedCornerShape(15.dp))) {
                BasicTextField(value = message.value, onValueChange = {
                        message.value = it
                        if (maxChars != null &&message.value.length > maxChars)
                            message.value = message.value.substring(0..<maxChars)
                    },
                    textStyle = TextStyle(Color.Black, fontSize = TextUnit(UIDraw.toSp(16.dp).value, TextUnitType.Sp)),
                    modifier = Modifier.width(size.width.dp).heightIn(max = size.height.dp)
                        .padding(10.dp),
                    decorationBox = {
                        it()
                        if (message.value.isEmpty())
                            UIDraw.DrawText(label, color = Color.Gray)
                    })
            }
            Box(modifier = Modifier.width(40.dp).height(40.dp)) {
                Image(painterResource(R.drawable.paperplane_fill), "",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.align(Alignment.Center).clickable {
                        textFieldFocus.clearFocus()
                        sendMessage(context, message)
                    })
            }
        }
    }
    private fun sendMessage(context: Context, message: MutableState<String>) {
        if (message.value.isEmpty() || message.value.isBlank())
            return
        val now = Instant.now().toEpochMilli() * 10.0.pow(6)
        val messageData = MessageData("%.0f".format(now), message.value, true)
        val data3 = FirebaseDatabaseData3(context)
        data3.setData1(messageData) {}
        message.value = ""
    }
}
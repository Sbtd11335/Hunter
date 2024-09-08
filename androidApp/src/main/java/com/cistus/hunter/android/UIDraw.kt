package com.cistus.hunter.android

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cistus.hunter.BuildConfig
import com.cistus.hunter.Screen

class UIDraw {

    class NoRipple: RippleTheme {
        @Composable
        override fun defaultColor(): Color = Color.Transparent
        @Composable
        override fun rippleAlpha(): RippleAlpha = RippleAlpha(0f, 0f, 0f, 0f)
    }

    companion object {
        val FILLSTYLE_NONE: UByte = 0u
        val FILLSTYLE_MAXSIZE: UByte = 1u
        val FILLSTYLE_MAXWIDTH: UByte = 2u
        val FILLSTYLE_MAXHEIGHT: UByte = 3u

        @Composable
        fun toDp(px: Double): Dp {
            with(LocalDensity.current) {
                return px.toFloat().toDp()
            }
        }
        @Composable
        fun toDp(px: Float): Dp {
            with(LocalDensity.current) {
                return px.toDp()
            }
        }
        @Composable
        fun toDp(px: Int): Dp {
            with(LocalDensity.current) {
                return px.toDp()
            }
        }
        @Composable
        fun toPx(dp: Dp): Float {
            with(LocalDensity.current) {
                return dp.toPx()
            }
        }
        @Composable
        fun toSp(dp: Dp): TextUnit {
            with(LocalDensity.current) {
                return dp.toSp()
            }
        }

        @Composable
        fun CenterColumn(modifier: Modifier = Modifier, hide: Boolean = false, spacing: Dp? = null,
                         fillStyle: UByte = FILLSTYLE_MAXSIZE, onTapped: (() -> Unit)? = null, content: @Composable () -> Unit) {
            val alpha = if (!hide) 1f else 0f
            var fill: Modifier = Modifier
            var tap: Modifier = Modifier
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            when(fillStyle) {
                FILLSTYLE_NONE -> fill = Modifier
                FILLSTYLE_MAXSIZE -> fill = Modifier.fillMaxSize()
                FILLSTYLE_MAXWIDTH -> fill = Modifier.fillMaxWidth()
                FILLSTYLE_MAXHEIGHT -> fill = Modifier.fillMaxHeight()
            }
            Column(modifier = Modifier
                .then(fill)
                .then(modifier)
                .then(tap)
                .alpha(alpha),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                if (spacing == null)
                    content()
                else
                    Column(verticalArrangement = Arrangement.spacedBy(spacing),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        content()
                    }
            }
        }
        @Composable
        fun CenterRow(modifier: Modifier = Modifier, hide: Boolean = false, spacing: Dp? = null,
                      fillStyle: UByte = FILLSTYLE_MAXSIZE, onTapped: (() -> Unit)? = null, content: @Composable () -> Unit) {
            val alpha = if (!hide) 1f else 0f
            var fill: Modifier = Modifier
            var tap: Modifier = Modifier
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            when(fillStyle) {
                FILLSTYLE_NONE -> fill = Modifier
                FILLSTYLE_MAXSIZE -> fill = Modifier.fillMaxSize()
                FILLSTYLE_MAXWIDTH -> fill = Modifier.fillMaxWidth()
                FILLSTYLE_MAXHEIGHT -> fill = Modifier.fillMaxHeight()
            }
            Row(modifier = Modifier
                .then(fill)
                .then(modifier)
                .then(tap)
                .alpha(alpha),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                if (spacing == null)
                    content()
                else
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing)) {
                        content()
                    }
            }
        }
        @Composable
        fun CustomColumn(modifier: Modifier = Modifier, style: String = "Center",
                         hide: Boolean = false, spacing: Dp? = null, fillStyle: UByte = FILLSTYLE_MAXSIZE,
                         onTapped: (() -> Unit)? = null,  content: @Composable () -> Unit) {
            val verticalArrangement: Arrangement.Vertical
            val horizontalAlignment: Alignment.Horizontal
            var fill: Modifier = Modifier
            val alpha = if (!hide) 1f else 0f
            var tap: Modifier = Modifier
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            when(style) {
                "TopStart" -> {
                    verticalArrangement = Arrangement.Top
                    horizontalAlignment = Alignment.Start
                }
                "Top" -> {
                    verticalArrangement = Arrangement.Top
                    horizontalAlignment = Alignment.CenterHorizontally
                }
                "TopEnd" -> {
                    verticalArrangement = Arrangement.Top
                    horizontalAlignment = Alignment.End
                }
                "CenterStart" -> {
                    verticalArrangement = Arrangement.Center
                    horizontalAlignment = Alignment.Start
                }
                "Center" -> {
                    verticalArrangement = Arrangement.Center
                    horizontalAlignment = Alignment.CenterHorizontally
                }
                "CenterEnd" -> {
                    verticalArrangement = Arrangement.Center
                    horizontalAlignment = Alignment.End
                }
                "BottomStart" -> {
                    verticalArrangement = Arrangement.Bottom
                    horizontalAlignment = Alignment.Start
                }
                "Bottom" -> {
                    verticalArrangement = Arrangement.Bottom
                    horizontalAlignment = Alignment.CenterHorizontally
                }
                "BottomEnd" -> {
                    verticalArrangement = Arrangement.Bottom
                    horizontalAlignment = Alignment.End
                }
                else -> {
                    verticalArrangement = Arrangement.Center
                    horizontalAlignment = Alignment.CenterHorizontally
                }
            }
            when(fillStyle) {
                FILLSTYLE_NONE -> fill = Modifier
                FILLSTYLE_MAXSIZE -> fill = Modifier.fillMaxSize()
                FILLSTYLE_MAXWIDTH -> fill = Modifier.fillMaxWidth()
                FILLSTYLE_MAXHEIGHT -> fill = Modifier.fillMaxHeight()
            }
            Column(modifier = Modifier
                .then(fill)
                .then(modifier)
                .then(tap)
                .alpha(alpha),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment) {
                if (spacing == null)
                    content()
                else
                    Column(verticalArrangement = Arrangement.spacedBy(spacing),
                        horizontalAlignment = horizontalAlignment) {
                        content()
                    }
            }
        }
        @Composable
        fun CustomRow(modifier: Modifier = Modifier, style: String = "Center",
                      hide: Boolean = false, spacing: Dp? = null, fillStyle: UByte = FILLSTYLE_MAXSIZE,
                      onTapped: (() -> Unit)? = null, content: @Composable () -> Unit) {
            val verticalAlignment: Alignment.Vertical
            val horizontalArrangement: Arrangement.Horizontal
            var fill: Modifier = Modifier
            val alpha = if (!hide) 1f else 0f
            var tap: Modifier = Modifier
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            when(style) {
                "TopStart" -> {
                    verticalAlignment = Alignment.Top
                    horizontalArrangement = Arrangement.Start
                }
                "Top" -> {
                    verticalAlignment = Alignment.Top
                    horizontalArrangement = Arrangement.Center
                }
                "TopEnd" -> {
                    verticalAlignment = Alignment.Top
                    horizontalArrangement = Arrangement.End
                }
                "CenterStart" -> {
                    verticalAlignment = Alignment.CenterVertically
                    horizontalArrangement = Arrangement.Start
                }
                "Center" -> {
                    verticalAlignment = Alignment.CenterVertically
                    horizontalArrangement = Arrangement.Center
                }
                "CenterEnd" -> {
                    verticalAlignment = Alignment.CenterVertically
                    horizontalArrangement = Arrangement.End
                }
                "BottomStart" -> {
                    verticalAlignment = Alignment.Bottom
                    horizontalArrangement = Arrangement.Start
                }
                "Bottom" -> {
                    verticalAlignment = Alignment.Bottom
                    horizontalArrangement = Arrangement.Center
                }
                "BottomEnd" -> {
                    verticalAlignment = Alignment.Bottom
                    horizontalArrangement = Arrangement.End
                }
                else -> {
                    verticalAlignment = Alignment.CenterVertically
                    horizontalArrangement = Arrangement.Center
                }
            }
            when(fillStyle) {
                FILLSTYLE_NONE -> fill = Modifier
                FILLSTYLE_MAXSIZE -> fill = Modifier.fillMaxSize()
                FILLSTYLE_MAXWIDTH -> fill = Modifier.fillMaxWidth()
                FILLSTYLE_MAXHEIGHT -> fill = Modifier.fillMaxHeight()
            }
            Row(modifier = Modifier
                .then(fill)
                .then(modifier)
                .then(tap)
                .alpha(alpha),
                verticalAlignment = verticalAlignment,
                horizontalArrangement = horizontalArrangement) {
                if (spacing == null)
                    content()
                else
                    Row(verticalAlignment = verticalAlignment,
                        horizontalArrangement = horizontalArrangement) {
                        content()
                    }
            }
        }
        @Composable
        fun DrawDialog(showDialog: MutableState<Boolean>? = null, closeText: String = "",
                       fullScreen: Boolean = false, closeTextColor: Color = Color.TextButton,
                       content: @Composable (Float) -> Unit) {
            val drawDialog = showDialog ?: rememberSaveable { mutableStateOf(true) }
            var closeTexHeight by remember { mutableFloatStateOf(0f) }
            if (drawDialog.value)
                Dialog(onDismissRequest = { drawDialog.value = false },
                    properties = DialogProperties(usePlatformDefaultWidth = !fullScreen)) {
                    Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background) {
                        content(closeTexHeight)
                        if (closeText.isNotEmpty())
                            CustomColumn(style = "TopStart",
                                modifier = Modifier.padding(20.dp)) {
                                DrawText(closeText, color = closeTextColor, modifier = Modifier.onGloballyPositioned {
                                    closeTexHeight = it.size.height.toFloat() * 3
                                }) {
                                    drawDialog.value = false
                                }
                            }
                    }
                }
        }
        @Composable
        fun DrawVersion() {
            val appName = BuildConfig.APP_NAME
            val version = BuildConfig.VERSION_NAME
            val build = BuildConfig.VERSION_CODE.toString()
            CustomColumn(style = "Bottom", modifier = Modifier.padding(20.dp)) {
                DrawText("%s Version %s-%s".format(appName, version, build), color = Color.Black)
                DrawText("Developed by Cistus", color = Color.Black, style = "Bold")
            }
        }
        @Composable
        fun DrawBackGround(colors: List<Color>? = null, onTapped: (() -> Unit)? = null,
                           content: @Composable (Size) -> Unit = {}) {
            val drawColors = colors ?: listOf(Color.AppColor1, Color.AppColor2)
            var size by remember { mutableStateOf(Size(0f, 0f)) }
            var tap: Modifier = Modifier
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = drawColors, start = Offset.Zero,
                        end = Offset(toPx(Screen.width.dp), toPx(Screen.height.dp))
                    )
                )
                .then(tap)
                .onGloballyPositioned {
                    size = Size(it.size.width.toFloat(), it.size.height.toFloat())
                })
            {
                content(size)
            }
        }
        @Composable
        fun DrawText(text: String, color: Color = Color.Primary, fontSize: Float = 17f, textAlign: TextAlign? = null,
                     fontFamily: FontFamily = FontFamily.Default, style: String = "Default", emptyDraw: Boolean = true,
                     @SuppressLint("ModifierParameter") modifier: Modifier = Modifier, onTapped: (() -> Unit)? = null) {
            if (!emptyDraw && text.isEmpty())
                return
            val fontWeight: FontWeight
            val fontStyle: FontStyle
            var tap: Modifier = Modifier
            when(style) {
                "Bold" -> {
                    fontWeight = FontWeight.Bold
                    fontStyle = FontStyle.Normal
                }
                "Italic" -> {
                    fontWeight = FontWeight.Normal
                    fontStyle = FontStyle.Italic
                }
                "ItalicBold" -> {
                    fontWeight = FontWeight.Bold
                    fontStyle = FontStyle.Italic
                }
                else -> {
                    fontWeight = FontWeight.Normal
                    fontStyle = FontStyle.Normal
                }
            }
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            Text(text, color = color, fontWeight = fontWeight, fontFamily = fontFamily, textAlign = textAlign,
                fontStyle = fontStyle, fontSize = TextUnit(toSp(fontSize.dp).value, TextUnitType.Sp),
                modifier = modifier.then(tap))
        }
        @Composable
        fun DrawImage(image: Int, scale: Float = 1f, bigger: Boolean? = null,
                      @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
                      onTapped: (() -> Unit)? = null) {
            var tap: Modifier = Modifier
            val width: Float = if (bigger == null)
                Screen.smallerSize.toFloat() * scale
            else
                if (!bigger)
                    Screen.smallerSize.toFloat() * scale
                else
                    Screen.biggerSize.toFloat() * scale
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            Image(painterResource(image), "",
                modifier = Modifier
                    .width(width.dp)
                    .then(modifier)
                    .then(tap))
        }
        @Composable
        fun DrawProgress(scale: Float = 1f, color: Color = Color.Primary, style: String = "Circular",
                         @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
                         onTapped: (() -> Unit)? = null) {
            var tap: Modifier = Modifier
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            when(style) {
                "Circular" -> CircularProgressIndicator(color = color, modifier = modifier
                    .scale(scale)
                    .then(tap))
                "Linear" -> LinearProgressIndicator(color = color, modifier = modifier
                    .scale(scale)
                    .then(tap))
                else -> CircularProgressIndicator(color = color, modifier = modifier
                    .scale(scale)
                    .then(tap))
            }
        }
        @Composable
        fun DrawRCFrame(size: DpSize = DpSize(60.dp, 60.dp), color: Color = Color.Primary, radius: Dp = 15.dp,
                        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
                        onTapped: (() -> Unit)? = null, content: @Composable () -> Unit = {}) {
            var tap: Modifier = Modifier
            if (onTapped != null)
                tap = Modifier.clickable { onTapped() }
            Box(modifier = Modifier
                .width(size.width)
                .height(size.height)
                .then(modifier)
                .clip(RoundedCornerShape(radius))
                .background(color)
                .then(tap)) {
                content()
            }
        }
        @Composable
        fun DrawTextField(text: MutableState<String>, size: DpSize = DpSize(200.dp, 40.dp), radius: Dp = 15.dp,
                          label: String = "", backgroundColor: Color = Color.White, foregroundColor: Color = Color.Black,
                          labelColor: Color = Color.Gray, textPadding: Dp = 10.dp,
                          singleLine: Boolean = false,
                          @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {
            DrawRCFrame(size = size, color = backgroundColor, radius = radius, modifier = modifier, content = {
                BasicTextField(value = text.value, onValueChange = { text.value = it },
                    textStyle = TextStyle(foregroundColor), modifier = Modifier.fillMaxSize().padding(textPadding),
                    singleLine = singleLine, decorationBox = {
                        if (text.value.isEmpty())
                            DrawText(label, color = labelColor)
                        it()
                    })
            })
        }
        @Composable
        fun DrawSecureField(text: MutableState<String>, hide: MutableState<Boolean>,
                            size: DpSize = DpSize(200.dp, 40.dp), radius: Dp = 15.dp, label: String = "",
                            backgroundColor: Color = Color.White, foregroundColor: Color = Color.Black,
                            labelColor: Color = Color.Gray, textPadding: Dp = 10.dp, singleLine: Boolean = false,
                          @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {
            DrawRCFrame(size = size, color = backgroundColor, radius = radius, modifier = modifier, content = {
                CustomRow(style = "CenterStart") {
                    BasicTextField(value = text.value, onValueChange = { text.value = it },
                        textStyle = TextStyle(foregroundColor), singleLine = singleLine,
                        modifier = Modifier
                            .width(size.width - size.height)
                            .fillMaxHeight()
                            .padding(textPadding),
                        visualTransformation = if (!hide.value) VisualTransformation.None else PasswordVisualTransformation(),
                        decorationBox = {
                            if (text.value.isEmpty())
                                DrawText(label, color = labelColor)
                            it()
                        })
                    val img = if (!hide.value) R.drawable.eye_slash else R.drawable.eye
                    Image(painterResource(img), "", modifier = Modifier.padding(8.dp).clickable {
                        hide.value = !hide.value
                    })
                }
            })
        }
    }
}
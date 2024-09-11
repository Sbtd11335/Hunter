package com.cistus.hunter.android.scenes

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cistus.hunter.Directory
import com.cistus.hunter.LocalDataConfig
import com.cistus.hunter.TextFile
import com.cistus.hunter.android.Primary2
import com.cistus.hunter.android.R
import com.cistus.hunter.android.UIDraw

class ToS(context: Context) {

    init {
        val tos = LocalDataConfig.ToS(context)
        val tosDirectoryPath = tos.directoryPath
        val tosDirectory = Directory(tosDirectoryPath)
        val tosDataFilePath = tos.dateFilePath
        val tosDataFile = TextFile(tosDataFilePath)
        if (!tosDirectory.isExists())
            tosDirectory.create()
        tosDataFile.write(context.getString(R.string.ToS_Date))
    }

    @Composable
    fun Draw(padding: Float) {
        UIDraw.CenterColumn {
            UIDraw.CenterColumn(fillStyle = UIDraw.FILLSTYLE_MAXWIDTH, modifier = Modifier.height(UIDraw.toDp(padding))) {
                UIDraw.DrawText("利用規約", color = Color.Primary2)
            }
            Box(modifier = Modifier
                .verticalScroll(rememberScrollState()).background(MaterialTheme.colorScheme.background)) {
                ProgressLines(stringResource(R.string.ToS))
            }
        }
    }
    @Composable
    private fun ProgressLines(contents: String) {
        UIDraw.CustomColumn(style = "TopStart", spacing = 3.dp) {
            for (lines in contents.lines()) {
                if (lines.indexOfFirst { it == '*' } >= 0) {
                    val first = lines.indexOfFirst { it == '*' } + 1
                    val last = lines.indexOfLast { it == '*' }
                    val display = lines.substring(first..<last)
                    UIDraw.DrawText(display, color = Color.Primary2, fontSize = 24f,
                        style = "Bold")
                }
                else if (lines.indexOfFirst { it == '|' } >= 0) {
                    val first = lines.indexOfFirst { it == '|' } + 1
                    val last = lines.indexOfLast { it == '|' }
                    val item = lines.substring(first..<last)
                    val content = lines.substring(last + 1)
                    if (item.isEmpty())
                        UIDraw.DrawText("　・${content}", color = Color.Primary2)
                    else
                        UIDraw.DrawText("　${item}. $content", color = Color.Primary2,
                            style = "Bold")
                }
                else
                    UIDraw.DrawText(lines, color = Color.Primary2)
            }
        }
    }

    companion object {
        @Composable
        fun checkUpdate(): Boolean {
            val context = LocalContext.current
            val tos = LocalDataConfig.ToS(context)
            val tosDateFilePath = tos.dateFilePath
            val tosDateFile = TextFile(tosDateFilePath)
            tosDateFile.read()?.let { date ->
                return date != stringResource(R.string.ToS_Date)
            }
            return true
        }
    }
}
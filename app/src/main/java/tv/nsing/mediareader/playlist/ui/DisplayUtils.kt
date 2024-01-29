package tv.nsing.mediareader.playlist.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt



object DisplayUtils {
    @Composable
    fun screenWidthPx(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp.dp.dpToPx()
    }

    @Composable
    fun screenHeightPx(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp.dp.dpToPx()
    }

    @Composable
    fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.roundToPx() }


    @Composable
    fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

    @Composable
    fun KeepScreenOn() {
        val context = LocalContext.current
        DisposableEffect(Unit) {
            val window = context.findActivity()?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            onDispose {
                window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}



fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
package maxeem.america.devbytes.ui

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import maxeem.america.devbytes.R
import maxeem.america.devbytes.app
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.configuration
import org.jetbrains.anko.info

object UI : AnkoLogger {

    val inNightMode get() = when (val mode = getDefaultNightMode()) {
        MODE_NIGHT_UNSPECIFIED
             -> isAppUiNightMode
        else -> mode == MODE_NIGHT_YES
    }

    fun changeTheme(res: Resources) {
        info("""UI call applyNight
                res isNight: ${res.getBoolean(R.bool.isNight)}
                app conf ui mode: ${app.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK}
                is app ui night mode: $isAppUiNightMode
                AppCompatDelegate.getDefaultNightMode(): ${AppCompatDelegate.getDefaultNightMode()}
                inNightMode: $inNightMode
            """)
        val newMode = if (inNightMode) MODE_NIGHT_NO else MODE_NIGHT_YES
        info(" newMode: $newMode")
        setDefaultNightMode(newMode)
    }

    private val isAppUiNightMode get()
            = app.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

//        val newMode = when(AppCompatDelegate.getDefaultNightMode()) {
//             AppCompatDelegate.MODE_NIGHT_UNSPECIFIED ->
//                if (isAppUiNightMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
//            AppCompatDelegate.MODE_NIGHT_YES ->
//                AppCompatDelegate.MODE_NIGHT_NO
//            else ->
//                AppCompatDelegate.MODE_NIGHT_YES
//        }
}


package maxeem.america.devbytes.util

import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import maxeem.america.devbytes.app
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.info
import java.util.*

object Prefs : AnkoLogger {

    private const val KEY_LAST_SYNC = "last_sync"
    private const val KEY_SYNC_COUNT = "sync_count"

    private val prefs = app.defaultSharedPreferences

    val hasSync get() = syncCount > 0

    val lastSync get() = prefs.getLong(KEY_LAST_SYNC, -1)
    val syncCount get() = prefs.getInt(KEY_SYNC_COUNT, 0)

    val syncEvent = MutableLiveData(prefs.getLong(KEY_LAST_SYNC, -1))

    fun init() {
        syncEvent.observeForever { time ->
            if (time == prefs.getLong(KEY_LAST_SYNC, -1)) return@observeForever
            info(" syncData observeForever, when: $time, on $thread")
            prefs.edit(true) {
                putLong(KEY_LAST_SYNC, time)
                putInt(KEY_SYNC_COUNT, syncCount + 1)
            }
            dumpState()
        }
    }

    init { dumpState() }

    private fun dumpState() {
        info(" syncCount: $syncCount \n" +
                " lastSync: ${Date(lastSync)}\n" +
                " nextSync: ${Date(lastSync+ DEVBYTES_SYNC_INTERVAL_TIME_UNIT.toMillis(DEVBYTES_SYNC_INTERVAL_VALUE))} - we await =)")
    }

}

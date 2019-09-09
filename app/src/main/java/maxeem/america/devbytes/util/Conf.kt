package maxeem.america.devbytes.util

import maxeem.america.devbytes.BuildConfig.DEBUG
import java.util.concurrent.TimeUnit.DAYS
import java.util.concurrent.TimeUnit.HOURS

/**
 * Conf & Const values
 */

object Conf {
      object Database {
            const val NAME = "devbytes-db"
      }
      object DevBytes {
            const val BASE_URL = "https://devbytes.udacity.com" // "https://android-kotlin-fun-mars-server.appspot.com"
            const val PLAYLIST = "devbytes.json" // "devbytes"
                  val SYNC_INTERVAL  = if (DEBUG) 6L    else 3
                  val SYNC_TIME_UNIT = if (DEBUG) HOURS else DAYS
      }
}



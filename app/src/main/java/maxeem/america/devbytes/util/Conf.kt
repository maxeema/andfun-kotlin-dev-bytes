package maxeem.america.devbytes.util

import maxeem.america.devbytes.BuildConfig.DEBUG
import java.util.concurrent.TimeUnit.DAYS
import java.util.concurrent.TimeUnit.MINUTES

/**
 * Conf & Const values
 */

const val DATABASE_NAME = "devbytes-db"

const val DEVBYTES_BASE_URL = "https://devbytes.udacity.com/"
      val DEVBYTES_SYNC_INTERVAL_VALUE     = if (DEBUG) 45L else 3
      val DEVBYTES_SYNC_INTERVAL_TIME_UNIT = if (DEBUG) MINUTES else DAYS



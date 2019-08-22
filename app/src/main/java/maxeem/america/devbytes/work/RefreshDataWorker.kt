/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package maxeem.america.devbytes.work

import android.content.Context
import android.text.format.DateUtils
import androidx.work.*
import maxeem.america.devbytes.database.DevBytesDatabase
import maxeem.america.devbytes.repository.VideosRepository
import maxeem.america.devbytes.util.thread
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit

class RefreshDataWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params), AnkoLogger {

    override suspend fun doWork() =
        runCatching {
            info("doWork - begin, call refreshVideos from Worker, on $thread")
            VideosRepository(DevBytesDatabase.instance).refreshVideos()
            info("doWork - success")
            Result.success()
        }.getOrElse { err ->
            info("doWork - error", err)
            Result.retry()
        }

    companion object RDWorkerCompanion : AnkoLogger {

        private const val WORK_NAME_ONETIME = "Refresh-Data-Work-onetime"
//        private const val WORK_NAME_REPEATING = "Refresh-Data-Work-repeating"

        fun watchMe() {
            WorkManager.getInstance().getWorkInfosForUniqueWorkLiveData(WORK_NAME_ONETIME).observeForever { list ->
                info("work infos: $list")
            }
        }

        private val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
//                .setRequiresDeviceIdle(true) // works strange and a job doesn't start with this constraint almost always
                .build()

        fun setDelayedWork(duration: Long, timeUnit: TimeUnit) {
            info("setDelayedWork(), REPLACE current, with timeout: ${DateUtils.formatElapsedTime(timeUnit.toMillis(duration)/1000)}, on: $thread")
            val oneTimeRequest = OneTimeWorkRequestBuilder<RefreshDataWorker>()
                    .setInitialDelay(duration, timeUnit)
                    .setConstraints(constraints)
                    .build()
            WorkManager.getInstance().enqueueUniqueWork(
                    WORK_NAME_ONETIME,
                    ExistingWorkPolicy.REPLACE,
                    oneTimeRequest)
        }

        // In our App we use simple logic of fetching data from server
        //  after last success try + DEVBYTES_SYNC_INTERVAL_TIME_UNIT.toMillis(DEVBYTES_SYNC_INTERVAL_VALUE)
        //   but period work can't have first delay and even if we just updated, it could do the same at the same time
//        fun setRecurringWork(duration: Long, timeUnit: TimeUnit) {
//            val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(duration, timeUnit)
//                    .setConstraints(constraints)
//                    .build()
//            WorkManager.getInstance().enqueueUniquePeriodicWork(
//                    WORK_NAME_REPEATING,
//                    ExistingPeriodicWorkPolicy.KEEP,
//                    repeatingRequest)
//        }
    }

}

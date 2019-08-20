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

package maxeem.america.devbyteviewer

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import maxeem.america.devbyteviewer.work.RefreshDataWorker
import java.util.concurrent.TimeUnit

val app = DevByteApp.instance

class DevByteApp : Application() {

    companion object {
        private lateinit var _instance: DevByteApp
        val instance get() = _instance
    }

    init {
        _instance = this
    }

    private val appScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() { super.onCreate()
        delayedInit()
    }
    private fun delayedInit() {
        appScope.launch {
            setRecurringWork()
        }
    }
    private fun setRecurringWork() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }.build()
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }

}

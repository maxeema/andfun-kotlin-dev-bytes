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

package maxeem.america.devbytes

import android.app.Application
import android.os.Handler
import maxeem.america.devbytes.database.DevBytesDatabaseImpl
import maxeem.america.devbytes.repository.VideosRepositoryImpl
import maxeem.america.devbytes.util.Prefs
import maxeem.america.devbytes.util.hash
import maxeem.america.devbytes.util.pid
import maxeem.america.devbytes.util.timeMillis
import maxeem.america.devbytes.work.RefreshDataWorker
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

val app = DevBytesApp.instance

private val appModule = module {
    single { DevBytesDatabaseImpl.instance }
    single { VideosRepositoryImpl.instance }
}

class DevBytesApp : Application(), AnkoLogger {

    companion object {
        private lateinit var _instance: DevBytesApp
        val instance get() = _instance
    }

    init {
        _instance = this
    }

    override fun onCreate() { super.onCreate()
        info("$pid $hash $timeMillis onCreate()")
        startKoin {
            androidLogger(); androidContext(this@DevBytesApp)
            modules(appModule)
        }
        Prefs.init()
        app.handler.postDelayed(::delayedInit, 2000)
    }

    private fun delayedInit() {
        RefreshDataWorker.watchMe()
    }

}

val DevBytesApp.handler by lazy { Handler(app.mainLooper) }
val DevBytesApp.packageInfo
    get() = packageManager.getPackageInfo(packageName, 0)

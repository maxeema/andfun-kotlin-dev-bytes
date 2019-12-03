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

package maxeem.america.devbytes.repository

import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import maxeem.america.devbytes.database.DevBytesDatabase
import maxeem.america.devbytes.database.asDomainModel
import maxeem.america.devbytes.network.DevBytesService
import maxeem.america.devbytes.network.asDatabaseModel
import maxeem.america.devbytes.util.Conf
import maxeem.america.devbytes.util.Prefs
import maxeem.america.devbytes.util.pid
import maxeem.america.devbytes.util.thread
import maxeem.america.devbytes.work.RefreshDataWorker
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.koin.core.KoinComponent
import org.koin.core.inject

class VideosRepositoryImpl private constructor() : VideosRepository, AnkoLogger, KoinComponent {

    companion object {
        val instance : VideosRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            VideosRepositoryImpl()
        }
    }

    private val net : DevBytesService by inject()
    private val db : DevBytesDatabase by inject()
    private val mutex = Mutex()

    override
    val videos = db.videosDao.getAll().map { it.asDomainModel() }

    override
    suspend fun refreshVideos(byWhom: String) {
        info("$pid refresh videos by '$byWhom', mutex is ${if (mutex.isLocked) "locked" else "unlocked" }, on $thread")
        mutex.withLock {
            info("  mutex lock's acquired by '$byWhom' on $thread")
            refreshVideosImpl()
        }
    }
    private suspend fun refreshVideosImpl() {
        val playlist = net.getPlaylistAsync().await()
        val distinct = withContext(Dispatchers.Default) { playlist.asDatabaseModel().distinctBy { it.url } }
        val ids = db.videosDao.insertAll(*distinct.toTypedArray())
        Prefs.syncEvent.postValue(System.currentTimeMillis())
        RefreshDataWorker.setDelayedWork(Conf.DevBytes.SYNC_INTERVAL, Conf.DevBytes.SYNC_TIME_UNIT)
        info(" got size: ${playlist.videos.size}," +
                " distinct size: ${distinct.size}, on $thread" +
                    "\n $db ${db.videosDao} \n inserted ids: $ids")
    }

}

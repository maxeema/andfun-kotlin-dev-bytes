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

import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import maxeem.america.devbytes.database.DevBytesDatabase
import maxeem.america.devbytes.database.asDomainModel
import maxeem.america.devbytes.network.Network
import maxeem.america.devbytes.network.asDatabaseModel
import maxeem.america.devbytes.util.DEVBYTES_SYNC_INTERVAL_TIME_UNIT
import maxeem.america.devbytes.util.DEVBYTES_SYNC_INTERVAL_VALUE
import maxeem.america.devbytes.util.Prefs
import maxeem.america.devbytes.util.thread
import maxeem.america.devbytes.work.RefreshDataWorker
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class VideosRepository(private val db : DevBytesDatabase) : AnkoLogger {

    val videos = Transformations.map(db.videosDao.getAll()) { it.asDomainModel() }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = Network.devbytes.getPlaylistAsync().await()
            val distinct = playlist.asDatabaseModel().distinctBy { it.url }
            db.videosDao.insertAll(*distinct.toTypedArray())
            info("refresh videos, got size: ${playlist.videos.size}, distinct size: ${distinct.size}, on $thread")
            Prefs.syncEvent.postValue(System.currentTimeMillis())
            RefreshDataWorker.setDelayedWork(DEVBYTES_SYNC_INTERVAL_VALUE, DEVBYTES_SYNC_INTERVAL_TIME_UNIT)
        }
    }

}

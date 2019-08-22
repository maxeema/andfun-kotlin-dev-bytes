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

package maxeem.america.devbytes.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import maxeem.america.devbytes.database.DevBytesDatabase
import maxeem.america.devbytes.network.NetworkApiStatus
import maxeem.america.devbytes.repository.VideosRepository
import maxeem.america.devbytes.util.Prefs
import maxeem.america.devbytes.util.thread
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class ListingViewModel : ViewModel(), AnkoLogger {

    private val repo = VideosRepository(DevBytesDatabase.instance)

    val videos = repo.videos
    val hasData = videos.map { it.isNotEmpty() }

    private val _status = MutableLiveData<NetworkApiStatus?>()
    val status : LiveData<NetworkApiStatus?> = _status

    val lastSync = Prefs.syncEvent.map { Prefs.lastSync }
    val syncCount = Prefs.syncEvent.map { Prefs.syncCount }

    init {
        if (!Prefs.hasSync)
            refresh()
    }

    fun refresh() { refreshImpl() }

    private fun refreshImpl() = viewModelScope.launch {
        _status.value = NetworkApiStatus.Loading
        runCatching {
            info("call refreshVideos from UI, on $thread")
            repo.refreshVideos()
            info("call refreshVideos from UI - success")
            _status.value = NetworkApiStatus.Success
        }.onFailure { err ->
            error("call refreshVideos from UI - error", err)
            _status.value = NetworkApiStatus.Error.of(err)
        }
    }

}

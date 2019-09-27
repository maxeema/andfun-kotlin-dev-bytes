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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import maxeem.america.devbytes.network.NetworkApiStatus
import maxeem.america.devbytes.repository.VideosRepository
import maxeem.america.devbytes.util.Prefs
import maxeem.america.devbytes.util.asImmutable
import maxeem.america.devbytes.util.asMutable
import maxeem.america.devbytes.util.thread
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class ListingViewModel : ViewModel(), AnkoLogger {

    private val repo = VideosRepository.instance

    val videos  = repo.videos
    val hasData = videos.map { !it.isNullOrEmpty() }.apply { asMutable().value = !videos.value.isNullOrEmpty()}

    val status = MutableLiveData<NetworkApiStatus?>().asImmutable()
    val statusEvent = status.map { it }
    fun consumeStatusEvent() { statusEvent.asMutable().value = null }

    val lastSync  = Prefs.syncEvent.map { Prefs.lastSync }
    val syncCount = Prefs.syncEvent.map { Prefs.syncCount }

    init {
        info("init, hasSync: ${Prefs.hasSync}, syncCount: ${Prefs.syncCount}, lastSync: ${Prefs.lastSync}")
        if (!Prefs.hasSync)
            refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshImpl()
        }
    }

    private suspend fun refreshImpl() {
        info("call refreshVideos from UI, on $thread")
        status.asMutable().value = NetworkApiStatus.Loading
        runCatching {
            delay(200)
            repo.refreshVideos("listing model")
            delay(200)
        }.onSuccess {
            info("call refreshVideos from UI - success, on $thread")
            status.asMutable().value = NetworkApiStatus.Success
        }.onFailure { err ->
            error("call refreshVideos from UI - error, on $thread", err)
            status.asMutable().value = NetworkApiStatus.Error.of(err)
        }
    }

    override fun onCleared() {
        super.onCleared()
        info("onCleared()")
    }

}

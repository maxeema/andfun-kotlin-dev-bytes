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

package maxeem.america.devbyteviewer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import maxeem.america.devbyteviewer.database.VideosDatabase
import maxeem.america.devbyteviewer.repository.VideosRepository

class DevByteViewModel : ViewModel() {

    private val repo = VideosRepository(VideosDatabase.instance)

    init {
        viewModelScope.launch {
            runCatching {
                repo.refreshVideos()
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    val videos = repo.videos

}

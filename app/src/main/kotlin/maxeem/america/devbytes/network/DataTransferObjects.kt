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

package maxeem.america.devbytes.network

import com.squareup.moshi.JsonClass
import maxeem.america.devbytes.database.DatabaseVideo

@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

@JsonClass(generateAdapter = true)
data class NetworkVideo(
        val url: String,
        val updated: String,
        val title: String,
        val thumbnail: String,
        val description: String)

fun NetworkVideoContainer.asDatabaseModel() =
        videos.map {
            DatabaseVideo(
                    url = it.url,
                    updated = it.updated,
                    title = it.title,
                    thumbnail = it.thumbnail,
                    description = it.description)
        }.toTypedArray()

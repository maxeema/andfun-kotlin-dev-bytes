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

package maxeem.america.devbytes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import maxeem.america.devbytes.domain.Video

@Entity(tableName = "videos")
open class DatabaseVideo(
        @PrimaryKey
        val url: String,         // "https://www.youtube.com/watch?v=sYGKUtM2ga8"
        val updated: String,     // "2018-06-07T17:09:43+00:00",
        val title: String,       // "Android Jetpack: EmojiCompat"
        val thumbnail: String,   // "https://i4.ytimg.com/vi/sYGKUtM2ga8/hqdefault.jpg"
        val description: String) // "With the EmojiCompat library, part of Jetpack, your app can ..."

class DatabaseVideoQuery(
        @ColumnInfo(name="rowid")
        val id: Long,
        url: String,
        updated: String,
        title: String,
        thumbnail: String,
        description: String)
    : DatabaseVideo(url, updated, title, thumbnail, description)

fun List<DatabaseVideoQuery>.asDomainModel() =
    map {
        Video (
                id = it.id,
                url = it.url,
                updated = it.updated,
                title = it.title,
                description = it.description,
                thumbnail = it.thumbnail)
    }

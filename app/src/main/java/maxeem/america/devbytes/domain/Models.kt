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

package maxeem.america.devbytes.domain

import android.text.format.DateUtils.*
import maxeem.america.devbytes.app
import maxeem.america.devbytes.util.smartTruncate
import java.text.SimpleDateFormat

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

/**
 * Videos represent a devbyte that can be played.
 */
data class Video(val title: String,
                 val description: String,
                 val url: String,
                 val updated: String,
                 val thumbnail: String) {

    private companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }

    val date : String?
        get() {
            val millis = dateFormat.parse(updated)?.time ?: return null
            return formatDateTime(app, millis, FORMAT_SHOW_DATE or FORMAT_SHOW_YEAR or FORMAT_ABBREV_ALL)
        }

    val shortDescription: String
        get() = description.smartTruncate(200)
}

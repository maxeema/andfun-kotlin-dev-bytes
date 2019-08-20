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

package maxeem.america.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import maxeem.america.devbyteviewer.database.VideosDatabase
import maxeem.america.devbyteviewer.repository.VideosRepository

class RefreshDataWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    companion object {
            const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork() =
            runCatching {
                VideosRepository(VideosDatabase.instance).refreshVideos()
                Result.success()
            }.getOrElse {
                it.printStackTrace()
                Result.retry()
            }

}

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

package maxeem.america.devbytes.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import maxeem.america.devbytes.R
import maxeem.america.devbytes.util.hash
import maxeem.america.devbytes.util.timeMillis
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.configuration
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    init {
        info("$hash $timeMillis init")
    }

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        info("$hash $timeMillis onCreate, savedInstanceState: $savedInstanceState\n" +
            " UI in night mode: ${UI.inNightMode} \n" +
            " res isNight: ${resources.getBoolean(R.bool.isNight)} \n" +
            " configuration ui mode night: ${configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK}\n" +
            " delegate local night mode: ${delegate.localNightMode}\n" +
            " app compat delegate default night mode: ${AppCompatDelegate.getDefaultNightMode()}")
    }

    override fun onNightModeChanged(mode: Int) { super.onNightModeChanged(mode)
        info("$hash $timeMillis onNightModeChanged, mode: $mode")
    }

    override fun onDestroy() { super.onDestroy()
        info("$hash $timeMillis onDestroy")
    }

}

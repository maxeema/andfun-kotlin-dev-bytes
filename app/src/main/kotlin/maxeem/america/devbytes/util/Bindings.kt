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

package maxeem.america.devbytes.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.TooltipCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("srcOf")
fun ImageView.srcOf(url: String) = GlideHelper.loadUrlInto(context, url, this)

@BindingAdapter("visibleOn")
fun View.visibleOn(it: Boolean?) { visibility = if (it == true) View.VISIBLE else View.INVISIBLE }

@BindingAdapter("goneIf")
fun View.goneIf(it: Boolean?) { visibility = if (it == true) View.GONE else View.VISIBLE }

@BindingAdapter("textHtml")
fun TextView.textHtml(str: String) { text = str.fromHtml() }

@BindingAdapter("goneIfNotNull")
fun View.goneIfNotNull(it: Any?) { visibility = if (it != null) View.GONE else View.VISIBLE }

@BindingAdapter("tooltipCompat")
fun View.tooltipCompat(str: String) { TooltipCompat.setTooltipText(this, str) }

//@BindingAdapter("onClickNotImplemented")
//fun View.onClickNotImplemented(msg: CharSequence?) {
//    onClick {
//        longSnackbar(R.string.not_implemented).apply {
//            anchorView = this@onClickNotImplemented
//        }
//    }
//}
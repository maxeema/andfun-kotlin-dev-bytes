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

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.devbytes.R
import maxeem.america.devbytes.app
import maxeem.america.devbytes.databinding.FragmentListingBinding
import maxeem.america.devbytes.domain.Video
import maxeem.america.devbytes.network.NetworkApiStatus
import maxeem.america.devbytes.util.*
import maxeem.america.devbytes.viewmodels.ListingViewModel
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar

class ListingFragment : Fragment(), AnkoLogger {

    private val model: ListingViewModel by viewModels()
    private lateinit var binding : FragmentListingBinding

    private val busy = ObservableBoolean(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$timeMillis $hash onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing, container, false)

        compatActivity()?.setSupportActionBar(binding.toolbar)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = model
        binding.busy = busy

        binding.refresh.setOnRefreshListener(::refreshVideos)

        val adapter = ListingAdapter(::playVideo)
        binding.recycler.addItemDecoration(object: RecyclerView.ItemDecoration() {
            val gap = context!!.dip(8); val spanCount = (binding.recycler.layoutManager as GridLayoutManager).spanCount
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (spanCount == 1) {
                    super.getItemOffsets(outRect, view, parent, state)
                } else {
                    outRect.set(if (binding.recycler.getChildAdapterPosition(view) % spanCount == 0) gap else 0, 0, gap, 0)
                }
            }
        })
        binding.recycler.adapter = adapter

        model.videos.observe(viewLifecycleOwner) { videos ->
            if (videos.isNullOrEmpty()) return@observe
            // Note, that this callback could be from user swipe-to-refresh action or RefreshDataWorker job by WorkManager
            if (busy.get()) // on user interaction clear list fully and fill it with the last data again
                adapter.submitList(emptyList())
            else
                busy.set(true) // in case of WorkManager job we just merge data within current in adapter, but in reality it is very rare case =)
            compatActivity()?.delayed(100) {
                binding.recycler.itemAnimator?.isRunning {
                    if (lifecycle.currentState < Lifecycle.State.CREATED) return@isRunning
                    adapter.submitList(videos)
                    compatActivity()?.delayed(500) {
                        binding.recycler.itemAnimator?.isRunning(::setNotBusy)
                    }
                }
            }
        }
        model.status.observe(viewLifecycleOwner) { status ->
            if (status !is NetworkApiStatus.Error) return@observe
            compatActivity()?.delayed(500) {
                setNotBusy()
                if (adapter.itemCount == 0) return@delayed
                val msgId = if (status is NetworkApiStatus.ConnectionError) R.string.no_connection else R.string.some_error
                view?.longSnackbar(msgId)?.apply {
                    if (status !is NetworkApiStatus.ConnectionError)
                        setAction(R.string.details) { activity!!.alert(Utils.formatError(msgId, status.err)) { cancelButton { dismiss() } }.show() }
                }
            }
        }
        binding.error.onClick(::refreshVideos)

        return binding.root
    }

    private fun setNotBusy() {
        busy.set(false)
        binding.refresh.isRefreshing = false
        binding.refresh.isEnabled = true
    }

    private fun refreshVideos() {
        if (busy.get()) return
        busy.set(true)
        compatActivity()?.delayed(300, code = model::refresh)
    }

    private fun playVideo(video: Video) {
        if (busy.get()) return
        var intent = Intent(Intent.ACTION_VIEW, video.launchUri)
        if (intent.resolveActivity(app.packageManager) == null) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
        }
        startActivity(intent)
    }

    private val Video.launchUri : Uri
        get() = Uri.parse("vnd.youtube:" + Uri.parse(url).getQueryParameter("v"))

}

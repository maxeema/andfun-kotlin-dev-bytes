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
import androidx.core.net.toUri
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.devbytes.R
import maxeem.america.devbytes.app
import maxeem.america.devbytes.databinding.FragmentListingBinding
import maxeem.america.devbytes.domain.Video
import maxeem.america.devbytes.network.NetworkApiStatus
import maxeem.america.devbytes.util.*
import maxeem.america.devbytes.viewmodels.ListingViewModel
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.info

class ListingFragment : BaseFragment() {

    private val model: ListingViewModel by viewModels()
    private lateinit var binding : FragmentListingBinding

    private val busy = ObservableBoolean(false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$timeMillis $hash onCreateView, savedInstanceState: $savedInstanceState")

        binding = FragmentListingBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = model
        binding.busy = busy

        binding.toolbar.setOnMenuItemClickListener {
            findNavController().navigate(AboutFragmentDirections.actionGlobalAboutFragment())
            true
        }

        binding.refresh.setOnRefreshListener(::refreshVideos)

        val adapter = ListingAdapter(::playVideo)
        binding.recycler.addItemDecoration(object: RecyclerView.ItemDecoration() {
            val spanCount = resources.getInteger(R.integer.grid_spans)
            val hGap = resources.getDimension(R.dimen.spans_gap_horizontal).toInt()
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (spanCount == 1) {
                    super.getItemOffsets(outRect, view, parent, state)
                } else {
                    outRect.set(if (binding.recycler.getChildAdapterPosition(view) % spanCount != 0) hGap else 0, 0, 0, 0)
                }
            }
        })
        binding.recycler.adapter = adapter

        model.videos.observe(viewLifecycleOwner) { videos ->
            info("observe videos, size: ${videos?.size}, ids: ${videos.map { it.id }}" +
                    "\n status: ${model.status.value}, statusEvent: ${model.statusEvent.value}")
            if (!videos.isNullOrEmpty())
                adapter.submitList(videos)
        }
        model.statusEvent.observe(viewLifecycleOwner) { status ->
            info("observe status: $status, model status: ${model.status.value}," +
                    " view lifecycle state: ${viewOwner?.lifecycle?.currentState}")
            status ?: return@observe
            if (status == NetworkApiStatus.Loading) {
                busy.set(true)
                return@observe
            }
            viewOwner?.delayed(700) {
                model.consumeStatusEvent()
                endRefresh()
                // show errors in snackbars only when adapter contains data otherwise it'll be displayed by layout
                when (status) {
                    NetworkApiStatus.Success -> view?.longSnackbar(R.string.update_success)
                    is NetworkApiStatus.Error -> {
                        if (adapter.itemCount > 0) return@delayed
                        val msgId = if (status is NetworkApiStatus.ConnectionError) R.string.no_connection else R.string.some_error
                        view?.longSnackbar(msgId)?.apply {
//                            setAnchorView(R.id.msg)
                            if (status !is NetworkApiStatus.ConnectionError)
                                setAction(R.string.details) {
                                    materialAlert(Utils.formatError(msgId, status.err)) { setNegativeButton(R.string.close, null) }.show()
                                }
                        }
                    }
                }
            }
        }
        binding.error.onClick(::refreshVideos)

        return binding.root
    }

    private fun endRefresh() {
        busy.set(false)
        binding.refresh.isRefreshing = false
        binding.refresh.isEnabled = true
    }

    private fun refreshVideos() {
        if (busy.get()) return
        busy.set(true)
        delayed(200, code = model::refresh)
    }

    private fun playVideo(video: Video) {
        var intent = Intent(Intent.ACTION_VIEW, video.launchUri)
        if (intent.resolveActivity(app.packageManager) == null) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
        }
        startActivity(intent)
    }

    private val Video.launchUri : Uri
        get() = Uri.parse("vnd.youtube:" + url.toUri().getQueryParameter("v"))

}

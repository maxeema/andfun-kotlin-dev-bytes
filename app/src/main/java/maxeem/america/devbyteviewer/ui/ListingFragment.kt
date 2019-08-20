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

package maxeem.america.devbyteviewer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.devbyteviewer.R
import maxeem.america.devbyteviewer.app
import maxeem.america.devbyteviewer.databinding.DevbyteItemBinding
import maxeem.america.devbyteviewer.databinding.FragmentListingBinding
import maxeem.america.devbyteviewer.domain.Video
import maxeem.america.devbyteviewer.viewmodels.DevByteViewModel

class ListingFragment : Fragment() {

    private val viewModel: DevByteViewModel by viewModels()
    private var viewModelAdapter: DevByteAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentListingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModelAdapter = DevByteAdapter { val video = it.getTag(R.id.tag_video) as Video
            var intent = Intent(Intent.ACTION_VIEW, video.launchUri)
            if (intent.resolveActivity(app.packageManager) == null) {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
            }
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = viewModelAdapter

        return binding.root
    }

    private val Video.launchUri : Uri
        get() = Uri.parse("vnd.youtube:" + Uri.parse(url).getQueryParameter("v"))

}

class DevByteAdapter(private val play: (View) -> Unit) : RecyclerView.Adapter<DevByteViewHolder>() {

    var videos: List<Video> = emptyList()
        set(value) { field = value;  notifyDataSetChanged() }

    override fun getItemCount() = videos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DevByteViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), DevByteViewHolder.LAYOUT, parent, false))

    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.viewDataBinding.apply {
            video = videos[position]
            root.setTag(R.id.tag_video, video)
            clickableOverlay.setOnClickListener(play)
        }
    }

}

class DevByteViewHolder(val viewDataBinding: DevbyteItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes val LAYOUT = R.layout.devbyte_item
    }
}

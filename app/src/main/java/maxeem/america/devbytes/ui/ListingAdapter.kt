package maxeem.america.devbytes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.devbytes.R
import maxeem.america.devbytes.databinding.ListingItemBinding
import maxeem.america.devbytes.domain.Video

class ListingAdapter(private val playOnClick: (Video) -> Unit)
            : ListAdapter<Video, ListingAdapter.DevByteViewHolder>(DiffCallback) {

    init {
        setHasStableIds(true)
    }
    override fun getItemId(position: Int) = getItem(position)?.id ?: RecyclerView.NO_ID

    private companion object DiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video) = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: Video, newItem: Video) = oldItem == newItem
    }

    private val onClick : (View) -> Unit = { view -> playOnClick(view.getTag(R.id.tag_video) as Video) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        = DevByteViewHolder.create(parent, LayoutInflater.from(parent.context))

    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.viewDataBinding.apply {
            video = getItem(position)
            clickableOverlay.apply {
                setOnClickListener(onClick)
                setTag(R.id.tag_video, video)
            }
        }
    }

    class DevByteViewHolder private constructor (val viewDataBinding: ListingItemBinding)
                : RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            fun create(parent: ViewGroup, inflater: LayoutInflater)
                    = DevByteViewHolder(ListingItemBinding.inflate(inflater, parent, false))
        }
    }

}


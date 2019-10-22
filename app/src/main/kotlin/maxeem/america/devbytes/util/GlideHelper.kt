package maxeem.america.devbytes.util

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import maxeem.america.devbytes.ui.UI
import org.jetbrains.anko.withAlpha

object GlideHelper {

    fun loadUrlInto(ctx: Context, url: String, img: ImageView) {
        Glide.with(ctx)
            .load(url)
    //        .transition(DrawableTransitionOptions.withCrossFade())
            .listener(getListener())
            .into(img)
    }

    private lateinit var listener : RequestListener<Drawable>
    private lateinit var filter: PorterDuffColorFilter

    private fun getListener() : RequestListener<Drawable>{
        if (::listener.isInitialized.not()) {
            filter = PorterDuffColorFilter(
                    Color.BLACK.withAlpha(0x33), PorterDuff.Mode.DARKEN)
            listener = object: RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean) = false
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean) = false.apply {
                    if (UI.inNightMode && resource is BitmapDrawable)
                        resource.colorFilter = filter
                }
            }
        }
        return listener
    }

}


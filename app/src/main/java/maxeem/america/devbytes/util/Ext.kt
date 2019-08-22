package maxeem.america.devbytes.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import maxeem.america.devbytes.app
import maxeem.america.devbytes.handler

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun Int.asString() = app.getString(this)
fun String.fromHtml() = Utils.fromHtml(this)

fun View.onClick(l: ()->Unit) = setOnClickListener { l() }

fun Fragment.compatActivity() = activity as AppCompatActivity?

fun AppCompatActivity.delayed(delay: Long, stateAtLeast: Lifecycle.State = Lifecycle.State.CREATED, code: ()->Unit) {
    if (isFinishing || isDestroyed) return
    app.handler.postDelayed(delay) {
        if (lifecycle.currentState.isAtLeast(stateAtLeast))
            code()
    }
}

private val PUNCTUATION = listOf(", ", "; ", ": ", " ")
/**
 * Truncate long text with a preference for word boundaries and without trailing punctuation.
 */
fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append("...")
    }
    return builder.toString()
}
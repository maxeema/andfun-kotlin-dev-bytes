package maxeem.america.devbytes.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import maxeem.america.devbytes.app

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun Int.asString() = app.getString(this)
fun Int.asString(vararg args: Any) = app.getString(this, *args)
fun Int.asQuantityString(quantity: Int, vararg args: Any) = app.resources.getQuantityString(this, quantity, *args)
fun Int.asDrawable() = app.getDrawable(this)

fun View.onClick(l: ()->Unit) = setOnClickListener { l() }

fun Fragment.compatActivity() = activity as AppCompatActivity?

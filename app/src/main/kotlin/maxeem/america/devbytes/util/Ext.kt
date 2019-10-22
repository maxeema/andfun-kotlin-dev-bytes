package maxeem.america.devbytes.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import maxeem.america.devbytes.app
import maxeem.america.devbytes.ui.BaseFragment

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun Int.asString() = app.getString(this)
fun Int.asString(vararg args: Any) = app.getString(this, *args)
fun Int.asQuantityString(quantity: Int, vararg args: Any) = app.resources.getQuantityString(this, quantity, *args)
fun Int.asDrawable() = app.getDrawable(this)

fun View.onClick(l: ()->Unit) = setOnClickListener { l() }

fun BaseFragment.compatActivity() = activity as AppCompatActivity?

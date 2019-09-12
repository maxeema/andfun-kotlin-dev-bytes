package maxeem.america.devbytes.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun View.onClick(l: ()->Unit) = setOnClickListener { l() }

fun Fragment.compatActivity() = activity as AppCompatActivity?

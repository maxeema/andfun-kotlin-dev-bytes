package maxeem.america.devbytes.util

import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import maxeem.america.devbytes.R

fun Fragment.materialAlert(@StyleRes theme: Int = R.style.AlertOverride,
                                     msg: CharSequence,
                                     code: (MaterialAlertDialogBuilder.()->Unit)? = null)
       : AlertDialog = MaterialAlertDialogBuilder(context, theme).apply {
            setMessage(msg)
            code?.invoke(this)
        }.show()

fun Fragment.materialAlert(@StringRes msg: Int, code: (MaterialAlertDialogBuilder.()->Unit)? = null)
        = materialAlert(msg = msg.asString(), code = code)

fun Fragment.materialAlertError(msg: CharSequence, code: (MaterialAlertDialogBuilder.()->Unit)? = null)
        = materialAlert(R.style.AlertOverride_Error, msg, code)

fun Fragment.materialAlertError(@StringRes msg: Int, code: (MaterialAlertDialogBuilder.()->Unit)? = null)
        = materialAlertError(msg.asString(), code)
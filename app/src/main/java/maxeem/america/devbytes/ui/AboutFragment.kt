package maxeem.america.devbytes.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import maxeem.america.devbytes.R
import maxeem.america.devbytes.app
import maxeem.america.devbytes.databinding.FragmentAboutBinding
import maxeem.america.devbytes.packageInfo
import maxeem.america.devbytes.util.asString
import maxeem.america.devbytes.util.hash
import maxeem.america.devbytes.util.onClick
import maxeem.america.devbytes.util.timeMillis
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AboutFragment : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        = FragmentAboutBinding.inflate(inflater, container, false).apply {
            info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState")
            lifecycleOwner = viewLifecycleOwner
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(AboutFragmentDirections.actionAboutFragmentPop())
            }
            author.apply {
                val mail = Intent(Intent.ACTION_SENDTO)
                        .setData("mailto:${R.string.author_email.asString()}".toUri())
                isClickable = mail.resolveActivity(app.packageManager) != null
                if (isClickable) onClick {
                    startActivity(mail.apply {
                        putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject.asString(R.string.app_name.asString()))
                    })
                }
            }
            version.text = app.packageInfo.versionName.substringBefore('-')
            googlePlay.onClick {
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://play.google.com/store/apps/details?id=${app.packageName}".toUri()
                    `package` = "com.android.vending"
                    if (resolveActivity(app.packageManager) == null)
                        `package` = null
                    startActivity(this)
                }
            }
        }.root

}
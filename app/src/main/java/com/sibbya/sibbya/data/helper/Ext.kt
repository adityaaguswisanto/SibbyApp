package com.sibbya.sibbya.data.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.sibbya.sibbya.BuildConfig
import com.sibbya.sibbya.R
import com.sibbya.sibbya.data.network.Resource
import com.sibbya.sibbya.ui.auth.forgot.ForgotFragment
import com.sibbya.sibbya.ui.auth.login.LoginFragment
import com.sibbya.sibbya.ui.home.HomeActivity
import kotlinx.coroutines.launch
import java.io.FileDescriptor
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/** For Make Toast a View */
fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/** For Make Launch Intent a Activity */
fun <A : Activity> Activity.launchActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

/** For Make alert Dialog a View */
fun Context.alertDialog(
    msg: String,
    positive: String,
    negative: String,
    listener: DialogInterface.OnClickListener
) {
    val alert = AlertDialog.Builder(this)
    alert.setTitle("Apakah Anda Yakin ?")
    alert.setMessage(msg)
    alert.setPositiveButton(
        positive, listener
    )
    alert.setNegativeButton(
        negative, null
    )
    val alertDialog = alert.create()
    alertDialog.show()
}

/** For Make Url from Api */
fun urlAssets() = "${BuildConfig.BASE_URL}/images/"

/** For Make Url from Api */
fun urlNews() = "${BuildConfig.BASE_URL}/news/"

/** For Make Visible, Gone and Invisible a View */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

/** For Descriptor */
fun Context.fileDescriptor(uri: Uri): FileDescriptor? =
    contentResolver.openFileDescriptor(uri, "r", null)?.fileDescriptor

/** For Make Glide a View */
fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .error(R.drawable.ic_broken)
        .into(this)
}

/** For Make Permission External Storage*/
fun Fragment.permissionExternalStorage() = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) {
    if (it)
        Log.i("Message", "Info: Is Granted")
    else
        Log.i("Message", "Info: Is Denied")
}

/** For Make Format Date*/
fun formatDate(dateString: String): String? {
    try {
        var sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sd.timeZone = TimeZone.getTimeZone("GMT")
        val d: Date = sd.parse(dateString)
        sd = SimpleDateFormat("dd MMMM yyyy")
        return sd.format(d)
    } catch (e: ParseException) {
    }
    return ""
}

fun formatDateWithTime(dateString: String): String? {
    try {
        var sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sd.timeZone = TimeZone.getTimeZone("GMT")
        val d: Date = sd.parse(dateString)
        sd = SimpleDateFormat("dd MMMM yyyy - HH:mm:ss")
        return sd.format(d)
    } catch (e: ParseException) {
    }
    return ""
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.logout() = lifecycleScope.launch {
    if (activity is HomeActivity) {
        (activity as HomeActivity).performLogout()
    }
}

/** For Make Multipart Filing a Image Post */
fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Terjadi Kesalahan Pada Server", retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("Email atau password salah")
            } else {
                logout()
            }
        }
        failure.errorCode == 500 -> {
            if (this is ForgotFragment) {
                requireView().snackbar("Kami tidak dapat menemukan pengguna dengan alamat email tersebut.")
            }
        }
        failure.errorCode == 422 -> {
            if (this is ForgotFragment) {
                requireView().snackbar("Mohon tunggu sebentar dan coba kembali.")
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}
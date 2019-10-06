package c.m.jeparalanguage.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun alertDialog(context: Context, titleText: String, messageText: String): AlertDialog.Builder? =
    context.let {
        AlertDialog.Builder(it)
    }.setTitle(titleText).setMessage(messageText)

fun toast(context: Context, messageText: String, isShortToast: Boolean = true): Toast =
    when (isShortToast) {
        false -> Toast.makeText(context, messageText, Toast.LENGTH_LONG)
        true -> Toast.makeText(context, messageText, Toast.LENGTH_SHORT)
    }

// For connection checked
@Suppress("DEPRECATION")
fun Context.isNetworkStatusAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.let { cm ->
        cm.activeNetworkInfo?.let {
            if (it.isConnected) return true
        }
    }
    return false
}



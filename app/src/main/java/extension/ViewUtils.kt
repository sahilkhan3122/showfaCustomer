package extension

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.showfa_customer_android.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun View.showSnackBar(message: String) {

    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(resources.getString(R.string.Okk)) {}
    snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.theme_color))
    snackBar.show()
}

fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun getDateFormat(time: Date?): String? {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    return sdf.format(time)
}

fun hideSoftKeyboard(activity: Activity, view: View? = null) {
    try {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        // TODO: handle exception
        e.printStackTrace()
    }

}


fun Context.dialogShow(
    title: String? = null,
    msg: String? = null,
    positiveText: String? = null,
    negativeText: String? = null,
    positiveClick: (() -> Unit)? = null,
    negativeClick: (() -> Unit)? = null,
    dialogView: View? = null,
): MaterialAlertDialogBuilder {
    val builder = MaterialAlertDialogBuilder(this)

    builder.setView(dialogView)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton(
            positiveText
        ) { _, _ -> positiveClick?.invoke() }
        .setNegativeButton(negativeText)
        { _, _ -> negativeClick?.invoke() }.background = ColorDrawable(Color.TRANSPARENT)

    return builder

}


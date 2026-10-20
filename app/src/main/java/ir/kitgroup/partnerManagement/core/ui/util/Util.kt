package ir.kitgroup.partnerManagement.core.ui.util

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ir.kitgroup.partnerManagement.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.util.UUID

import saman.zamani.persiandate.PersianDate
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


/** دانلود عکس و ذخیره روی حافظه داخلی */
suspend fun saveBase64ImageToFile(base64Data: String, fileName: String, context: Context): String? {
    return withContext(Dispatchers.IO) {
        try {
            // پاک‌سازی احتمالی header و newlineها
            val cleanedBase64 = base64Data.substringAfter(",").replace("\n", "").replace("\r", "")

            // decode Base64
            val bytes = android.util.Base64.decode(cleanedBase64, android.util.Base64.DEFAULT)

            // ایجاد فایل با پسوند jpg در حافظه داخلی
            val file = File(context.filesDir, "$fileName.jpg")
            file.outputStream().use { it.write(bytes) }

            // بازگرداندن مسیر فایل
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}


fun getTypefaceRegular(context: Context): Typeface {
    return ResourcesCompat.getFont(context, R.font.iran_sans)!!
}

fun getColorAttr(ctx: Context, attrId: Int): Int {
    val typedValue = TypedValue()
    ctx.theme.resolveAttribute(attrId, typedValue, true)
    return ContextCompat.getColor(ctx, typedValue.resourceId)
}

fun Context.getColorFromAttr(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}

fun String.toEnglishDigits(): String {

    val persianDigits = "۰۱۲۳۴۵۶۷۸۹"
    val arabicDigits = "٠١٢٣٤٥٦٧٨٩"

    return buildString {

        for (char in this@toEnglishDigits) {

            when {
                char in persianDigits -> {
                    append(
                        ('0'.code + persianDigits.indexOf(char))
                            .toChar()
                    )
                }

                char in arabicDigits -> {
                    append(
                        ('0'.code + arabicDigits.indexOf(char))
                            .toChar()
                    )
                }

                else -> append(char)
            }
        }
    }
}

fun String.fixPersianChars(): String {
    return replace('ي', 'ی')
        .replace('ى', 'ی')
        .replace('ك', 'ک')
}

object ErrorHandler {
    fun getHttpErrorMessage(context: Context, code: Int, message: String?): String {
        return when (code) {
            400 -> context.getString(R.string.error_bad_request)
            401 -> context.getString(R.string.error_unauthorized)
            403 -> context.getString(R.string.error_forbidden)
            404 -> context.getString(R.string.error_not_found)
            500 -> context.getString(R.string.error_internal_server)
            else -> getErrorMessage(context, message)
        }
    }

    private fun getErrorMessage(context: Context, message: String?): String {
        return when {
            message.isNullOrBlank() -> context.getString(R.string.error_unknown)
            message.contains("timeout", ignoreCase = true) ->
                context.getString(R.string.error_timeout)

            message.contains("Unable to resolve host", ignoreCase = true) ->
                context.getString(R.string.error_network_internet)

            else -> message
        }
    }

    fun getExceptionMessage(context: Context, throwable: Throwable): String {
        return when (throwable) {
            is IOException -> context.getString(R.string.error_network_internet)
            is HttpException -> getErrorMessage(context, throwable.message())
            else -> context.getString(R.string.error_unknown)
        }
    }
}


fun convertNumbersToEnglish(input: String): String {
    val arabicNumbers = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val persianNumbers = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    val englishNumbers = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    var result = input
    for (i in 0..9) {
        result = result.replace(persianNumbers[i], englishNumbers[i])
        result = result.replace(arabicNumbers[i], englishNumbers[i])
    }
    return result
}

fun formatJalaliDate(dateString: String?): String {
    if (dateString.isNullOrBlank()) return "-"
    return try {
        // اگر تاریخ فقط شامل yyyy-MM-dd بود یا فرمت کامل ISO داشت
        val cleanDate = dateString.substringBefore("T").trim()
        val parts = cleanDate.split("-")
        if (parts.size == 3) {
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()

            val persianDate = PersianDate()
            val jalali = persianDate.gregorian_to_jalali(year, month, day)
            // فرمت خروجی: 1405/07/29
            String.format(
                java.util.Locale.US,
                "%04d/%02d/%02d",
                jalali[0],
                jalali[1],
                jalali[2]
            )
        } else {
            dateString
        }
    } catch (e: Exception) {
        dateString
    }
}



@Composable
fun String?.toAmountDisplay(): String {
    if (isNullOrBlank()) {
        return stringResource(R.string.value_not_available)
    }

    val longValue = this.replace(",", "").toDoubleOrNull()?.toLong()
    val formattedAmount = if (longValue != null) {
        NumberFormat.getNumberInstance(Locale.US).format(longValue)
    } else {
        this
    }

    return stringResource(
        R.string.value_amount_rial,
        formattedAmount
    )
}

fun jalaliToGregorian(date: String): String? {
    return try {
        val parts = date.split("/")

        if (parts.size != 3) {
            return null
        }

        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val day = parts[2].toInt()

        val gregorian = PersianDate().jalali_to_gregorian(year, month, day)

        "%04d-%02d-%02d".format(
            gregorian[0],
            gregorian[1],
            gregorian[2]
        )
    } catch (e: Exception) {
        null
    }
}

fun formatPercentForInput(value: Double?): String {
    if (value == null) return ""

    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}

fun formatJalali(year: Int, month: Int, day: Int): String {
    return String.format(Locale.US, "%04d/%02d/%02d", year, month, day)
}

fun extractErrorMessage(
    errorBody: String?,
    defaultMessage: String
): String {
    if (errorBody.isNullOrBlank()) {
        return defaultMessage
    }

    return try {
        val json = org.json.JSONObject(errorBody)

        json.optString("message")
            .takeIf { it.isNotBlank() }
            ?: defaultMessage

    } catch (e: Exception) {
        errorBody
    }
}


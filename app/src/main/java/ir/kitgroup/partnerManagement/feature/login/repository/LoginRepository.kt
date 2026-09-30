package ir.kitgroup.partnerManagement.feature.login.repository


import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.core.network.NetworkResult
import ir.kitgroup.partnerManagement.core.network.ServerErrorResponse
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.ui.util.ErrorHandler.getExceptionMessage
import ir.kitgroup.partnerManagement.feature.login.model.LoginRequest
import ir.kitgroup.partnerManagement.feature.login.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.content.Context
import android.os.Build
import android.provider.Settings


class LoginRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiService,
    private val gson: Gson
) {

    private fun getDeviceModel(): String {
        return Build.MODEL ?: "unknown"
    }
    private fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
            ?: Build.VERSION.SDK_INT.toString()
    }

    private fun getAppVersion(): String {
        return try {
            context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName
                ?: "0"
        } catch (e: Exception) {
            "0"
        }
    }

    private fun getDeviceIdFallback(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"
    }

    suspend fun loginUser(
        username: String,
        password: String
    ): NetworkResult<LoginResponse> {

        return withContext(Dispatchers.IO) {

            try {

                val request = LoginRequest(
                    username = username,
                    password = password,
                    deviceModel = getDeviceModel(),
                    androidVersion = getAndroidVersion(),
                    imei = getDeviceIdFallback(),
                    appVersion = getAppVersion()
                )

                val response = api.login(request)

                // -----------------------------------------
                // SUCCESS
                // -----------------------------------------

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {
                        return@withContext NetworkResult.Success(body)
                    }

                    return@withContext NetworkResult.Error(
                        "پاسخ معتبری از سرور دریافت نشد"
                    )
                }

                // -----------------------------------------
                // ERROR
                // -----------------------------------------

                val errorBody = response.errorBody()
                    ?.string()

                // تلاش برای خواندن JSON خطای سرور
                val serverMessage = parseServerErrorMessage(errorBody)

                val finalMessage = when {
                    !serverMessage.isNullOrBlank() -> serverMessage

                    !response.message().isNullOrBlank() ->
                        response.message()

                    else ->
                        "خطا در ارتباط با سرور (${response.code()})"
                }

                NetworkResult.Error(finalMessage)

            } catch (ex: Exception) {

                NetworkResult.Error(
                    getExceptionMessage(
                        context,
                        ex
                    )
                )
            }
        }
    }

    private fun parseServerErrorMessage(
        errorBody: String?
    ): String? {

        if (errorBody.isNullOrBlank()) {
            return null
        }

        return try {
            val errorResponse =
                gson.fromJson(
                    errorBody,
                    ServerErrorResponse::class.java
                )

            errorResponse.message

        } catch (e: Exception) {

            null
        }
    }

}
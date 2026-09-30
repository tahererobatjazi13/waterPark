package ir.kitgroup.partnerManagement.core.ui.util

import android.os.RemoteException
import androidx.annotation.StringRes
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.exception.ApiException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object AppErrorMapper {

    fun map(throwable: Throwable): UiText {

        return when (throwable) {

            // -----------------------------
            // Internet / Network
            // -----------------------------

            is UnknownHostException ->
                UiText.StringResource(
                    R.string.error_network_internet
                )

            is ConnectException ->
                UiText.StringResource(
                    R.string.error_network_connection
                )

            is SocketTimeoutException ->
                UiText.StringResource(
                    R.string.error_timeout
                )

            is IOException ->
                UiText.StringResource(
                    R.string.error_network_internet
                )

            // -----------------------------
            // HTTP
            // -----------------------------

            is HttpException ->
                mapHttpError(throwable.code())


            is ApiException ->
                UiText.DynamicString(
                    throwable.message ?: "خطای سرور"
                )
            // -----------------------------
            // Other
            // -----------------------------

            else ->
                UiText.DynamicString(
                    throwable.message
                        ?.takeIf { it.isNotBlank() }
                        ?: "خطای نامشخص"
                )
        }
    }

    private fun mapHttpError(code: Int): UiText {

        return when (code) {

            400 ->
                UiText.StringResource(
                    R.string.error_bad_request
                )

            401 ->
                UiText.StringResource(
                    R.string.error_unauthorized
                )

            403 ->
                UiText.StringResource(
                    R.string.error_forbidden
                )

            404 ->
                UiText.StringResource(
                    R.string.error_not_found
                )

            408 ->
                UiText.StringResource(
                    R.string.error_timeout
                )

            500 ->
                UiText.StringResource(
                    R.string.error_internal_server
                )

            502, 503, 504 ->
                UiText.StringResource(
                    R.string.error_server_unavailable
                )

            else ->
                UiText.StringResource(
                    R.string.error_http,
                    code
                )
        }
    }
}
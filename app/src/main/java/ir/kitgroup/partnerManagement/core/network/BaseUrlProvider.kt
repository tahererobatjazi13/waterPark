package ir.kitgroup.partnerManagement.core.network

import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseUrlProvider @Inject constructor(
    private val mainPreferences: MainPreferences
) {

    @Volatile
    private var cachedBaseUrl: HttpUrl? = null

    suspend fun init() {
        val url = mainPreferences.getBaseUrl()

        cachedBaseUrl = url
            .ifBlank {
                "http://192.168.20.209:9880/"
            }
            .toHttpUrl()
    }

    fun getBaseUrl(): HttpUrl {
        return cachedBaseUrl
            ?: "http://192.168.20.209:9880/".toHttpUrl()
    }

    fun updateBaseUrl(newUrl: String) {
        cachedBaseUrl = newUrl.toHttpUrl()
    }
}
package ir.kitgroup.partnerManagement.feature.login.validator

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object ServerAddressValidator {

    fun normalize(input: String): String? {

        var value = input
            .trim()
            .removeSuffix("/")

        if (value.isBlank()) {
            return null
        }

        if (
            !value.startsWith("http://", ignoreCase = true) &&
            !value.startsWith("https://", ignoreCase = true)
        ) {
            value = "http://$value"
        }

        val httpUrl = value.toHttpUrlOrNull()
            ?: return null

        if (httpUrl.encodedPath != "/" &&
            httpUrl.encodedPath.isNotBlank()
        ) {
            return null
        }

        return httpUrl.toString().trimEnd('/') + "/"
    }

    fun extractHostAndPort(baseUrl: String): String {

        if (baseUrl.isBlank()) {
            return ""
        }

        return baseUrl
            .removePrefix("http://")
            .removePrefix("https://")
            .removeSuffix("/")
            .trim()
    }
}
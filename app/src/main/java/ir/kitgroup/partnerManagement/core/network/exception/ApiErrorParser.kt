
package ir.kitgroup.partnerManagement.core.network.exception

import org.json.JSONArray
import org.json.JSONObject

object ApiErrorParser {

    fun extractMessage(
        errorBody: String?,
        defaultMessage: String
    ): String {
        if (errorBody.isNullOrBlank()) {
            return defaultMessage
        }

        return runCatching {
            val json = JSONObject(errorBody)

            extractFromJson(json)
                ?.takeIf(String::isNotBlank)
                ?: defaultMessage
        }.getOrDefault(defaultMessage)
    }

    private fun extractFromJson(json: JSONObject): String? {
        // پاسخ‌های رایج API
        val messageKeys = listOf(
            "message",
            "Message",
            "error_description",
            "error"
        )

        for (key in messageKeys) {
            val value = json.opt(key)

            when (value) {
                is String -> {
                    value.trim()
                        .takeIf(String::isNotBlank)
                        ?.let { return it }
                }

                is JSONObject -> {
                    extractFromJson(value)
                        ?.let { return it }
                }

                is JSONArray -> {
                    val messages = buildList {
                        for (i in 0 until value.length()) {
                            val item = value.opt(i)

                            when (item) {
                                is String -> item.trim()
                                    .takeIf(String::isNotBlank)
                                    ?.let(::add)

                                is JSONObject -> {
                                    extractFromJson(item)
                                        ?.let(::add)
                                }
                            }
                        }
                    }

                    messages.joinToString("، ")
                        .takeIf(String::isNotBlank)
                        ?.let { return it }
                }
            }
        }

        // خطاهای اعتبارسنجی ASP.NET Core
        val errors = json.optJSONObject("errors")
            ?: json.optJSONObject("Errors")

        if (errors != null) {
            val messages = buildList {
                val keys = errors.keys()

                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = errors.opt(key)

                    when (value) {
                        is String -> value.trim()
                            .takeIf(String::isNotBlank)
                            ?.let(::add)

                        is JSONArray -> {
                            for (i in 0 until value.length()) {
                                value.optString(i)
                                    .takeIf(String::isNotBlank)
                                    ?.let(::add)
                            }
                        }
                    }
                }
            }

            messages.distinct()
                .joinToString("، ")
                .takeIf(String::isNotBlank)
                ?.let { return it }
        }

        return null
    }
}

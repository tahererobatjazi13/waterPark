
package ir.kitgroup.partnerManagement.core.network.exception

import retrofit2.Response
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse

object ApiResponseHandler {

    fun handle(
        response: Response<ModelResponse>,
        defaultMessage: String
        ): Result<ModelResponse> {
        if (!response.isSuccessful) {
            val message = ApiErrorParser.extractMessage(
                errorBody = response.errorBody()?.string(),
                defaultMessage = defaultMessage
            )

            return Result.failure(
                ApiException(message)
            )
        }

        val body = response.body()
            ?: return Result.failure(
                ApiException(defaultMessage)
            )

        if (!body.success) {
            return Result.failure(
                ApiException(
                    body.message
                        ?.trim()
                        ?.takeIf(String::isNotBlank)
                        ?: defaultMessage
                )
            )
        }

        return Result.success(body)
    }
}


package ir.kitgroup.partnerManagement.core.network.exception

import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import kotlinx.coroutines.CancellationException
import retrofit2.Response

object ApiRequestExecutor {

    suspend fun execute(
        defaultMessage: String,
        request: suspend () -> Response<ModelResponse>
    ): Result<ModelResponse> {
        return try {
            ApiResponseHandler.handle(
                response = request(),
                defaultMessage = defaultMessage,
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

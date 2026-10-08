package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.contract.model.CreateContractRequest
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContractRepository @Inject constructor(
    private val contractDao: ContractDao,
    private val apiService: ApiService
) {

    fun observeByOrganization(
        organizationId: String
    ): Flow<List<ContractEntity>> {
        return contractDao.observeByOrganization(organizationId)
    }

    fun observeAllContracts(): Flow<List<ContractEntity>> =
        contractDao.observeAll()

    suspend fun upsertAll(
        contracts: List<ContractEntity>
    ) {
        contractDao.upsertAll(contracts)
    }
    fun observeById(
        contractId: String
    ): Flow<ContractEntity?> {

        return contractDao.observeById(contractId)
    }

    suspend fun deleteAll() {
        contractDao.deleteAll()
    }


    suspend fun createContract(
        request: CreateContractRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.createContract(request)

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                val body =
                    response.body()!!

                if (body.success) {

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception(
                            body.message
                                ?: "خطا در ثبت قرارداد"
                        )
                    )
                }

            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ثبت قرارداد: ${response.code()}"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun updateContract(
        request: CreateContractRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.updateContract(request)

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                val body =
                    response.body()!!

                if (body.success) {

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception(
                            body.message
                                ?: "خطا در ویرایش قرارداد"
                        )
                    )
                }

            } else {

                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ویرایش قرارداد: ${response.code()}"
                )
                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun terminateActiveContract(
        request: TerminateActiveContractRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.terminateActiveContract(request)

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                val body =
                    response.body()!!

                if (body.success) {

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception(
                            body.message
                                ?: "خطا در خاتمه دادن قرارداد"
                        )
                    )
                }

            } else {

                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در  خاتمه دادن قرارداد: ${response.code()}"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}


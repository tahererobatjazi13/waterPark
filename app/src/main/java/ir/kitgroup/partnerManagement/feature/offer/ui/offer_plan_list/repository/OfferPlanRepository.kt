package ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.offer.model.OfferPlanRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OfferPlanRepository @Inject constructor(
    private val offerDao: OfferDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context
) {

    fun observeAll() = offerDao.observeAll()

    fun observeOfferById(offerId: String?): Flow<OfferEntity?> {
        return if (offerId.isNullOrBlank()) {
            kotlinx.coroutines.flow.flowOf(null)
        } else {
            offerDao.observeOfferById(offerId)
        }
    }


    suspend fun createOfferPlanAndSync(
        request: OfferPlanRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_offer_plan_create)
        ) {
            apiService.createOfferPlan(request)
        }.fold(
            onSuccess = { response ->
                syncRepository.syncData()
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }


    suspend fun updateOfferPlanAndSync(
        request: OfferPlanRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_offer_plan_update)
        ) {
            apiService.updateOfferPlan(request)
        }.fold(
            onSuccess = { response ->
                syncRepository.syncData()
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

}

package ir.kitgroup.partnerManagement.core.usecase

import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import javax.inject.Inject



class CheckCanAddOfferUseCase @Inject constructor(
    private val offerDao: OfferDao) {
    /**
     * @return در صورت مجاز بودن null برمی‌گرداند؛ در صورت عدم وجود داده، UiText خطا را برمی‌گرداند.
     */
    suspend operator fun invoke(): UiText? {
        val offersCount = offerDao.getCount()

        return if (offersCount == 0 ) {
            UiText.StringResource(R.string.msg_sync_data_first)
        } else {
            null
        }
    }
}


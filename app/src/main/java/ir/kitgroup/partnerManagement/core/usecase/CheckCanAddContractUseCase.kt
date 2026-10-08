package ir.kitgroup.partnerManagement.core.usecase

import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import javax.inject.Inject

class CheckCanAddContractUseCase @Inject constructor(
    private val organizationDao: OrganizationDao
) {
    /**
     * @return در صورت مجاز بودن null برمی‌گرداند؛ در صورت عدم وجود داده، UiText خطا را برمی‌گرداند.
     */
    suspend operator fun invoke(): UiText? {
        val organizationsCount = organizationDao.getCount()

        return if (organizationsCount == 0) {
            UiText.StringResource(R.string.msg_sync_data_first)
        } else {
            null
        }
    }
}

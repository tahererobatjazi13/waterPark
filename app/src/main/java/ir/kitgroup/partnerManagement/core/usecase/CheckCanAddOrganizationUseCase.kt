package ir.kitgroup.partnerManagement.core.usecase

import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import javax.inject.Inject

class CheckCanAddOrganizationUseCase @Inject constructor(
    private val cityDao: CityDao,
    private val regionDao: RegionDao
) {
    /**
     * @return در صورت مجاز بودن null برمی‌گرداند؛ در صورت عدم وجود داده، UiText خطا را برمی‌گرداند.
     */
    suspend operator fun invoke(): UiText? {
        val citiesCount = cityDao.getCount()
        val regionsCount = regionDao.getCount()

        return if (citiesCount == 0 || regionsCount == 0) {
            UiText.StringResource(R.string.msg_sync_data_first)
        } else {
            null
        }
    }
}

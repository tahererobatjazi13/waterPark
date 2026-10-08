package ir.kitgroup.partnerManagement.core.usecase

import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import javax.inject.Inject

class CheckCanAddMeetingUseCase @Inject constructor(
    private val visitSubjectDao: VisitSubjectDao) {
    /**
     * @return در صورت مجاز بودن null برمی‌گرداند؛ در صورت عدم وجود داده، UiText خطا را برمی‌گرداند.
     */
    suspend operator fun invoke(): UiText? {
        val visitSubjectsCount = visitSubjectDao.getCount()

        return if (visitSubjectsCount == 0 ) {
            UiText.StringResource(R.string.msg_sync_data_first)
        } else {
            null
        }
    }
}

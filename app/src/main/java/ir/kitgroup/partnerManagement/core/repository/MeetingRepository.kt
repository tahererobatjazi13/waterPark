package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeetingRepository @Inject constructor(
    private val meetingDao: MeetingDao
) {

    fun observeMeetingsByOrganization(
        organizationId: String
    ): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeByOrganization(organizationId)
    }

    fun observeAllMeetings(
    ): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeAllMeetings()
    }

    fun observeMeetingsByDate(date: String): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeMeetingsByDate(date)
    }
    suspend fun deleteMeeting(meetingId: String) {
        meetingDao.deleteById(meetingId)
    }

    fun observeMeetingById(
        meetingId: String
    ): Flow<MeetingWithDetail?> {
        return meetingDao.observeMeetingById(meetingId)
    }


}
package ir.kitgroup.partnerManagement.feature.dashboard.model

import ir.kitgroup.partnerManagement.core.network.model.CityDto
import ir.kitgroup.partnerManagement.core.network.model.WarningsDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.RegionDto
import ir.kitgroup.partnerManagement.core.network.model.SubjectVisitDto
import kotlinx.serialization.Serializable

@Serializable
data class BaseDataSyncResponse(
    val cities: List<CityDto>,
    val regions: List<RegionDto>,
    val visitTopics: List<SubjectVisitDto>,
    val noticeTypes: List<WarningsDto>,
    val organizations: List<OrganizationDto>
)
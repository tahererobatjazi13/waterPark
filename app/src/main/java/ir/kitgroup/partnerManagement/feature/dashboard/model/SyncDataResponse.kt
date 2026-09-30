package ir.kitgroup.partnerManagement.feature.dashboard.model

import ir.kitgroup.partnerManagement.core.network.model.AssignedSerialDto
import ir.kitgroup.partnerManagement.core.network.model.AssignedStandDto
import ir.kitgroup.partnerManagement.core.network.model.CityDto
import ir.kitgroup.partnerManagement.core.network.model.ContractDto
import ir.kitgroup.partnerManagement.core.network.model.MeetingDto
import ir.kitgroup.partnerManagement.core.network.model.OfferDetailDto
import ir.kitgroup.partnerManagement.core.network.model.OfferDto
import ir.kitgroup.partnerManagement.core.network.model.WarningTypeDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationPersonDto
import ir.kitgroup.partnerManagement.core.network.model.ProductDto
import ir.kitgroup.partnerManagement.core.network.model.RegionDto
import ir.kitgroup.partnerManagement.core.network.model.StandTypeDto
import ir.kitgroup.partnerManagement.core.network.model.StatisticDto
import ir.kitgroup.partnerManagement.core.network.model.VisitSubjectDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorDto
import ir.kitgroup.partnerManagement.core.network.model.WarningDto


data class SyncDataResponse(
    val success: Boolean,
    val message: String?,
    val serverDateTime: String?,

    val cities: List<CityDto> = emptyList(),
    val regions: List<RegionDto> = emptyList(),
    val visitSubjects: List<VisitSubjectDto> = emptyList(),
    val standTypes: List<StandTypeDto> = emptyList(),
    val warningTypes: List<WarningTypeDto> = emptyList(),
    val products: List<ProductDto> = emptyList(),
    val offers: List<OfferDto> = emptyList(),
    val offerDetails: List<OfferDetailDto> = emptyList(),
    val visitors: List<VisitorDto> = emptyList(),
    val organizations: List<OrganizationDto> = emptyList(),
    val organizationPersons: List<OrganizationPersonDto> = emptyList(),
    val meetings: List<MeetingDto> = emptyList(),
    val warnings: List<WarningDto> = emptyList(),
    val assignedStands: List<AssignedStandDto> = emptyList(),
    val contracts: List<ContractDto> = emptyList(),
    val assignedSerials: List<AssignedSerialDto> = emptyList(),

    val statistics: StatisticDto? = null
)

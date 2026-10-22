package ir.kitgroup.partnerManagement.core.database.mapper

import ir.kitgroup.partnerManagement.core.database.entity.AssignStandChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferDetailEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.network.model.WarningDto
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.AttachmentFileEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractTerminationEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.StatisticEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.network.model.StandTypeDto
import ir.kitgroup.partnerManagement.core.network.model.CityDto
import ir.kitgroup.partnerManagement.core.network.model.ContractDto
import ir.kitgroup.partnerManagement.core.network.model.MeetingDto
import ir.kitgroup.partnerManagement.core.network.model.OfferDto
import ir.kitgroup.partnerManagement.core.network.model.OfferDetailDto
import ir.kitgroup.partnerManagement.core.network.model.WarningTypeDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationPersonDto
import ir.kitgroup.partnerManagement.core.network.model.ProductDto
import ir.kitgroup.partnerManagement.core.network.model.RegionDto
import ir.kitgroup.partnerManagement.core.network.model.AssignedStandDto
import ir.kitgroup.partnerManagement.core.network.model.VisitSubjectDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.network.model.AssignStandChangeStatusDto
import ir.kitgroup.partnerManagement.core.network.model.AssignedSerialDto
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.network.model.ContractTerminationDto
import ir.kitgroup.partnerManagement.core.network.model.MeetingChangeStatusDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationChangeStatusDto
import ir.kitgroup.partnerManagement.core.network.model.StatisticDto
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus


fun CityDto.toEntity(): CityEntity {
    return CityEntity(
        cityId = this.cityId,
        name = this.name,
        stateCode = this.stateCode
    )
}

fun RegionDto.toEntity(): RegionEntity {
    return RegionEntity(
        regionId = regionId,
        name = name,
        cityId = cityId,
        stateCode = this.stateCode
    )
}

fun VisitSubjectDto.toEntity(): VisitSubjectEntity {
    return VisitSubjectEntity(
        subjectVisitId = subjectVisitId,
        title = title,
        stateCode = this.stateCode
    )
}


fun StandTypeDto.toEntity(): StandTypeEntity {
    return StandTypeEntity(
        standTypeId = standTypeId,
        title = title,
        code = code,
        description = description,
        displayTypeName = displayTypeName,
        installationTypeName = installationTypeName,
        standTypeName = standTypeName,
        displayType = displayType,
        installationType = installationType,
        standType = standType,
        stateCode = this.stateCode
    )
}


fun WarningTypeDto.toEntity(): WarningTypeEntity {
    return WarningTypeEntity(
        warningTypeId = warningTypeId,
        title = title,
        code = code,
        score = score,
        stateCode = this.stateCode
    )
}

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        productId = productId,
        name = name,
        code = code,
        price = price,
        priceExternal = priceExternal,
        itemType = itemType,
        itemTypeName = itemTypeName,
        isCommissionable = isCommissionable,
        stateCode = stateCode
    )
}


fun OfferDto.toEntity(): OfferEntity {
    return OfferEntity(
        offerId = offerId,
        title = title,
        code = code,
        fromDate = fromDate,
        toDate = toDate,
        statusOffer = statusOffer,
        statusOfferName = statusOfferName,
        stateCode = stateCode,
        modifiedOn = modifiedOn,
    )
}

fun OfferDetailDto.toEntity(): OfferDetailEntity {
    return OfferDetailEntity(
        offerDetailId = offerDetailId,
        offerId = offerId,
        productId = productId,
        productName = productName,
        discountType = discountType,
        discountTypeName = discountTypeName,
        discountPercent = discountPercent,
        discountAmount = discountAmount,
        commissionType = commissionType,
        commissionTypeName = commissionTypeName,
        commissionPercent = commissionPercent,
        commissionAmount = commissionAmount,
        personCategory = personCategory,
        personCategoryName = personCategoryName,
        gender = gender,
        genderName = genderName,
        status = status,
        statusName = statusName,
        stateCode = stateCode,
        modifiedOn = modifiedOn
    )
}

fun OrganizationDto.toEntity(): OrganizationEntity {
    val finalId = this.organizationId ?: this.serverId ?: java.util.UUID.randomUUID().toString()

    return OrganizationEntity(
        organizationId = finalId,
        serverId = this.organizationId ?: this.serverId,
        activeVisitorId = this.activeVisitorId,
        activeContractId = this.activeContractId,
        name = this.name,
        ownerName = this.ownerName,
        code = this.code,
        nationalId = this.nationalId,
        organizationType = this.organizationType ?: 0,
        organizationTypeName = this.organizationTypeName,
        grade = this.grade ?: 0,
        gradeName = this.gradeName,
        statusGetStand = this.statusGetStand,
        statusGetStandName = this.statusGetStandName,
        level = this.level ?: 0,
        levelName = this.levelName,
        status = this.status ?: 1,
        statusName = this.statusName,
        phone = this.phone,
        landLine = this.landLine,
        mobile = this.mobile,
        address = this.address,
        cityId = this.cityId,
        cityName = this.cityName,
        regionId = this.regionId,
        regionName = this.regionName,
        latitude = this.latitude,
        longitude = this.longitude,
        visitorId = this.visitorId,
        statusExternalCustomer = this.statusExternalCustomer ?: false,
        englishName = this.englishName,
        document = this.document,
        customerCapacity = this.customerCapacity ?: 0,
        email = this.email,
        ticketSaleCountHistory = this.ticketSaleCountHistory ?: 0,
        issuedSerialCount = this.issuedSerialCount ?: 0,
        remainingSerialCount = this.remainingSerialCount ?: 0,
        assignedSerialCount = this.assignedSerialCount ?: 0,
        cancelledSerialCount = this.cancelledSerialCount ?: 0,
        usingSerialCount = this.usingSerialCount ?: 0,
        programingVisitCount = this.programingVisitCount ?: 0,
        visitCount = this.visitCount ?: 0,
        countVisitorActive = this.countVisitorActive ?: 0,
        countStandAssign = this.countStandAssign ?: 0,
        countWarning = this.countWarning ?: 0,
        sumScore = this.sumScore ?: 0,
        deactiveDate = this.deactiveDate,
        reasonDeactive = this.reasonDeactive,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn,
        syncStatus = SyncStatus.SYNCED,
        isLocallyModified = false
    )
}


fun OrganizationEntity.toPendingDto(): OrganizationDto {
    return OrganizationDto(
        organizationId = if (this.syncStatus == SyncStatus.PENDING_INSERT) null else (this.serverId
            ?: this.organizationId),
        serverId = null,
        activeVisitorId = this.activeVisitorId,
        activeContractId = this.activeContractId,
        name = this.name,
        ownerName = this.ownerName,
        code = this.code,
        nationalId = this.nationalId,
        organizationType = this.organizationType ?: 0,
        organizationTypeName = this.organizationTypeName,
        grade = this.grade ?: 0,
        gradeName = this.gradeName,
        statusGetStand = this.statusGetStand,
        statusGetStandName = this.statusGetStandName,
        level = this.level ?: 0,
        levelName = this.levelName,
        status = this.status ?: 1,
        statusName = this.statusName,
        phone = this.phone,
        landLine = this.landLine,
        mobile = this.mobile,
        address = this.address,
        cityId = this.cityId,
        cityName = this.cityName,
        regionId = this.regionId,
        regionName = this.regionName,
        latitude = this.latitude,
        longitude = this.longitude,
        visitorId = this.visitorId,
        statusExternalCustomer = this.statusExternalCustomer ?: false,
        englishName = this.englishName,
        document = this.document,
        customerCapacity = this.customerCapacity ?: 0,
        email = this.email,
        ticketSaleCountHistory = this.ticketSaleCountHistory ?: 0,
        issuedSerialCount = this.issuedSerialCount ?: 0,
        remainingSerialCount = this.remainingSerialCount ?: 0,
        assignedSerialCount = this.assignedSerialCount ?: 0,
        cancelledSerialCount = this.cancelledSerialCount ?: 0,
        usingSerialCount = this.usingSerialCount ?: 0,
        programingVisitCount = this.programingVisitCount ?: 0,
        visitCount = this.visitCount ?: 0,
        countVisitorActive = this.countVisitorActive ?: 0,
        countStandAssign = this.countStandAssign ?: 0,
        countWarning = this.countWarning ?: 0,
        sumScore = this.sumScore ?: 0,
        deactiveDate = this.deactiveDate,
        reasonDeactive = this.reasonDeactive,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn
    )
}

fun OrganizationChangeStatusEntity.toPendingDto(): OrganizationChangeStatusDto {
    return OrganizationChangeStatusDto(
        organizationId = organizationId,
        statusOrg = this.statusOrg,
        deactiveDate = this.deactiveDate,
        reason = this.reason,
        visitDate = this.visitDate,
        subjectVisitId = this.subjectVisitId
    )
}

fun OrganizationPersonDto.toEntity(): OrganizationPersonEntity {
    val finalId = this.orgPersonId ?: this.serverId ?: java.util.UUID.randomUUID().toString()
    return OrganizationPersonEntity(
        orgPersonId = finalId,
        serverId = this.orgPersonId ?: this.serverId,
        organizationId = this.organizationId,
        mainPersonId = this.mainPersonId,
        fullName = this.fullName,
        role = this.role ?: 0,
        roleName = this.roleName,
        mobile = this.mobile,
        phone = this.phone,
        gender = this.gender,
        genderName = this.genderName,
        description = this.description,
        isCommissionEligible = this.isCommissionEligible,
        statusRelation = this.statusRelation,
        statusRelationName = this.statusRelationName,
        remainBalance = this.remainBalance,
        beforeRemain = this.beforeRemain,
        beforeDate = this.beforeDate,
        stateCode = this.stateCode,
        modifiedOn = this.modifiedOn,
        syncStatus = SyncStatus.SYNCED,
        isLocallyModified = false
    )
}

fun OrganizationPersonEntity.toPendingDto(): OrganizationPersonDto {
    return OrganizationPersonDto(
        orgPersonId = if (this.syncStatus == SyncStatus.PENDING_INSERT) null else (this.serverId
            ?: this.orgPersonId),
        serverId = null,
        organizationId = this.organizationId,
        mainPersonId = this.mainPersonId,
        fullName = this.fullName,
        role = this.role ?: 0,
        roleName = this.roleName,
        mobile = this.mobile,
        phone = this.phone,
        gender = this.gender,
        genderName = this.genderName,
        description = this.description,
        isCommissionEligible = this.isCommissionEligible,
        statusRelation = this.statusRelation,
        statusRelationName = this.statusRelationName,
        remainBalance = this.remainBalance,
        beforeRemain = this.beforeRemain,
        beforeDate = this.beforeDate,
        stateCode = this.stateCode,
        modifiedOn = this.modifiedOn
    )
}

fun WarningDto.toEntity(): WarningEntity {
    val finalId = this.warningId ?: this.serverId ?: java.util.UUID.randomUUID().toString()
    return WarningEntity(
        warningId = finalId,
        serverId = this.warningId ?: this.serverId,
        organizationId = this.organizationId,
        organizationName = this.organizationName,
        meetingId = this.meetingId,
        visitorId = this.visitorId,
        visitorName = this.visitorName,
        warningTypeId = this.warningTypeId,
        warningTypeName = this.warningTypeName,
        score = this.score ?: 0,
        description = this.description,
        warningDate = this.warningDate,
        stateCode = this.stateCode ?: 0,
        modifiedOn = modifiedOn,
        syncStatus = SyncStatus.SYNCED,
        isLocallyModified = false
    )
}


fun WarningEntity.toPendingDto(): WarningDto {
    return WarningDto(
        warningId = if (this.syncStatus == SyncStatus.PENDING_INSERT) null else (this.serverId
            ?: this.warningId),
        serverId = null,
        organizationId = this.organizationId,
        organizationName = this.organizationName,
        meetingId = this.meetingId,
        visitorId = this.visitorId,
        visitorName = this.visitorName,
        warningTypeId = this.warningTypeId,
        warningTypeName = this.warningTypeName,
        score = this.score ?: 0,
        description = this.description,
        warningDate = this.warningDate,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn
    )
}


fun ContractDto.toEntity(): ContractEntity {
    val finalId = this.contractId ?: this.serverId ?: java.util.UUID.randomUUID().toString()
    return ContractEntity(
        contractId = finalId,
        serverId = this.contractId ?: this.serverId,
        title = this.title,
        organizationId = this.organizationId,
        contractNumber = this.contractNumber,
        startDate = this.startDate,
        endDate = this.endDate,
        description = this.description,
        settlementPeriodType = this.settlementPeriodType ?: 0,
        settlementPeriodTypeName = this.settlementPeriodTypeName,
        cooperationModel = this.cooperationModel ?: 0,
        cooperationModelName = this.cooperationModelName,
        defaultDiscountPercent = this.defaultDiscountPercent ?: 0.0,
        defaultCommissionPercent = this.defaultCommissionPercent ?: 0.0,
        status = this.status ?: 0,
        statusName = this.statusName,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn,
        syncStatus = SyncStatus.SYNCED,
        isLocallyModified = false
    )
}

fun ContractEntity.toPendingDto(): ContractDto {
    return ContractDto(
        contractId = if (this.syncStatus == SyncStatus.PENDING_INSERT) null else (this.serverId
            ?: this.contractId),
        serverId = null,
        title = this.title,
        organizationId = this.organizationId,
        contractNumber = this.contractNumber,
        startDate = this.startDate,
        endDate = this.endDate,
        description = this.description,
        settlementPeriodType = this.settlementPeriodType ?: 0,
        settlementPeriodTypeName = this.settlementPeriodTypeName,
        cooperationModel = this.cooperationModel ?: 0,
        cooperationModelName = this.cooperationModelName,
        defaultDiscountPercent = this.defaultDiscountPercent ?: 0.0,
        defaultCommissionPercent = this.defaultCommissionPercent ?: 0.0,
        status = this.status ?: 0,
        statusName = this.statusName,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn
    )
}

fun ContractTerminationEntity.toPendingDto(): ContractTerminationDto {
    return ContractTerminationDto(
        contractId = this.contractId,
        organizationId = this.organizationId,
        terminationDate = this.terminationDate
    )
}

fun MeetingDto.toEntity(): MeetingEntity {
    val finalId = this.meetingId ?: this.serverId ?: java.util.UUID.randomUUID().toString()
    return MeetingEntity(
        meetingId = finalId,
        serverId = this.meetingId ?: this.serverId,
        organizationId = this.organizationId,
        organizationName = this.organizationName,
        visitorId = this.visitorId,
        visitorName = this.visitorName,
        subjectVisitId = this.subjectVisitId,
        subjectVisitName = this.subjectVisitName,
        organizationPersonId = this.organizationPersonId,
        organizationPersonName = this.organizationPersonName,
        description = this.description,
        visitDate = this.visitDate,
        visitRealDate = this.visitRealDate,
        visitTime = this.visitTime,
        status = this.status ?: 0,
        statusName = this.statusName,
        type = this.type ?: 0,
        typeName = this.typeName,
        latitude = this.latitude,
        longitude = this.longitude,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn,
        syncStatus = SyncStatus.SYNCED,
        isLocallyModified = false
    )
}


fun MeetingEntity.toPendingDto(): MeetingDto {
    return MeetingDto(
        meetingId = if (this.syncStatus == SyncStatus.PENDING_INSERT) null else (this.serverId
            ?: this.meetingId),
        serverId = null,
        organizationId = this.organizationId,
        organizationName = this.organizationName,
        visitorId = this.visitorId,
        visitorName = this.visitorName,
        subjectVisitId = this.subjectVisitId,
        subjectVisitName = this.subjectVisitName,
        organizationPersonId = this.organizationPersonId,
        organizationPersonName = this.organizationPersonName,
        description = this.description,
        visitDate = this.visitDate,
        visitRealDate = this.visitRealDate,
        visitTime = this.visitTime,
        status = this.status ?: 0,
        statusName = this.statusName,
        type = this.type ?: 0,
        typeName = this.typeName,
        latitude = this.latitude,
        longitude = this.longitude,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn
    )
}


fun MeetingChangeStatusEntity.toPendingDto(): MeetingChangeStatusDto {
    return MeetingChangeStatusDto(
        meetingId = meetingId,
        status = this.status,
        visitRealDate = this.visitRealDate,
        latitude = this.latitude,
        longitude = this.longitude,
        description = this.description
    )
}

fun VisitorDto.toEntity(): VisitorEntity {
    return VisitorEntity(
        visitorId = visitorId,
        name = name,
        code = code,
        mobile = mobile,
        workType = workType,
        workTypeName = workTypeName,
        stateCode = stateCode
    )
}


fun VisitorOrganizationDto.toEntity(): VisitorOrganizationEntity {
    return VisitorOrganizationEntity(
        visitorOrganizationId = orgVisitorId,
        visitorId = visitorId,
        organizationId = organizationId,
        statusRelation = statusRelation,
        statusRelationName = statusRelationName,
        dateStart = dateStart,
        dateEnd = dateEnd,
        name = name,
        status = status
    )
}


fun AssignedSerialDto.toEntity(): AssignedSerialEntity {
    return AssignedSerialEntity(
        serialAssignmentId = this.serialAssignmentId,
        name = this.name,
        organizationId = this.organizationId,
        organizationName = this.organizationName,
        offerId = this.offerId,
        offerName = this.offerName,
        contractId = this.contractId,
        serialPrefix = this.serialPrefix,
        fromSerial = this.fromSerial,
        toSerial = this.toSerial,
        count = this.count,
        assignmentDate = this.assignmentDate,
        contractOfferStatus = this.contractOfferStatus,
        contractOfferStatusName = this.contractOfferStatusName,
        meetingId = this.meetingId,
        lastSerialUsed = this.lastSerialUsed,
        remainingSerialCount = this.remainingSerialCount,
        stateCode = this.stateCode,
        modifiedOn = this.modifiedOn
    )
}


fun AssignedStandDto.toEntity(): AssignedStandEntity {
    val finalId = this.assignedStandId ?: this.serverId ?: java.util.UUID.randomUUID().toString()
    return AssignedStandEntity(
        assignedStandId = finalId,
        serverId = this.assignedStandId ?: this.serverId,
        organizationId = this.organizationId,
        organizationName = this.organizationName,
        standTypeId = this.standTypeId,
        standTypeName = this.standTypeName,
        visitorId = this.visitorId,
        visitorName = this.visitorName,
        description = this.description.orEmpty(),
        assignmentDate = this.assignmentDate,
        plannedReturnDate = this.plannedReturnDate,
        actualReturnDate = this.actualReturnDate,
        deliveryToOrgDate = this.deliveryToOrgDate,
        deliveryToVisitorDate = this.deliveryToVisitorDate,
        count = this.count ?: 1,
        assignmentType = this.assignmentType ?: 0,
        assignmentTypeName = this.assignmentTypeName,
        assignmentMode = this.assignmentMode ?: 0,
        assignmentModeName = this.assignmentModeName,
        meetingId = this.meetingId,
        statusAssign = this.statusAssign,
        statusAssignName = this.statusAssignName,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn,
        syncStatus = SyncStatus.SYNCED,
        isLocallyModified = false
    )
}

fun AssignedStandEntity.toPendingDto(): AssignedStandDto {
    return AssignedStandDto(
        assignedStandId = if (this.syncStatus == SyncStatus.PENDING_INSERT) null else (this.serverId
            ?: this.assignedStandId),
        serverId = null,
        organizationId = this.organizationId,
        organizationName = this.organizationName,
        standTypeId = this.standTypeId,
        standTypeName = this.standTypeName,
        visitorId = this.visitorId,
        visitorName = this.visitorName,
        description = this.description.orEmpty(),
        assignmentDate = this.assignmentDate,
        plannedReturnDate = this.plannedReturnDate,
        actualReturnDate = this.actualReturnDate,
        deliveryToOrgDate = this.deliveryToOrgDate,
        deliveryToVisitorDate = this.deliveryToVisitorDate,
        count = this.count ?: 1,
        assignmentType = this.assignmentType ?: 0,
        assignmentTypeName = this.assignmentTypeName,
        assignmentMode = this.assignmentMode ?: 0,
        assignmentModeName = this.assignmentModeName,
        meetingId = this.meetingId,
        statusAssign = this.statusAssign,
        statusAssignName = this.statusAssignName,
        stateCode = this.stateCode ?: 0,
        modifiedOn = this.modifiedOn
    )
}

fun AssignStandChangeStatusEntity.toPendingDto(): AssignStandChangeStatusDto {
    return AssignStandChangeStatusDto(
        standAssignmentId = standAssignmentId,
        organizationId = this.organizationId,
        status = this.status,
        actualReturnDate = this.actualReturnDate,
        deliveryToOrgDate = this.deliveryToOrgDate
    )
}

fun StatisticDto.toEntity(): StatisticEntity {
    return StatisticEntity(
        id = 1,
        totalVisits = this.totalVisits,
        totalAssignedStands = this.totalAssignedStands,
        totalAssignedSerials = this.totalAssignedSerials,
        activeContractsCount = this.activeContractsCount,
        totalWarningsRegistered = this.totalWarningsRegistered
    )
}


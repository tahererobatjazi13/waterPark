package ir.kitgroup.partnerManagement.core.database.mapper

import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.AutoNumberSequenceEntity
import ir.kitgroup.partnerManagement.core.database.entity.BaseSettingEntity
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractOfferEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferDetailEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrgAttachmentFileEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.PersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.network.model.WarningDto
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.StatisticEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.entity.TicketIssueEntity
import ir.kitgroup.partnerManagement.core.database.entity.TicketIssueLineEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.WalletTransactionEntity
import ir.kitgroup.partnerManagement.core.network.model.StandTypeDto
import ir.kitgroup.partnerManagement.core.network.model.AutoNumberSequenceDto
import ir.kitgroup.partnerManagement.core.network.model.BaseSettingDto
import ir.kitgroup.partnerManagement.core.network.model.CityDto
import ir.kitgroup.partnerManagement.core.network.model.ContractDto
import ir.kitgroup.partnerManagement.core.network.model.ContractOfferDto
import ir.kitgroup.partnerManagement.core.network.model.MeetingDto
import ir.kitgroup.partnerManagement.core.network.model.OfferDto
import ir.kitgroup.partnerManagement.core.network.model.OfferDetailDto
import ir.kitgroup.partnerManagement.core.network.model.OrgAttachmentFileDto
import ir.kitgroup.partnerManagement.core.network.model.WarningTypeDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationPersonDto
import ir.kitgroup.partnerManagement.core.network.model.PersonDto
import ir.kitgroup.partnerManagement.core.network.model.ProductDto
import ir.kitgroup.partnerManagement.core.network.model.RegionDto
import ir.kitgroup.partnerManagement.core.network.model.AssignedStandDto
import ir.kitgroup.partnerManagement.core.network.model.VisitSubjectDto
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueDto
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.WalletTransactionDto
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.network.model.AssignedSerialDto
import ir.kitgroup.partnerManagement.core.network.model.StatisticDto

fun BaseSettingDto.toEntity(): BaseSettingEntity {
    return BaseSettingEntity(
        baseSettingId = baseSettingId,
        name = name,
        key = key,
        value = value
    )
}

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
        stateCode = stateCode
    )
}


fun OrganizationDto.toEntity(): OrganizationEntity {
    return OrganizationEntity(
        organizationId = this.organizationId,
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
    )
}

fun OrganizationPersonDto.toEntity(): OrganizationPersonEntity {
    return OrganizationPersonEntity(
        orgPersonId = this.orgPersonId,
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
        stateCode = this.stateCode
    )
}

fun WarningDto.toEntity(): WarningEntity {
    return WarningEntity(
        warningId = this.warningId,
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
        stateCode = this.stateCode ?: 0
    )
}

fun OrgAttachmentFileDto.toEntity(): OrgAttachmentFileEntity {
    return OrgAttachmentFileEntity(
        orgAttachmentFileId = orgAttachmentFileId,
        name = name,
        organizationId = organizationId,
        fileType = fileType,
        originalFileName = originalFileName,
        storedFileName = storedFileName,
        relativePath = relativePath,
        fileExtension = fileExtension,
        mimeType = mimeType,
        fileSize = fileSize,
        fileUrl = fileUrl,
        fileHash = fileHash,
        storageStatus = storageStatus,
        description = description,
        sourceCreate = sourceCreate,
        meetingId = meetingId
    )
}


fun PersonDto.toEntity(): PersonEntity {
    return PersonEntity(
        personId = personId,
        name = name,
        description = description,
        gender = gender,
        mobile = mobile,
        phone1 = phone1,
        status = status
    )
}


fun AutoNumberSequenceDto.toEntity(): AutoNumberSequenceEntity {
    return AutoNumberSequenceEntity(
        autoNumberSequenceId = autoNumberSequenceId,
        name = name,
        currentNumber = currentNumber,
        prefix = prefix,
        format = format,
        step = step,
        recreationCenterId = recreationCenterId
    )
}

fun ContractDto.toEntity(): ContractEntity {
    return ContractEntity(
        contractId = this.contractId,
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
    )
}

fun ContractOfferDto.toEntity(): ContractOfferEntity {
    return ContractOfferEntity(
        contractOfferId = contractOfferId,
        name = name,
        contractId = contractId,
        countSerial = countSerial,
        lastSerialUsed = lastSerialUsed,
        serialFrom = serialFrom,
        serialPrefix = serialPrefix,
        serialTo = serialTo,
        offerTicketPlanId = offerTicketPlanId,
        contractOfferStatus = contractOfferStatus,
        organizationId = organizationId,
        remainSerialCount = remainSerialCount,
        meetingId = meetingId
    )
}

fun MeetingDto.toEntity(): MeetingEntity {
    return MeetingEntity(
        meetingId = this.meetingId,
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
    )
}


fun TicketIssueDto.toEntity(): TicketIssueEntity {
    return TicketIssueEntity(
        ticketIssueId = ticketIssueId,
        name = name,
        count = count,
        customerMobile = customerMobile,
        customerName = customerName,
        description = description,
        issuedOn = issuedOn,
        organizationPersonId = organizationPersonId,
        customerType = customerType,
        sumPrice = sumPrice,
        sumDiscountPrice = sumDiscountPrice,
        organizationId = organizationId,
        sourceCreate = sourceCreate
    )
}

fun TicketIssueLineDto.toEntity(): TicketIssueLineEntity {
    return TicketIssueLineEntity(
        ticketIssueLineId = ticketIssueLineId,
        name = name,
        consumedAgeCategory = consumedAgeCategory,
        consumedDate = consumedDate,
        consumedGender = consumedGender,
        discountPrice = discountPrice,
        gender = gender,
        personCategory = personCategory,
        productServiceId = productServiceId,
        serialNumber = serialNumber,
        ticketIssueId = ticketIssueId,
        ticketStatus = ticketStatus,
        unitPrice = unitPrice,
        count = count,
        realCount = realCount,
        contractOfferId = contractOfferId,
        sumPrice = sumPrice,
        finalProductServiceId = finalProductServiceId,
        finalCustomerType = finalCustomerType,
        usageDate = usageDate,
        useTime = useTime,
        visitorId = visitorId,
        recreationCenterPartId = recreationCenterPartId,
        totalCommission = totalCommission,
        statusUsage = statusUsage,
        approvedBy = approvedBy,
        approvedDate = approvedDate
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
        visitorOrganizationId = visitorOrganizationId,
        name = name,
        dateEnd = dateEnd,
        dateStart = dateStart,
        organizationId = organizationId,
        visitorId = visitorId,
        statusRelation = statusRelation
    )
}


fun WalletTransactionDto.toEntity(): WalletTransactionEntity {
    return WalletTransactionEntity(
        walletTransactionId = walletTransactionId,
        name = name,
        amount = amount,
        createSource = createSource,
        description = description,
        type = type,
        organizationPersonId = organizationPersonId,
        ticketIssueLineId = ticketIssueLineId
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
        stateCode = this.stateCode
    )
}


fun AssignedStandDto.toEntity(): AssignedStandEntity {
    return AssignedStandEntity(
        assignedStandId = this.assignedStandId,
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
        stateCode = this.stateCode ?: 0
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


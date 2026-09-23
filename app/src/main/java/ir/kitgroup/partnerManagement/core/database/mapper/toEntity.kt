package ir.kitgroup.partnerManagement.core.database.mapper

import ir.kitgroup.partnerManagement.core.database.entity.AdvertisingStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.AutoNumberSequenceEntity
import ir.kitgroup.partnerManagement.core.database.entity.BaseSettingEntity
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractOfferEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanLineEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrgAttachmentFileEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningsEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.PersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.ProductServiceEntity
import ir.kitgroup.partnerManagement.core.network.model.OrganizationWarningDto
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.StandAssignmentEntity
import ir.kitgroup.partnerManagement.core.database.entity.SubjectVisitEntity
import ir.kitgroup.partnerManagement.core.database.entity.TicketIssueEntity
import ir.kitgroup.partnerManagement.core.database.entity.TicketIssueLineEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.WalletTransactionEntity
import ir.kitgroup.partnerManagement.core.network.model.AdvertisingStandDto
import ir.kitgroup.partnerManagement.core.network.model.AutoNumberSequenceDto
import ir.kitgroup.partnerManagement.core.network.model.BaseSettingDto
import ir.kitgroup.partnerManagement.core.network.model.CityDto
import ir.kitgroup.partnerManagement.core.network.model.ContractDto
import ir.kitgroup.partnerManagement.core.network.model.ContractOfferDto
import ir.kitgroup.partnerManagement.core.network.model.MeetingDto
import ir.kitgroup.partnerManagement.core.network.model.OfferTicketPlanDto
import ir.kitgroup.partnerManagement.core.network.model.OfferTicketPlanLineDto
import ir.kitgroup.partnerManagement.core.network.model.OrgAttachmentFileDto
import ir.kitgroup.partnerManagement.core.network.model.WarningsDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationPersonDto
import ir.kitgroup.partnerManagement.core.network.model.PersonDto
import ir.kitgroup.partnerManagement.core.network.model.ProductServiceDto
import ir.kitgroup.partnerManagement.core.network.model.RegionDto
import ir.kitgroup.partnerManagement.core.network.model.StandAssignmentDto
import ir.kitgroup.partnerManagement.core.network.model.SubjectVisitDto
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueDto
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.WalletTransactionDto
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationWarningEntity

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
    )
}

fun RegionDto.toEntity(): RegionEntity {
    return RegionEntity(
        regionId = regionId,
        name = name,
        cityId = cityId,
        recreationCenterId = recreationCenterId
    )
}

fun OrganizationDto.toEntity(): OrganizationEntity {
    return OrganizationEntity(
        organizationId = organizationId,
        name = name,
        address = address,
        cityId = cityId,
        code = code,
        customerCapacity = customerCapacity,
        document = document,
        email = email,
        englishName = englishName,
        grade = grade,
        landLine = landLine,
        latitude = latitude,
        longitude = longitude,
        mobile = mobile,
        nationalId = nationalId,
        organizationType = organizationType,
        ownerName = ownerName,
        phone = phone,
        regionId = regionId,
        status = status,
        ticketSaleCountHistory = ticketSaleCountHistory,
        statusExternalCustomer = statusExternalCustomer,
        level = level,
        statusGetStand = statusGetStand,
        reasonDeactive = reasonDeactive,
        countWarning = countWarning,
        sumScore = sumScore,
        recreationCenterId = recreationCenterId,
        sourceCreate = sourceCreate,
        recalculateStatistics = recalculateStatistics,
        countVisitorActive = countVisitorActive,
        countStandAssign = countStandAssign,
        assignedSerialCount = assignedSerialCount,
        cancelledSerialCount = cancelledSerialCount,
        remainingSerialCount = remainingSerialCount,
        issuedSerialCount = issuedSerialCount,
        visitCount = visitCount,
        visitorId = visitorId,
        visitDate = visitDate,
        programmingVisitCount = programmingVisitCount,
        usingSerialCount = usingSerialCount,
        deactiveDate = deactiveDate,
        subjectVisitId = subjectVisitId
    )
}


fun OrganizationPersonDto.toEntity(): OrganizationPersonEntity {
    return OrganizationPersonEntity(
        organizationPersonId = organizationPersonId,
        name = name,
        description = description,
        isCommissionEligible = isCommissionEligible,
        organizationId = organizationId,
        personId = personId,
        role = role,
        beforeDate = beforeDate,
        beforeRemain = beforeRemain,
        remainBalance = remainBalance,
        appPassword = appPassword,
        updateRemainPriceWallet = updateRemainPriceWallet,
        finalDate = finalDate,
        statusRelation = statusRelation
    )
}

fun OrganizationWarningDto.toEntity(): OrganizationWarningEntity {
    return OrganizationWarningEntity(
        organizationWarningId = organizationWarningId,
        name = name,
        organizationId = organizationId,
        warningId = warningId,
        visitorId = visitorId,
        dateWarning = dateWarning,
        description = description,
        score = score,
        meetingId = meetingId
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

fun ProductServiceDto.toEntity(): ProductServiceEntity {
    return ProductServiceEntity(
        productServiceId = productServiceId,
        name = name,
        basePrice = basePrice,
        code = code,
        isCommissionable = isCommissionable,
        itemType = itemType,
        recreationCenterId = recreationCenterId,
        unit = unit,
        basePriceExternal = basePriceExternal
    )
}


fun WarningsDto.toEntity(): WarningsEntity {
    return WarningsEntity(
        warningsId = warningsId,
        name = name,
        score = score,
        recreationCenterId = recreationCenterId,
        code = code
    )
}

fun SubjectVisitDto.toEntity(): SubjectVisitEntity {
    return SubjectVisitEntity(
        subjectVisitId = subjectVisitId,
        name = name,
        recreationCenterId = recreationCenterId
    )
}


fun AdvertisingStandDto.toEntity(): AdvertisingStandEntity {
    return AdvertisingStandEntity(
        advertisingStandId = advertisingStandId,
        name = name,
        code = code,
        description = description,
        displayType = displayType,
        installationType = installationType,
        recreationCenterId = recreationCenterId,
        standType = standType
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
        contractId = contractId,
        name = name,
        contractNumber = contractNumber,
        cooperationModel = cooperationModel,
        defaultCommissionPercent = defaultCommissionPercent,
        defaultDiscountPercent = defaultDiscountPercent,
        description = description,
        endDate = endDate,
        organizationId = organizationId,
        settlementPeriodType = settlementPeriodType,
        startDate = startDate,
        contractStatus = contractStatus
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
        meetingId = meetingId,
        name = name,
        description = description,
        organizationId = organizationId,
        status = status,
        type = type,
        visitDate = visitDate,
        visitorId = visitorId,
        visitRealDate = visitRealDate,
        subjectVisitId = subjectVisitId,
        visitTime = visitTime,
        personId = personId,
        longitude = longitude,
        latitude = latitude
    )
}

fun OfferTicketPlanDto.toEntity(): OfferTicketPlanEntity {
    return OfferTicketPlanEntity(
        offerTicketPlanId = offerTicketPlanId,
        name = name,
        code = code,
        recreationCenterId = recreationCenterId,
        validFromDate = validFromDate,
        validToDate = validToDate,
        status = status
    )
}


fun OfferTicketPlanLineDto.toEntity(): OfferTicketPlanLineEntity {
    return OfferTicketPlanLineEntity(
        offerTicketPlanLineId = offerTicketPlanLineId,
        name = name,
        commissionAmount = commissionAmount,
        commissionPercent = commissionPercent,
        commissionType = commissionType,
        discountAmount = discountAmount,
        discountPercent = discountPercent,
        discountType = discountType,
        gender = gender,
        offerTicketPlanId = offerTicketPlanId,
        personCategory = personCategory,
        productServiceId = productServiceId,
        status = status
    )
}


fun StandAssignmentDto.toEntity(): StandAssignmentEntity {
    return StandAssignmentEntity(
        standAssignmentId = standAssignmentId,
        name = name,
        actualReturnDate = actualReturnDate,
        advertisingStandId = advertisingStandId,
        assignmentDate = assignmentDate,
        assignmentMode = assignmentMode,
        assignmentType = assignmentType,
        deliveryToOrgDate = deliveryToOrgDate,
        deliveryToVisitorDate = deliveryToVisitorDate,
        organizationId = organizationId,
        plannedReturnDate = plannedReturnDate,
        status = status,
        visitorId = visitorId,
        count = count,
        description = description,
        meetingId = meetingId
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
        description = description,
        mobile = mobile,
        recreationCenterId = recreationCenterId,
        workType = workType,
        systemUserId = systemUserId,
        appPassword = appPassword,
        recreationCenterPartId = recreationCenterPartId,
        countActiveOrg = countActiveOrg,
        deviceModel = deviceModel,
        androidVersion = androidVersion,
        appVersion = appVersion,
        imei = imei,
        lastSyncDate = lastSyncDate
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
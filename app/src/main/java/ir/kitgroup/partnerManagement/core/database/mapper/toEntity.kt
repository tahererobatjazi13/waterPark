package ir.kitgroup.partnerManagement.core.database.mapper


import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningsEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.SubjectVisitEntity
import ir.kitgroup.partnerManagement.core.network.model.CityDto
import ir.kitgroup.partnerManagement.core.network.model.WarningsDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.RegionDto
import ir.kitgroup.partnerManagement.core.network.model.SubjectVisitDto

fun CityDto.toEntity(): CityEntity {
    return CityEntity(
        cityId = this.cityId,
        name = this.name,
    )
}

fun RegionDto.toEntity(): RegionEntity {
    return RegionEntity(
        regionId = this.regionId,
        name = this.name,
        cityId = this.cityId,
        receationCenterId = this.receationCenterId
    )
}


fun WarningsDto.toEntity(): WarningsEntity {
    return WarningsEntity(
        warningsId = this.warningsId,
        name = this.name,
        score = this.score,
        receationCenterId = this.receationCenterId,
        code = this.code
    )
}

fun SubjectVisitDto.toEntity(): SubjectVisitEntity {
    return SubjectVisitEntity(
        subjectVisitId = this.subjectVisitId,
        name = this.name,
        isActive = this.isActive
    )
}

fun OrganizationDto.toEntity(): OrganizationEntity {
    return OrganizationEntity(
        id = this.id,
        name = this.name,
        cityId = this.cityId,
        regionId = this.regionId,
        address = this.address,
        phone = this.phone,
        latitude = this.latitude,
        longitude = this.longitude,
        grade = this.grade,
        status = this.status
    )
}



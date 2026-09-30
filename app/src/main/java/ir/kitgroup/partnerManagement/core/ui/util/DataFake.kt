package ir.kitgroup.partnerManagement.core.ui.util

import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity

val demoVisitorOrganizations = listOf(
    VisitorOrganizationEntity(
        visitorOrganizationId = "vo-001",
        name = "امیر حسین رضایی",
        dateStart = "1405/02/05",
        dateEnd = "1405/02/15",
        statusRelation = 0,
        visitorId = "visitor-001",
        organizationId = "org-ghasr-talaee"
    ),
    VisitorOrganizationEntity(
        visitorOrganizationId = "vo-002",
        name = "سارا محمدی",
        dateStart = "1405/02/05",
        dateEnd = "1405/02/15",
        statusRelation = 1,
        visitorId = "visitor-002",
        organizationId = "org-ghasr-talaee"
    ),
    VisitorOrganizationEntity(
        visitorOrganizationId = "vo-003",
        name = "علی جعفری",
        dateStart = "1405/02/05",
        dateEnd = "1405/02/15",
        statusRelation = 2,
        visitorId = "visitor-003",
        organizationId = "org-darvishi"
    ),
    VisitorOrganizationEntity(
        visitorOrganizationId = "vo-004",
        name = "مهین محمدی",
        dateStart = "1405/02/05",
        dateEnd = "1405/02/15",
        statusRelation = 2,
        visitorId = "visitor-004",
        organizationId = "org-darvishi"
    ),
    VisitorOrganizationEntity(
        visitorOrganizationId = "vo-005",
        name = "جعفر امری",
        dateStart = "1405/02/05",
        dateEnd = "1405/02/15",
        statusRelation = 0,
        visitorId = "visitor-005",
        organizationId = "org-ghasr-talaee"
    )
)

val demoAdvertisingStandList = listOf(
    StandTypeEntity(
        standTypeId = "1",
        title = "استند رومیزی",
        code = "TABLE_STAND",
        description = "",
        displayTypeName = "چاپی",
        installationTypeName = "ایستاده",
        standTypeName = "استند بروشر",
        displayType = 5,
        installationType = 5,
        standType = 1,
        stateCode = 0

    ),
    StandTypeEntity(
        standTypeId = "2",
        title = "استند رومیزی",
        code = "TABLE_STAND",
        description = "",
        displayTypeName = "چاپی",
        installationTypeName = "ایستاده",
        standTypeName = "استند بروشر",
        displayType = 5,
        installationType = 5,
        standType = 1,
        stateCode = 0
    )

)





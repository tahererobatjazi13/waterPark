package ir.kitgroup.partnerManagement.core.ui.util


import ir.kitgroup.partnerManagement.core.database.entity.AdvertisingStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanLineEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationWarningEntity
import ir.kitgroup.partnerManagement.core.database.entity.StandAssignmentEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity


val demoOrganizations = listOf(

    OrganizationEntity(
        organizationId = "org-001",

        name = "هتل پارسیان آزادی",

        address = "بلوار وکیل‌آباد",

        cityId = "city-mashhad",

        code = "ORG-001",

        customerCapacity = 500,

        document = "مجوز فعالیت 1405",

        email = "info@parsianazadi.ir",

        englishName = "Parsian Azadi Hotel",

        grade = 5,

        landLine = "05136000001",

        latitude = 36.2845,

        longitude = 59.5892,

        mobile = "09151234567",

        nationalId = "10123456789",

        organizationType = 1,

        ownerName = "علی یوسفی",

        phone = "05136000001",

        regionId = "region-02",

        status = 1,

        ticketSaleCountHistory = 1250,

        statusExternalCustomer = true,

        level = 1,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 1,

        sumScore = 92,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 4,

        countStandAssign = 5,

        assignedSerialCount = 120,

        cancelledSerialCount = 3,

        remainingSerialCount = 25,

        issuedSerialCount = 145,

        visitCount = 18,

        visitorId = "visitor-001",

        visitDate = "1405/06/20",

        programmingVisitCount = 20,

        usingSerialCount = 120,

        deactiveDate = null,

        subjectVisitId = "subject-001"
    ),

    OrganizationEntity(
        organizationId = "org-002",

        name = "سازمان پالاس",

        address = "قاسم‌آباد",

        cityId = "city-mashhad",

        code = "ORG-002",

        customerCapacity = 300,

        document = "مجوز تجاری",

        email = "info@palace.ir",

        englishName = "Palace Organization",

        grade = 4,

        landLine = "05136000002",

        latitude = 36.3562,

        longitude = 59.5084,

        mobile = "09151234568",

        nationalId = "10123456790",

        organizationType = 2,

        ownerName = "محمد رضایی",

        phone = "05136000002",

        regionId = "region-02",

        status = 2,

        ticketSaleCountHistory = 860,

        statusExternalCustomer = true,

        level = 2,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 0,

        sumScore = 84,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 3,

        countStandAssign = 4,

        assignedSerialCount = 80,

        cancelledSerialCount = 2,

        remainingSerialCount = 18,

        issuedSerialCount = 98,

        visitCount = 12,

        visitorId = "visitor-002",

        visitDate = "1405/06/18",

        programmingVisitCount = 14,

        usingSerialCount = 80,

        deactiveDate = null,

        subjectVisitId = "subject-002"
    ),

    OrganizationEntity(
        organizationId = "org-003",

        name = "سازمان نوید",

        address = "بلوار پیروزی",

        cityId = "city-mashhad",

        code = "ORG-003",

        customerCapacity = 200,

        document = "قرارداد همکاری",

        email = "info@navid.ir",

        englishName = "Navid Organization",

        grade = 3,

        landLine = "05136000003",

        latitude = 36.3015,

        longitude = 59.5289,

        mobile = "09151234569",

        nationalId = "10123456791",

        organizationType = 2,

        ownerName = "حسین احمدی",

        phone = "05136000003",

        regionId = "region-02",

        status = 3,

        ticketSaleCountHistory = 540,

        statusExternalCustomer = true,

        level = 2,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 2,

        sumScore = 76,

        recreationCenterId = "center-001",

        sourceCreate = 2,

        recalculateStatistics = true,

        countVisitorActive = 3,

        countStandAssign = 3,

        assignedSerialCount = 55,

        cancelledSerialCount = 4,

        remainingSerialCount = 10,

        issuedSerialCount = 65,

        visitCount = 9,

        visitorId = "visitor-003",

        visitDate = "1405/06/15",

        programmingVisitCount = 10,

        usingSerialCount = 55,

        deactiveDate = null,

        subjectVisitId = "subject-003"
    ),

    OrganizationEntity(
        organizationId = "org-004",

        name = "هتل مرکزی",

        address = "خیابان امام رضا",

        cityId = "city-mashhad",

        code = "ORG-004",

        customerCapacity = 450,

        document = "مجوز هتل",

        email = "info@markazi.ir",

        englishName = "Markazi Hotel",

        grade = 5,

        landLine = "05136000004",

        latitude = 36.2820,

        longitude = 59.6190,

        mobile = "09151234570",

        nationalId = "10123456792",

        organizationType = 1,

        ownerName = "رضا کریمی",

        phone = "05136000004",

        regionId = "region-02",

        status = 4,

        ticketSaleCountHistory = 1420,

        statusExternalCustomer = true,

        level = 1,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 0,

        sumScore = 95,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 5,

        countStandAssign = 5,

        assignedSerialCount = 160,

        cancelledSerialCount = 5,

        remainingSerialCount = 32,

        issuedSerialCount = 192,

        visitCount = 24,

        visitorId = "visitor-004",

        visitDate = "1405/06/19",

        programmingVisitCount = 25,

        usingSerialCount = 160,

        deactiveDate = null,

        subjectVisitId = "subject-004"
    ),

    OrganizationEntity(
        organizationId = "org-005",

        name = "کیوسک اطلس",

        address = "پارک کوهسنگی",

        cityId = "city-mashhad",

        code = "ORG-005",

        customerCapacity = 100,

        document = "مجوز غرفه",

        email = "atlas@sample.ir",

        englishName = "Atlas Kiosk",

        grade = 3,

        landLine = "05136000005",

        latitude = 36.2736,

        longitude = 59.5694,

        mobile = "09151234571",

        nationalId = "10123456793",

        organizationType = 3,

        ownerName = "مهدی محمدی",

        phone = "05136000005",

        regionId = "region-02",

        status = 1,

        ticketSaleCountHistory = 320,

        statusExternalCustomer = true,

        level = 3,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 1,

        sumScore = 68,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 2,

        countStandAssign = 3,

        assignedSerialCount = 30,

        cancelledSerialCount = 1,

        remainingSerialCount = 7,

        issuedSerialCount = 37,

        visitCount = 6,

        visitorId = "visitor-005",

        visitDate = "1405/06/12",

        programmingVisitCount = 7,

        usingSerialCount = 30,

        deactiveDate = null,

        subjectVisitId = "subject-005"
    ),

    OrganizationEntity(
        organizationId = "org-006",

        name = "هتل الماس",

        address = "خیابان پاستور",

        cityId = "city-mashhad",

        code = "ORG-006",

        customerCapacity = 250,

        document = "مجوز هتل",

        email = "info@almas.ir",

        englishName = "Almas Hotel",

        grade = 2,

        landLine = "05136000006",

        latitude = 36.2994,

        longitude = 59.5772,

        mobile = "09151234572",

        nationalId = "10123456794",

        organizationType = 1,

        ownerName = "سعید حسینی",

        phone = "05136000006",

        regionId = "region-02",

        status = 1,

        ticketSaleCountHistory = 410,

        statusExternalCustomer = true,

        level = 2,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 0,

        sumScore = 73,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 2,

        countStandAssign = 2,

        assignedSerialCount = 42,

        cancelledSerialCount = 2,

        remainingSerialCount = 8,

        issuedSerialCount = 50,

        visitCount = 7,

        visitorId = "visitor-006",

        visitDate = "1405/06/10",

        programmingVisitCount = 8,

        usingSerialCount = 42,

        deactiveDate = null,

        subjectVisitId = "subject-006"
    ),

    OrganizationEntity(
        organizationId = "org-007",

        name = "هتل وفا",

        address = "بلوار وکیل‌آباد",

        cityId = "city-mashhad",

        code = "ORG-007",

        customerCapacity = 280,

        document = "قرارداد همکاری",

        email = "info@vafa.ir",

        englishName = "Vafa Hotel",

        grade = 3,

        landLine = "05136000007",

        latitude = 36.3312,

        longitude = 59.4851,

        mobile = "09151234573",

        nationalId = "10123456795",

        organizationType = 1,

        ownerName = "امیر موسوی",

        phone = "05136000007",

        regionId = "region-02",

        status = 5,

        ticketSaleCountHistory = 620,

        statusExternalCustomer = true,

        level = 2,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 1,

        sumScore = 81,

        recreationCenterId = "center-001",

        sourceCreate = 2,

        recalculateStatistics = false,

        countVisitorActive = 3,

        countStandAssign = 3,

        assignedSerialCount = 61,

        cancelledSerialCount = 3,

        remainingSerialCount = 12,

        issuedSerialCount = 73,

        visitCount = 11,

        visitorId = "visitor-007",

        visitDate = "1405/06/08",

        programmingVisitCount = 12,

        usingSerialCount = 61,

        deactiveDate = null,

        subjectVisitId = "subject-007"
    ),

    OrganizationEntity(
        organizationId = "org-008",

        name = "سازمان مهندسی",

        address = "خیابان فاطمی",

        cityId = "city-mashhad",

        code = "ORG-008",

        customerCapacity = 350,

        document = "قرارداد سازمانی",

        email = "info@mohandesi.ir",

        englishName = "Engineering Organization",

        grade = 4,

        landLine = "05136000008",

        latitude = 36.3078,

        longitude = 59.5935,

        mobile = "09151234574",

        nationalId = "10123456796",

        organizationType = 2,

        ownerName = "حمید رضایی",

        phone = "05136000008",

        regionId = "region-02",

        status = 1,

        ticketSaleCountHistory = 930,

        statusExternalCustomer = true,

        level = 1,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 0,

        sumScore = 88,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 4,

        countStandAssign = 4,

        assignedSerialCount = 95,

        cancelledSerialCount = 2,

        remainingSerialCount = 20,

        issuedSerialCount = 115,

        visitCount = 16,

        visitorId = "visitor-008",

        visitDate = "1405/06/17",

        programmingVisitCount = 18,

        usingSerialCount = 95,

        deactiveDate = null,

        subjectVisitId = "subject-008"
    ),

    OrganizationEntity(
        organizationId = "org-009",

        name = "هتل امیر",

        address = "خیابان رضاییه",

        cityId = "city-mashhad",

        code = "ORG-009",

        customerCapacity = 180,

        document = "مجوز هتل",

        email = "info@amir.ir",

        englishName = "Amir Hotel",

        grade = 2,

        landLine = "05136000009",

        latitude = 36.2768,

        longitude = 59.6385,

        mobile = "09151234575",

        nationalId = "10123456797",

        organizationType = 1,

        ownerName = "مجتبی اکبری",

        phone = "05136000009",

        regionId = "region-02",

        status = 2,

        ticketSaleCountHistory = 290,

        statusExternalCustomer = true,

        level = 3,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 2,

        sumScore = 64,

        recreationCenterId = "center-001",

        sourceCreate = 2,

        recalculateStatistics = true,

        countVisitorActive = 2,

        countStandAssign = 2,

        assignedSerialCount = 28,

        cancelledSerialCount = 3,

        remainingSerialCount = 6,

        issuedSerialCount = 34,

        visitCount = 5,

        visitorId = "visitor-009",

        visitDate = "1405/06/05",

        programmingVisitCount = 6,

        usingSerialCount = 28,

        deactiveDate = null,

        subjectVisitId = "subject-009"
    ),

    OrganizationEntity(
        organizationId = "org-010",

        name = "آپارتمان ملل",

        address = "خیابان ستاری",

        cityId = "city-mashhad",

        code = "ORG-010",

        customerCapacity = 150,

        document = "قرارداد همکاری",

        email = "info@melal.ir",

        englishName = "Melal Apartment",

        grade = 3,

        landLine = "05136000010",

        latitude = 36.3421,

        longitude = 59.5208,

        mobile = "09151234576",

        nationalId = "10123456798",

        organizationType = 2,

        ownerName = "علی اکبری",

        phone = "05136000010",

        regionId = "region-02",

        status = 3,

        ticketSaleCountHistory = 470,

        statusExternalCustomer = true,

        level = 2,

        statusGetStand = true,

        reasonDeactive = null,

        countWarning = 0,

        sumScore = 79,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 3,

        countStandAssign = 3,

        assignedSerialCount = 46,

        cancelledSerialCount = 1,

        remainingSerialCount = 9,

        issuedSerialCount = 55,

        visitCount = 8,

        visitorId = "visitor-010",

        visitDate = "1405/06/14",

        programmingVisitCount = 9,

        usingSerialCount = 46,

        deactiveDate = null,

        subjectVisitId = "subject-010"
    ),

    OrganizationEntity(
        organizationId = "org-011",

        name = "مهمانسرا اسپیناس",

        address = "بلوار مرکزی",

        cityId = "city-mashhad",

        code = "ORG-011",

        customerCapacity = 220,

        document = "قرارداد منقضی",

        email = "info@espinas.ir",

        englishName = "Espinas Guest House",

        grade = 5,

        landLine = "05136000011",

        latitude = 36.3051,

        longitude = 59.6059,

        mobile = "09151234577",

        nationalId = "10123456799",

        organizationType = 1,

        ownerName = "رضا مرادی",

        phone = "05136000011",

        regionId = "region-02",

        status = 2,

        ticketSaleCountHistory = 730,

        statusExternalCustomer = false,

        level = 2,

        statusGetStand = false,

        reasonDeactive = "اتمام قرارداد همکاری",

        countWarning = 4,

        sumScore = 51,

        recreationCenterId = "center-001",

        sourceCreate = 1,

        recalculateStatistics = false,

        countVisitorActive = 0,

        countStandAssign = 0,

        assignedSerialCount = 60,

        cancelledSerialCount = 12,

        remainingSerialCount = 0,

        issuedSerialCount = 60,

        visitCount = 13,

        visitorId = "visitor-011",

        visitDate = "1405/05/20",

        programmingVisitCount = 13,

        usingSerialCount = 60,

        deactiveDate = "1405/06/01",

        subjectVisitId = "subject-011"
    ),

    OrganizationEntity(
        organizationId = "org-012",
        name = "چالیدره",
        address = "طرقبه",
        cityId = "city-mashhad",
        code = "ORG-012",
        customerCapacity = 600,
        document = "قرارداد پایان‌یافته",
        email = "info@chalidreh.ir",
        englishName = "Chalidarreh",
        grade = 2,
        landLine = "05136000012",
        latitude = 36.3198,
        longitude = 59.3482,
        mobile = "09151234578",
        nationalId = "10123456800",
        organizationType = 3,
        ownerName = "مهدی کاظمی",
        phone = "05136000012",
        regionId = "region-02",
        status = 0,
        ticketSaleCountHistory = 380,
        statusExternalCustomer = false,
        level = 3,
        statusGetStand = false,
        reasonDeactive = "غیرفعال شدن مجموعه",
        countWarning = 3,
        sumScore = 47,
        recreationCenterId = "center-001",
        sourceCreate = 2,
        recalculateStatistics = true,
        countVisitorActive = 0,
        countStandAssign = 0,
        assignedSerialCount = 32,
        cancelledSerialCount = 8,
        remainingSerialCount = 0,
        issuedSerialCount = 32,
        visitCount = 9,
        visitorId = "visitor-012",
        visitDate = "1405/05/15",
        programmingVisitCount = 10,
        usingSerialCount = 32,
        deactiveDate = "1405/05/30",
        subjectVisitId = "subject-012"
    )
)


val demoMeetings = listOf(
    MeetingEntity(
        meetingId = "meet-001",
        name = "بازدید حضوری برنامه‌ریزی شده - هتل قصر طلایی",
        description = "مشهد، خیابان آزادی",
        organizationId = "org-ghasr-talaee",
        status = 2, // MeetingStatus.CANCELLED یا مقدار متناظر در سیستم
        type = 1,   // نوع ۱: بازدید حضوری
        visitDate = "1403/02/15",
        visitTime = "11:30",
        visitorId = "visitor-ali-mohammadi",
        visitRealDate = "1403/02/15",
        subjectVisitId = "subj-regular-inspection",
        personId = "person-ali-mohammadi",
        latitude = 36.2972,
        longitude = 59.6067
    ),
    MeetingEntity(
        meetingId = "meet-002",
        name = "بازدید تلفنی - هتل الماس",
        description = "مشهد، خیابان آزادی",
        organizationId = "org-almas",
        status = 2, // MeetingStatus.CANCELLED
        type = 2,   // نوع ۲: بازدید تلفنی
        visitDate = "1403/02/17",
        visitTime = "10:30",
        visitorId = "visitor-ali-rezaei",
        visitRealDate = null,
        subjectVisitId = "subj-follow-up-call",
        personId = "person-ali-rezaei",
        latitude = 36.2891,
        longitude = 59.6125
    ),
    MeetingEntity(
        meetingId = "meet-003",
        name = "بازدید حضوری غیربرنامه‌ریزی شده - هتل پارسیان",
        description = "مشهد، احمد آباد",
        organizationId = "org-parsian",
        status = 0, // MeetingStatus.PLANNED
        type = 1,   // نوع ۱: بازدید حضوری
        visitDate = "1403/02/16",
        visitTime = "02:30",
        visitorId = "visitor-maryam-rezaei",
        visitRealDate = "1403/02/16",
        subjectVisitId = "subj-ad-hoc-visit",
        personId = "person-maryam-rezaei",
        latitude = 36.3155,
        longitude = 59.5684
    ),
    MeetingEntity(
        meetingId = "meet-004",
        name = "بازدید تلفنی - سازمان برق",
        description = "مشهد، پاسداران",
        organizationId = "org-bargh-corp",
        status = 1, // MeetingStatus.DONE
        type = 2,   // نوع ۲: بازدید تلفنی
        visitDate = "1403/02/18",
        visitTime = "10:30",
        visitorId = "visitor-maryam-mofrad",
        visitRealDate = "1403/02/18",
        subjectVisitId = "subj-support-follow-up",
        personId = "person-maryam-mofrad",
        latitude = 36.3001,
        longitude = 59.5842
    )
)


val demoOrganizationWarnings = listOf(
    OrganizationWarningEntity(
        organizationWarningId = "warn-001",
        name = "تذکر شفاهی",
        organizationId = "org-ghasr-talaee",
        warningId = "warn-type-oral",
        visitorId = "احمد شفاهی", // یا شناسه ویزیتور مربوطه: "visitor-ahmad-shafiee"
        dateWarning = "1405/05/02",
        description = "تاخیر در پاسخ‌گویی به پیگیری‌های واحد بازاریابی",
        score = 2,
        meetingId = "meet-001"
    ),
    OrganizationWarningEntity(
        organizationWarningId = "warn-002",
        name = "تذکر کتبی",
        organizationId = "org-ghasr-talaee",
        warningId = "warn-type-written",
        visitorId = "رضا موسوی", // "visitor-reza-mousavi"
        dateWarning = "1405/05/02",
        description = "عدم رعایت استانداردهای قرارداد همکاری در ارائه استند تبلیغاتی",
        score = 4,
        meetingId = "meet-002"
    ),
    OrganizationWarningEntity(
        organizationWarningId = "warn-003",
        name = "تذکر شفاهی",
        organizationId = "org-darvishi",
        warningId = "warn-type-oral",
        visitorId = "پرهام شیری", // "visitor-parham-shiri"
        dateWarning = "1405/05/02",
        description = "مغایرت جزئی در اطلاعات ثبت‌شده حساب کاربری",
        score = -3,
        meetingId = null
    )
)

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

val demoOrganizationPersons = listOf(
    OrganizationPersonEntity(
        organizationPersonId = "org-person-001",
        personId = "person-001",
        organizationId = "org-ghasr-talaee",
        name = "علی حسینی",
        description = "مدیر داخلی هتل",
        role = 1,
        statusRelation = 2,
        isCommissionEligible = true,
        remainBalance = "1500000",
        beforeRemain = "0",
        beforeDate = "1404/12/28",
        finalDate = "1405/06/31",
        appPassword = null,
        updateRemainPriceWallet = false
    ),
    OrganizationPersonEntity(
        organizationPersonId = "org-person-002",
        personId = "person-002",
        organizationId = "org-ghasr-talaee",
        name = "مریم احمدی",
        description = "مسئول پذیرش",
        role = 2,
        statusRelation = 2,
        isCommissionEligible = false,
        remainBalance = "0",
        beforeRemain = "0",
        beforeDate = "1405/01/10",
        finalDate = "1405/06/31",
        appPassword = null,
        updateRemainPriceWallet = false
    )
)

val demoAdvertisingStandList = listOf(
    AdvertisingStandEntity(
        advertisingStandId = "1",
        name = "استند رومیزی",
        code = "TABLE_STAND",
        description = "",
        displayType = 0,
        installationType = 5,
        recreationCenterId = "1001",
        standType = 1
    ),
    AdvertisingStandEntity(
        advertisingStandId = "2",
        name = "بروشور معرفی",
        code = "BROCHURE",
        description = "توضیحات استند",
        displayType = 1,
        installationType = 2,
        recreationCenterId = "1001",
        standType = 2
    ),
    AdvertisingStandEntity(
        advertisingStandId = "3",
        name = "استند لابی",
        code = "LOBBY_STAND",
        description = "",
        displayType = 2,
        installationType = 1,
        recreationCenterId = "1002",
        standType = 3
    )
)


val demoStandAssignments = listOf(
    StandAssignmentEntity(
        standAssignmentId = "1",
        visitorId = "محمد احمدی",
        organizationId = "هتل پارسیان آزادی",
        status = 1,
        advertisingStandId = "stand_1",
        count = 5,
        assignmentDate = "۱۴۰۳/۰۳/۲۲",
        assignmentType = 1,
        assignmentMode = 2
    ),
    StandAssignmentEntity(
        standAssignmentId = "2",
        visitorId = "سارا مرادی",
        organizationId = " هتل اسپیناس پالاس",
        status = 2,
        advertisingStandId = "stand_2",
        count = 3,
        assignmentDate = "۱۴۰۳/۰۳/۲۴",
        assignmentType = 2,
        assignmentMode = 1
    ),
    StandAssignmentEntity(
        standAssignmentId = "3",
        visitorId = "علی رضایی",
        organizationId = "هتل هما",
        status = 3,
        advertisingStandId = "stand_3",
        count = 2,
        assignmentDate = "۱۴۰۳/۰۳/۲۰",
        assignmentType = 3,
        assignmentMode = 3
    ),
    StandAssignmentEntity(
        standAssignmentId = "4",
        visitorId = "نازنین کریمی",
        organizationId = "هتل بزرگ تهران",
        status = 0,
        advertisingStandId = "stand_4",
        count = 4,
        assignmentDate = "۱۴۰۳/۰۳/۲۵",
        assignmentType = 3,
        assignmentMode = 2
    ),
    StandAssignmentEntity(
        standAssignmentId = "5",
        visitorId = "علی رضایی",
        organizationId = "هتل هما",
        status = 1,
        advertisingStandId = "stand_3",
        count = 2,
        assignmentDate = "۱۴۰۳/۰۳/۲۰",
        assignmentType = 2,
        assignmentMode = 2
    ),
    StandAssignmentEntity(
        standAssignmentId = "6",
        visitorId = "نازنین کریمی",
        organizationId = "هتل بزرگ تهران",
        status = 1,
        advertisingStandId = "stand_1",
        count = 4,
        assignmentDate = "۱۴۰۳/۰۳/۲۵",
        assignmentType = 1,
        assignmentMode = 1
    )
)


fun demoOfferTicketPlans(): List<OfferTicketPlanEntity> = listOf(
    OfferTicketPlanEntity(
        offerTicketPlanId = "1",
        name = "طرح تخفیف اقامت نوروزی",
        code = "OFF-101",
        recreationCenterId = null,
        validFromDate = "1405/01/01",
        validToDate = "1405/01/15",
        status = 1
    ),
    OfferTicketPlanEntity(
        offerTicketPlanId = "2",
        name = "طرح بلیط تخفیف فصلی بهار",
        code = "OFF-102",
        recreationCenterId = null,
        validFromDate = "1405/01/16",
        validToDate = "1405/03/31",
        status = 2
    ),
    OfferTicketPlanEntity(
        offerTicketPlanId = "3",
        name = "طرح آفر آخرهفته تابستانه",
        code = "OFF-103",
        recreationCenterId = null,
        validFromDate = "1405/04/01",
        validToDate = "1405/06/31",
        status = 3
    ),
    OfferTicketPlanEntity(
        offerTicketPlanId = "4",
        name = "طرح تشویقی وفاداری مشتریان",
        code = "OFF-104",
        recreationCenterId = null,
        validFromDate = "1404/07/01",
        validToDate = "1404/12/29",
        status = 2
    ),
    OfferTicketPlanEntity(
        offerTicketPlanId = "5",
        name = "طرح مشتریان ممتاز",
        code = "OFF-105",
        recreationCenterId = null,
        validFromDate = "1404/05/04",
        validToDate = "1404/10/20",
        status = 2
    )
)


fun demoOfferTicketPlanLines(): List<OfferTicketPlanLineEntity> = listOf(
    OfferTicketPlanLineEntity(
        offerTicketPlanLineId = "1",
        name = "آفر بلیط بزرگسال",
        commissionType = 1, // درصدی
        commissionPercent = "5.0",
        commissionAmount = null,
        discountType = 1, // بدون تخفیف
        discountPercent = null,
        discountAmount = null,
        gender = 1, // نیاز نیست
        personCategory = 1, // کودک
        offerTicketPlanId = null, // Header ID
        productServiceId = "بلیط موج‌های آبی خردسال",
        status = 1
    ),
    OfferTicketPlanLineEntity(
        offerTicketPlanLineId = "2",
        name = "آفر بلیط خردسال",
        commissionType = 2, // مبلغ ثابت
        commissionPercent = null,
        commissionAmount = "150000",
        discountType = 2, // درصدی
        discountPercent = "10.0",
        discountAmount = null,
        gender = 2, // آقا و خانم
        personCategory = 2, // بزرگسال
        offerTicketPlanId = null,
        productServiceId = "بلیط پارک آبی بزرگسال",
        status = 2
    ),
    OfferTicketPlanLineEntity(
        offerTicketPlanLineId = "3",
        name = "بلیط",
        commissionType = 3, // بدون پورسانت
        commissionPercent = null,
        commissionAmount = null,
        discountType = 3, // مبلغ ثابت
        discountPercent = null,
        discountAmount = "300000",
        gender = 1, // نیاز نیست
        personCategory = 3, // همه رده‌های سنی
        offerTicketPlanId = null,
        productServiceId = "پکیج خانوادگی مجموعه تفریحی",
        status = 4
    )
)

fun demoContracts(): List<ContractEntity> = listOf(
    ContractEntity(
        contractId = "1",
        name = "قرارداد همکاری سال ۱۴۰۵",
        contractNumber = "CNT-1405-001",
        organizationId = "ORG-001", // هتل اسپیناس پالاس
        cooperationModel = 1, // خرید مستقیم سازمانی (DIRECT_ORGANIZATION_PURCHASE)
        settlementPeriodType = 3, // ماهیانه (MONTHLY)
        startDate = "1405/01/01",
        endDate = "1405/12/29",
        contractStatus = 1, // فعال (ACTIVE)
        defaultDiscountPercent = 10.0,
        defaultCommissionPercent = 5.0,
        description = "توافق بر اساس نرخ‌نامه رسمی هتل با ۱۰٪ تخفیف برای مشتریان سازمانی."
    ),
    ContractEntity(
        contractId = "2",
        name = "قرارداد همکاری فصلی",
        contractNumber = "CNT-1405-002",
        organizationId = "ORG-002", // هتل پارسیان آزادی
        cooperationModel = 0, // خرید با برگه معرفی (REFERRAL_VOUCHER)
        settlementPeriodType = 1, // روزانه (DAILY)
        startDate = "1405/03/01",
        endDate = "1405/05/31",
        contractStatus = 2, // معلق (SUSPENDED)
        defaultDiscountPercent = 15.0,
        defaultCommissionPercent = 7.5,
        description = "پورسانت بر اساس درصد فروش هر فصل محاسبه و تسویه می‌شود."
    ),
    ContractEntity(
        contractId = "3",
        name = "قرارداد همکاری شش‌ماهه",
        contractNumber = "CNT-1404-003",
        organizationId = "ORG-003", // هتل هما شیراز
        cooperationModel = 1, // خرید مستقیم سازمانی (DIRECT_ORGANIZATION_PURCHASE)
        settlementPeriodType = 1, // روزانه (DAILY)
        startDate = "1404/10/01",
        endDate = "1405/03/31",
        contractStatus = 2, // معلق (SUSPENDED)
        defaultDiscountPercent = 12.0,
        defaultCommissionPercent = 6.0,
        description = "تمدید خودکار قرارداد در صورت تحقق سقف فروش تعیین‌شده."
    ),
    ContractEntity(
        contractId = "4",
        name = "قرارداد همکاری سالانه",
        contractNumber = "CNT-1404-004",
        organizationId = "ORG-004", // هتل بزرگ تهران
        cooperationModel = 0, // خرید با برگه معرفی (REFERRAL_VOUCHER)
        settlementPeriodType = 3, // ماهیانه (MONTHLY)
        startDate = "1404/06/01",
        endDate = "1405/05/31",
        contractStatus = 3, // غیرفعال (INACTIVE)
        defaultDiscountPercent = 8.0,
        defaultCommissionPercent = 4.0,
        description = "قرارداد به‌دلیل عدم تمدید در پایان دوره، غیرفعال شده است."
    ),
    ContractEntity(
        contractId = "5",
        name = "قرارداد همکاری نوروز ۱۴۰۵",
        contractNumber = "CNT-1404-005",
        organizationId = "ORG-005", // هتل پردیس کیش
        cooperationModel = 1, // خرید مستقیم سازمانی (DIRECT_ORGANIZATION_PURCHASE)
        settlementPeriodType = 3, // ماهیانه (MONTHLY)
        startDate = "1404/12/15",
        endDate = "1405/02/15",
        contractStatus = 3, // غیرفعال (INACTIVE)
        defaultDiscountPercent = 20.0,
        defaultCommissionPercent = 10.0,
        description = "پروژه ویژه نوروز با تخفیف پلکانی برای رزروهای گروهی."
    ),
    ContractEntity(
        contractId = "6",
        name = "قرارداد همکاری تابستانه",
        contractNumber = "CNT-1405-006",
        organizationId = "ORG-006", // هتل آسمان اصفهان
        cooperationModel = 0, // خرید با برگه معرفی (REFERRAL_VOUCHER)
        settlementPeriodType = 2, // هفتگی (WEEKLY)
        startDate = "1405/04/01",
        endDate = "1405/06/31",
        contractStatus = 2, // معلق (SUSPENDED)
        defaultDiscountPercent = 10.0,
        defaultCommissionPercent = 5.0,
        description = "تخفیف ۱۰٪ برای رزروهای بالای ۵ شب."
    )
)



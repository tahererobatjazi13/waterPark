package ir.kitgroup.partnerManagement.feature.organization.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class OrganizationModel(
    val receationcenterid: Int,
    val name: String,
    val address: String,
    val status: Status,
    val grade: Int
)
/*
organizationtype
level
grade
ticketsalecounthistory  تعداد فروش در سیستم

kit_statusgetstand تمایل به دریافت استند تبلیغاتی

kit_customercapacity   ظرفیت یا پتانسیل مشتری
وضعیت مشتری خارجی kit_statusextenalcutomer

cityid
regionid
kit_longitude
kit_latitude
kit_document

kit_reasondeactive  علت غیر فعال شدن*/

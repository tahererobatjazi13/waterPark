package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contract")
data class ContractEntity(

    @PrimaryKey
    val contractId: String,

    val name: String? = null,

    val contractNumber: String? = null,
    // 0:خرید با برگه معرفی (REFERRAL_VOUCHER)  1خرید مستقیم سازمانی (DIRECT_ORGANIZATION_PURCHASE)
    val cooperationModel: Int? = null,

    val defaultCommissionPercent: Double? = null,

    val defaultDiscountPercent: Double? = null,

    val description: String? = null,

    val endDate: String? = null,

    val organizationId: String? = null,
    // 0:بدون تسویه (NONE)  1:روزانه (DAILY) 2:هفتگی (WEEKLY)  3:ماهیانه (MONTHLY)  4:سایر (OTHER)
    val settlementPeriodType: Int? = null,

    val startDate: String? = null,
    //  0:پیش‌نویس (DRAFT) 1:فعال (ACTIVE)  2:معلق (SUSPENDED)  3:غیرفعال (INACTIVE)
    val contractStatus: Int? = null
)
package ir.kitgroup.partnerManagement.core.database.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus

@Entity(
    tableName = "contracts",
    indices = [
        Index("organizationId"),
        Index("syncStatus")
    ]
)
data class ContractEntity(

    @PrimaryKey
    val contractId: String,

    val serverId: String? = null,

    val title: String? = null,

    val organizationId: String? = null,

    val contractNumber: String? = null,

    val startDate: String? = null,

    val endDate: String? = null,

    val description: String? = null,

    // 0:بدون تسویه (NONE)  1:روزانه (DAILY) 2:هفتگی (WEEKLY)  3:ماهیانه (MONTHLY)  4:سایر (OTHER)
    val settlementPeriodType: Int? = null,

    val settlementPeriodTypeName: String? = null,

    // 0:خرید با برگه معرفی (REFERRAL_VOUCHER)  1خرید مستقیم سازمانی (DIRECT_ORGANIZATION_PURCHASE)
    val cooperationModel: Int? = null,

    val cooperationModelName: String? = null,

    val defaultDiscountPercent: Double? = null,

    val defaultCommissionPercent: Double? = null,

    //  0:پیش‌نویس (DRAFT) 1:فعال (ACTIVE)  2:معلق (SUSPENDED)  3:غیرفعال (INACTIVE)
    val status: Int? = null,

    val statusName: String? = null,

    val stateCode: Int? = null,

    val modifiedOn: String? = null,

    // فیلدهای کنترل سینک
    val syncStatus: SyncStatus = SyncStatus.SYNCED,

    val isLocallyModified: Boolean = false,

    val lastModifiedTimestamp: Long = System.currentTimeMillis()
)
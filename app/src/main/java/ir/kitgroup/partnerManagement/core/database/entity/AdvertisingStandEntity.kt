package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advertising_stand")
data class AdvertisingStandEntity(

    @PrimaryKey
    val advertisingStandId: String,

    val name: String? = null,

    val code: String? = null,

    val description: String? = null,

    // نوع نمایش (DisplayType): چاپی=0، دیجیتال=1، تعاملی=2
    val displayType: Int? = null,

    // نوع نصب (InstallationType): ثابت=0، پرتال=1، دیواری=2، آویزی=3، ایستاده=4، رومیزی=5
    val installationType: Int? = null,

    val recreationCenterId: String? = null,

    // نوع استند (StandType): بنری=0، استند بروشور=1، استند رومیزی=2، بک لایت=3، رول آپ=4، ایکس استند=5، پاپ آپ=6، کیوسک=7، راهنما=8، برندینگ=9، سایر=10
    val standType: Int? = null
)

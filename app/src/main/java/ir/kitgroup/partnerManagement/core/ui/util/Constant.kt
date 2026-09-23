package ir.kitgroup.partnerManagement.core.ui.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.partnerManagement.R
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus.INITIAL_REGISTRATION

enum class UserRole {
    SUPERVISOR,
    VISITOR
}

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

enum class Status {
    ACTIVE,
    INACTIVE,
    USED,
    DELIVERED,
    DRAFT,
    BLOCKED,
    DONE,
    PLANNED,
    CANCELLED,
    RETURNED,
    ORANGE,
    CLOSED
}


enum class CardStatusFilter(
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    All(
        titleRes = R.string.label_all_status,
        icon = Icons.Default.List
    ),
    Used(
        titleRes = R.string.status_used,
        icon = Icons.Default.CheckCircle
    ),
    Delivered(
        titleRes = R.string.status_delivered,
        icon = Icons.Default.LocalShipping
    )
}

enum class AllocationFilterTab(val titleRes: Int) {
    All(R.string.label_all),
    Active(R.string.status_active),
    Draft(R.string.status_draft),
    Returned(R.string.status_returned),
    Cancelled(R.string.status_cancelled)
}


enum class OrganizationDetailTab(
    @StringRes val titleRes: Int
) {
    BASIC_INFO(
        titleRes = R.string.tab_organization_basic_info
    ),
    GENERAL_INFO(
        titleRes = R.string.tab_organization_general_info
    ),
    VISITS(
        titleRes = R.string.tab_visits
    ),
    PERSONS(
        titleRes = R.string.tab_organization_persons
    ),
    VISITOR(
        titleRes = R.string.tab_organization_visitor
    ),
    STANDS(
        titleRes = R.string.tab_organization_stands
    ),
    CONTRACTS(
        titleRes = R.string.tab_organization_contracts
    ),
    TICKETS(
        titleRes = R.string.tab_tickets_offers
    ),
    WARNING(
        titleRes = R.string.tab_organization_warnings
    )
}

enum class StatusColor {
    SUCCESS,
    ERROR,
    WARNING,
    INFO,
    NEUTRAL
}


enum class OrganizationStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {
    INITIAL_REGISTRATION(0, R.string.status_initial_registration, StatusColor.INFO),
    ACTIVE(1, R.string.status_active, StatusColor.SUCCESS),
    INACTIVE(2, R.string.status_inactive, StatusColor.ERROR),
    SUSPENDED(3, R.string.status_suspended, StatusColor.WARNING),
    CLOSED(4, R.string.status_closed, StatusColor.ERROR),
    YELLOW(5, R.string.status_yellow, StatusColor.WARNING),
    ORANGE(6, R.string.status_orange, StatusColor.WARNING);

    companion object {
        fun fromId(id: Int?): OrganizationStatus =
            entries.find { it.id == id } ?: INITIAL_REGISTRATION
    }
}

enum class VisitorOrganizationStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {
    INITIAL_REGISTRATION(0, R.string.status_initial_registration, StatusColor.INFO),
    ACTIVE(1, R.string.status_active, StatusColor.SUCCESS),
    INACTIVE(2, R.string.status_inactive, StatusColor.ERROR);

    companion object {
        fun fromId(id: Int?): VisitorOrganizationStatus =
            entries.find { it.id == id } ?: ACTIVE
    }
}


enum class PersonOrganizationStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {

    TERMINATION_COOPERATION(0, R.string.status_termination_cooperation, StatusColor.ERROR),
    ACTIVE(1, R.string.status_active, StatusColor.SUCCESS);

    companion object {
        fun fromId(id: Int?): PersonOrganizationStatus =
            entries.find { it.id == id } ?: ACTIVE
    }
}

enum class MeetingStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {
    PLANNED(0, R.string.status_planned, StatusColor.INFO),
    DONE(1, R.string.status_done, StatusColor.SUCCESS),
    CANCELLED(2, R.string.status_cancelled, StatusColor.ERROR);

    companion object {
        fun fromId(id: Int?): MeetingStatus =
            MeetingStatus.entries.find { it.id == id } ?: PLANNED
    }
}


enum class MeetingType(
    val value: Int,
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    PHONE(
        value = 0,
        titleRes = R.string.visit_type_phone, // تلفنی
        icon = Icons.Default.Phone
    ),
    PLANNED_IN_PERSON(
        value = 1,
        titleRes = R.string.visit_type_planned_in_person, // حضوری برنامه‌ریزی شده
        icon = Icons.Default.Schedule
    ),
    UNPLANNED_IN_PERSON(
        value = 2,
        titleRes = R.string.visit_type_unplanned_in_person, // حضوری غیر برنامه‌ریزی شده
        icon = Icons.Default.DirectionsWalk
    ),
    URGENT_IN_PERSON(
        value = 3,
        titleRes = R.string.visit_type_urgent_in_person, // حضوری فوری
        icon = Icons.Default.FlashOn
    );

    companion object {
        fun fromValue(value: Int?): MeetingType {
            return entries.firstOrNull { it.value == value } ?: PHONE
        }
    }
}


enum class DisplayType(val id: Int, @StringRes val titleRes: Int) {
    PRINT(0, R.string.display_type_print),           // چاپی
    DIGITAL(1, R.string.display_type_digital),       // دیجیتال
    INTERACTIVE(2, R.string.display_type_interactive); // تعاملی

    companion object {
        fun fromId(id: Int?): DisplayType? = entries.find { it.id == id }
    }
}

enum class InstallationType(val id: Int, @StringRes val titleRes: Int) {
    FIXED(0, R.string.installation_type_fixed),       // ثابت
    PORTABLE(1, R.string.installation_type_portable), // پرتال
    WALL(2, R.string.installation_type_wall),         // دیواری
    HANGING(3, R.string.installation_type_hanging),   // آویزی
    STANDING(4, R.string.installation_type_standing), // ایستاده
    DESKTOP(5, R.string.installation_type_desktop);   // رومیزی

    companion object {
        fun fromId(id: Int?): InstallationType? = entries.find { it.id == id }
    }
}

enum class StandType(val id: Int, @StringRes val titleRes: Int) {
    BANNER(0, R.string.stand_type_banner),             // بنری
    BROCHURE_STAND(1, R.string.stand_type_brochure),   // استند بروشور
    DESKTOP_STAND(2, R.string.stand_type_desktop),     // استند رومیزی
    BACKLIGHT(3, R.string.stand_type_backlight),       // بک لایت
    ROLLUP(4, R.string.stand_type_rollup),             // رول آپ
    X_STAND(5, R.string.stand_type_xstand),            // ایکس استند
    POPUP(6, R.string.stand_type_popup),               // پاپ آپ
    KIOSK(7, R.string.stand_type_kiosk),               // کیوسک
    GUIDE(8, R.string.stand_type_guide),               // راهنما
    BRANDING(9, R.string.stand_type_branding),         // برندینگ
    OTHER(10, R.string.stand_type_other);              // سایر

    companion object {
        fun fromId(id: Int?): StandType? = entries.find { it.id == id }
    }
}

enum class AssignmentType(val id: Int, @StringRes val titleRes: Int) {
    ASSIGN_TO_VISITOR(1, R.string.assign_type_to_visitor),      // تخصیص به بازاریاب
    ASSIGN_TO_ORGANIZATION(2, R.string.assign_type_to_organization), // تخصیص به سازمان
    RETURN(3, R.string.assign_type_return),                          // عودت
    COLLECT(4, R.string.assign_type_collect),                        // جمع آوری
    TERMINATE(5, R.string.assign_type_terminate);                    // خاتمه

    companion object {
        fun fromId(id: Int?): AssignmentType? = entries.find { it.id == id }
    }
}

enum class AssignmentMode(val id: Int, @StringRes val titleRes: Int) {
    TABLIGHATI(0, R.string.assign_mode_tablighati), // تبلیغاتی
    AMANI(1, R.string.assign_mode_amani),           // امانی
    EJAREHI(2, R.string.assign_mode_ejarehi);       // اجاره‌ای

    companion object {
        fun fromId(id: Int?): AssignmentMode? = entries.find { it.id == id }
    }
}

enum class StandAssignmentStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {
    DRAFT(0, R.string.status_draft, StatusColor.INFO),
    ACTIVE_DELIVERED(1, R.string.status_active_delivered, StatusColor.SUCCESS),
    RETURNED(2, R.string.status_returned, StatusColor.ERROR),
    CANCELED(3, R.string.status_cancelled, StatusColor.ERROR);

    companion object {
        fun fromId(id: Int?): StandAssignmentStatus =
            entries.find { it.id == id } ?: DRAFT
    }
}

enum class OfferPlanStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {
    DRAFT(0, R.string.status_draft, StatusColor.INFO),
    ACTIVE(1, R.string.status_active, StatusColor.SUCCESS),
    SUSPENDED(2, R.string.status_suspended, StatusColor.ERROR),
    CLOSED(3, R.string.status_closed, StatusColor.ERROR);

    companion object {
        fun fromId(id: Int?): OfferPlanStatus =
            entries.find { it.id == id } ?: DRAFT
    }
}


enum class CommissionType(
    val id: Int,
    @StringRes val titleRes: Int
) {
    NONE(0, R.string.commission_none),
    PERCENTAGE(1, R.string.commission_percentage),
    FIXED_AMOUNT(2, R.string.commission_fixed);

    companion object {
        fun fromId(id: Int?): CommissionType =
            entries.find { it.id == id } ?: NONE
    }
}

enum class DiscountType(
    val id: Int,
    @StringRes val titleRes: Int
) {
    NONE(0, R.string.discount_none),
    PERCENTAGE(1, R.string.discount_percentage),
    FIXED_AMOUNT(2, R.string.discount_fixed);

    companion object {
        fun fromId(id: Int?): DiscountType =
            entries.find { it.id == id } ?: NONE
    }
}

enum class PersonCategory(
    val id: Int,
    @StringRes val titleRes: Int
) {
    UNASSIGNED(0, R.string.label_category_none),
    ADULT(1, R.string.label_category_adult),
    CHILD(2, R.string.label_category_child);

    companion object {
        fun fromId(id: Int?): PersonCategory {
            return entries.firstOrNull { it.id == id } ?: UNASSIGNED
        }
    }
}

enum class GenderType(
    val id: Int,
    @StringRes val titleRes: Int
) {
    UNASSIGNED(0, R.string.gender_none),
    MALE(1, R.string.gender_male),
    FEMALE(2, R.string.gender_female),
    NOT_REQUIRED(3, R.string.gender_not_required);

    companion object {
        fun fromId(id: Int?): GenderType =
            entries.find { it.id == id } ?: UNASSIGNED
    }
}


enum class OfferPlanLineStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {
    ACTIVE(0, R.string.status_active, StatusColor.SUCCESS),
    TEMPORARILY_STOPPED(1, R.string.status_temporarily_stopped, StatusColor.WARNING),
    REVOKED(2, R.string.status_revoked, StatusColor.ERROR);

    companion object {
        fun fromId(id: Int?): OfferPlanLineStatus =
            entries.find { it.id == id } ?: ACTIVE
    }
}


enum class CooperationModel(
    val id: Int,
    @StringRes val titleRes: Int
) {
    REFERRAL_VOUCHER(0, R.string.cooperation_model_referral_voucher),
    DIRECT_ORGANIZATION_PURCHASE(1, R.string.cooperation_model_direct_organization_purchase);

    companion object {
        fun fromId(id: Int?): CooperationModel? = entries.firstOrNull { it.id == id }
    }
}


enum class SettlementPeriodType(
    val id: Int,
    @StringRes val titleRes: Int
) {
    NONE(0, R.string.settlement_none),
    DAILY(1, R.string.settlement_daily),
    WEEKLY(2, R.string.settlement_weekly),
    MONTHLY(3, R.string.settlement_monthly),
    OTHER(4, R.string.settlement_other);

    companion object {
        fun fromId(id: Int?): SettlementPeriodType? = entries.firstOrNull { it.id == id }
    }
}

enum class ContractStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {

    DRAFT(0, R.string.status_draft, StatusColor.INFO),
    ACTIVE(1, R.string.status_active, StatusColor.SUCCESS),
    SUSPENDED(2, R.string.status_suspended, StatusColor.ERROR),
    INACTIVE(3, R.string.status_inactive, StatusColor.ERROR);

    companion object {
        fun fromId(id: Int?): ContractStatus =
            entries.find { it.id == id } ?: DRAFT
    }
}


enum class ContractOfferStatus(
    override val id: Int,
    override val titleRes: Int,
    override val colorType: StatusColor
) : AppStatus {

    DRAFT(0, R.string.status_draft, StatusColor.INFO),
    ACTIVE(1, R.string.status_allocated_active, StatusColor.SUCCESS),
    SUSPENDED(2, R.string.status_suspended, StatusColor.ERROR),
    EXHAUSTED(3, R.string.status_quota_exhausted, StatusColor.ERROR);

    companion object {
        fun fromId(id: Int?): ContractOfferStatus =
            entries.find { it.id == id } ?: DRAFT
    }
}

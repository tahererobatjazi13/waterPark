package ir.kitgroup.partnerManagement.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PartnerManagementColors(
    val appBackground: Color,
    val screenBackground: Color,
    val cardBackground: Color,
    val cardBackgroundAlt: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val border: Color,

    val success: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val warning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val error: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    val info: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,

    val purple: Color,
    val purpleContainer: Color,
    val onPurpleContainer: Color,

    val iconBlueContainer: Color,
    val iconGreenContainer: Color,
    val iconPurpleContainer: Color,
    val iconOrangeContainer: Color,

    val ratingActive: Color,
    val ratingInactive: Color,
    val badge: Color,

    val tableHeaderBackground: Color,
    val tableRowEven: Color,
    val tableRowOdd: Color,

    val chartBlue: Color,
    val chartTeal: Color,
    val chartOrange: Color,
    val chartPurple: Color

)
val LightPartnerManagementColors = PartnerManagementColors(
    appBackground = White,
    screenBackground = LightBackground,
    cardBackground = LightSurface,
    cardBackgroundAlt = LightSurfaceVariant,

    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textTertiary = LightTextTertiary,
    border = LightBorder,

    /*
     * موفقیت
     */
    success = Success,
    successContainer = Color(0xFFDCFCE7),
    onSuccessContainer = Color(0xFF14532D),

    /*
     * هشدار
     */
    warning = Warning,
    warningContainer = Color(0xFFFFEDD5),
    onWarningContainer = Color(0xFF7C2D12),

    /*
     * خطا
     */
    error = Error,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),

    /*
     * اطلاعات
     */
    info = Info,
    infoContainer = Color(0xFFDBEAFE),
    onInfoContainer = Color(0xFF1E3A8A),

    /*
     * بنفش
     */
    purple = Purple,
    purpleContainer = Color(0xFFEDE9FE),
    onPurpleContainer = Color(0xFF4C1D95),

    /*
     * پس‌زمینه آیکن‌ها
     */
    iconBlueContainer = Color(0xFFDBEAFE),
    iconGreenContainer = Color(0xFFDCFCE7),
    iconPurpleContainer = Color(0xFFEDE9FE),
     iconOrangeContainer = Color(0xFFFFEDD5),

    ratingActive = Color(0xFFF59E0B),
    ratingInactive = Color(0xFFD1D5DB),
    badge = BadgeRed,

    /*
     * جدول
     */
    tableHeaderBackground = Color(0xFFE2E8F0),
    tableRowEven = Color(0xFFFFFFFF),
    tableRowOdd = Color(0xFFF1F5F9),

    /*
     * نمودار
     */
    chartBlue = Color(0xFF1D4ED8),
    chartTeal = Color(0xFF0F766E),
    chartOrange = Color(0xFFC2410C),
    chartPurple = Color(0xFF6D28D9)
)
val DarkPartnerManagementColors = PartnerManagementColors(
    appBackground = Black,
    screenBackground = DarkBackground,
    cardBackground = DarkSurface,
    cardBackgroundAlt = DarkSurfaceVariant,

    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textTertiary = DarkTextTertiary,
    border = DarkBorder,

    /*
     * موفقیت
     */
    success = SuccessDark,
    successContainer = Color(0xFF064E3B),
    onSuccessContainer = Color(0xFFA7F3D0),

    /*
     * هشدار
     */
    warning = WarningDark,
    warningContainer = Color(0xFF4A2B05),
    onWarningContainer = Color(0xFFFDE68A),

    /*
     * خطا
     */
    error = ErrorDark,
    errorContainer = Color(0xFF5A1A1A),
    onErrorContainer = Color(0xFFFECACA),

    /*
     * اطلاعات
     */
    info = InfoDark,
    infoContainer = Color(0xFF123B63),
    onInfoContainer = Color(0xFFBFDBFE),

    /*
     * بنفش
     */
    purple = PurpleDark,
    purpleContainer = Color(0xFF3B1D5C),
    onPurpleContainer = Color(0xFFDDD6FE),

    /*
     * پس‌زمینه آیکن‌ها
     */
    iconBlueContainer = Color(0xFF123B63),
    iconGreenContainer = Color(0xFF064E3B),
    iconPurpleContainer = Color(0xFF3B1D5C),
    iconOrangeContainer = Color(0xFF4A2B05),

    ratingActive = Color(0xFFFBBF24),
    ratingInactive = Color(0xFF52525B),
    badge = Color(0xFFFB7185),

    /*
     * جدول
     */
    tableHeaderBackground = Color(0xFF303844),
    tableRowEven = Color(0xFF1C2128),
    tableRowOdd = Color(0xFF252B34),

    /*
     * نمودار
     */
    chartBlue = Color(0xFF60A5FA),
    chartTeal = Color(0xFF2DD4BF),
    chartOrange = Color(0xFFFBBF24),
    chartPurple = Color(0xFFC084FC)
)

val LocalPartnerManagementColors = staticCompositionLocalOf {
    LightPartnerManagementColors
}

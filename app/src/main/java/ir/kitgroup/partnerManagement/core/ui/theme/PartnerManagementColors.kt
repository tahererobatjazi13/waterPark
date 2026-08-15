package ir.kitgroup.partnerManagement.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PartnerManagementColors(
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
    screenBackground = LightBackground,
    cardBackground = LightSurface,
    cardBackgroundAlt = LightSurfaceVariant,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textTertiary = LightTextTertiary,
    border = LightBorder,

    success = Success,
    successContainer = Color(0xFFE8F5E9),
    onSuccessContainer = Color(0xFF1B8B4A),

    warning = Warning,
    warningContainer = Color(0xFFFFF3E0),
    onWarningContainer = Color(0xFFF57C00),

    error = Error,
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFFD32F2F),

    info = Info,
    infoContainer = Color(0xFFE3F2FD),
    onInfoContainer = Color(0xFF1976D2),

    purple = Purple,
    purpleContainer = Color(0xFFF3E5F5),
    onPurpleContainer = Color(0xFF7B1FA2),

    iconBlueContainer = Color(0xFFE3F2FD),
    iconGreenContainer = Color(0xFFE8F5E9),
    iconPurpleContainer = Color(0xFFF3E5F5),

    ratingActive = Color(0xFFFFC107),
    ratingInactive = Color(0xFFE0E0E0),
    badge = BadgeRed,

    // NEW
    tableHeaderBackground = Color(0xFFF3F6FA),
    tableRowEven = Color(0xFFFFFFFF),
    tableRowOdd = Color(0xFFF8FAFC),

    chartBlue = Color(0xFF1976D2),
    chartTeal = Color(0xFF00897B),
    chartOrange = Color(0xFFF57C00),
    chartPurple = Color(0xFF7B1FA2)
)

val DarkPartnerManagementColors = PartnerManagementColors(
    screenBackground = DarkBackground,
    cardBackground = DarkSurface,
    cardBackgroundAlt = DarkSurfaceVariant,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textTertiary = DarkTextTertiary,
    border = DarkBorder,

    success = SuccessDark,
    successContainer = Color(0xFF063D2A),
    onSuccessContainer = Color(0xFF86EFAC),

    warning = WarningDark,
    warningContainer = Color(0xFF442A05),
    onWarningContainer = Color(0xFFFDE68A),

    error = ErrorDark,
    errorContainer = Color(0xFF4A1515),
    onErrorContainer = Color(0xFFFCA5A5),

    info = InfoDark,
    infoContainer = Color(0xFF0B2A4A),
    onInfoContainer = Color(0xFF93C5FD),

    purple = PurpleDark,
    purpleContainer = Color(0xFF2E1A47),
    onPurpleContainer = Color(0xFFC4B5FD),

    iconBlueContainer = Color(0xFF0B2A4A),
    iconGreenContainer = Color(0xFF063D2A),
    iconPurpleContainer = Color(0xFF2E1A47),

    ratingActive = Color(0xFFFBBF24),
    ratingInactive = Color(0xFF3F3F46),
    badge = BadgeRed,

    // NEW
    tableHeaderBackground = Color(0xFF1F2937),
    tableRowEven = Color(0xFF111827),
    tableRowOdd = Color(0xFF0F172A),

    chartBlue = Color(0xFF60A5FA),
    chartTeal = Color(0xFF2DD4BF),
    chartOrange = Color(0xFFFBBF24),
    chartPurple = Color(0xFFC084FC)
)

val LocalPartnerManagementColors = staticCompositionLocalOf {
    LightPartnerManagementColors
}

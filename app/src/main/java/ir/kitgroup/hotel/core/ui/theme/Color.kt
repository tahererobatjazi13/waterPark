package ir.kitgroup.hotel.core.ui.theme

import androidx.compose.ui.graphics.Color


/*
--------------------------------
Core Palette
--------------------------------
*/
val BrandBlue = Color(0xFF00ADEF)
val BrandBlueDark = Color(0xFF0088CC)
val BrandBlueContainer = Color(0xFFE8F4FD)

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

/*
--------------------------------
Light Theme Base
--------------------------------
*/
val LightBackground = Color(0xFFF5F8FA)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F4F8)

val LightTextPrimary = Color(0xFF111827)
val LightTextSecondary = Color(0xFF6B7280)
val LightTextTertiary = Color(0xFF8E9298)

val LightBorder = Color(0xFFE5E7EB)

/*
--------------------------------
Dark Theme Base
--------------------------------
*/
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkSurfaceVariant = Color(0xFF252525)

val DarkTextPrimary = Color(0xFFF9FAFB)
val DarkTextSecondary = Color(0xFFD1D5DB)
val DarkTextTertiary = Color(0xFF9CA3AF)

val DarkBorder = Color(0xFF2F2F2F)

/*
--------------------------------
Semantic
--------------------------------
*/
val Success = Color(0xFF10B981)
val SuccessDark = Color(0xFF34D399)

val Warning = Color(0xFFF59E0B)
val WarningDark = Color(0xFFFBBF24)

val Error = Color(0xFFD32F2F)
val ErrorDark = Color(0xFFF87171)

val Info = Color(0xFF1976D2)
val InfoDark = Color(0xFF60A5FA)

val Purple = Color(0xFF7C3AED)
val PurpleDark = Color(0xFFA78BFA)

val BadgeRed = Color(0xFFF43F5E)

/*
--------------------------------
Legacy Aliases
Keep temporarily to avoid compile errors.
Gradually replace these with MaterialTheme.colorScheme or HotelTheme.colors.
--------------------------------
*/
val PrimaryBlue = BrandBlue
val PrimaryBlueDark = BrandBlueDark
val PrimaryBlueLight = BrandBlueContainer

val ScreenBackground = LightBackground
val DarkBlack = DarkSurface

val Gray50 = Color(0xFFF9FAFB)
val Gray100 = Color(0xFFF1F4F8)
val Gray200 = Color(0xFFE0E0E0)
val Gray400 = Color(0xFF8E9298)
val Gray700 = Color(0xFF4A4A4A)
val Gray800 = Color(0xFF424242)
val Gray900 = Color(0xFF1F2A37)

val LightGray = Gray400
val DarkGray = Gray700

val SuccessBackground = Color(0xFFE8F5E9)
val WarningBackground = Color(0xFFFFF3E0)
val ErrorBackground = Color(0xFFFFEBEE)

val StarGold = Color(0xFFFFC107)
val StarGray = Gray200

val BlueLight = Color(0xFFE8F0FE)
val TabBackground = Gray100
val FilterSelectedBackground = BlueLight
val FilterUnselectedBackground = Gray200

val BlueIconBackground = Color(0xFFE3F2FD)
val GreenIconBackground = SuccessBackground
val PurpleIconBackground = Color(0xFFF3E5F5)

val BlueIconTint = Info
val GreenIconTint = Success
val PurpleIconTint = Purple

val Blue50 = Color(0xFFE3F2FD)
val Blue700 = Info

val BlueBackground = Blue50
val BlueContent = Blue700

val OrangeBackground = WarningBackground
val OrangeContent = Warning

val RedBackground = ErrorBackground
val RedContent = Error

val DashboardBlue = BrandBlue
val DashboardBlueDark = BrandBlueDark
val ScreenBg = LightBackground
val TextDark = LightTextPrimary
val TextGray = LightTextSecondary
val BorderGray = LightBorder

val Orange = Warning
val Blue = Color(0xFF1D4ED8)
val Green = Success

val GreenBg = SuccessBackground
val GreenText = Color(0xFF1B8B4A)
val BlueBg = Blue50
val BlueText = Info
val GrayBg = Color(0xFFF3F4F6)
val GrayText = LightTextSecondary

/*
صفحه = MaterialTheme.colorScheme.background
کارت = MaterialTheme.colorScheme.surface
متن اصلی = MaterialTheme.colorScheme.onSurface
متن فرعی = HotelTheme.colors.textSecondary
خط جداکننده = HotelTheme.colors.border
رنگ‌های وضعیت = HotelTheme.colors.success/warning/error/info*/

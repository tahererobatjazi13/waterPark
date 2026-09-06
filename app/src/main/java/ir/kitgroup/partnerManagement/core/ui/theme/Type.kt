package ir.kitgroup.partnerManagement.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.kitgroup.partnerManagement.R


val IranSans = FontFamily(
    Font(R.font.iran_sans, FontWeight.Normal)
)

val AppTypography = Typography(
    titleSmall = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp
    ),
    titleMedium = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
    ),
    titleLarge = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    ),
    labelSmall = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    displaySmall = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    displayMedium = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    displayLarge = TextStyle(
        fontFamily = IranSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)
package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode


@Composable
fun AppScreenPreview(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        PartnerManagementTheme(
            themeMode = ThemeMode.LIGHT,
            dynamicColor = false
        ) {
            content()
        }
    }
}
package ir.kitgroup.partnerManagement.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CardMenuItem
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode

@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onBackClick: () -> Unit,
    onDisplayModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_setting_theme,
            showBackButton = true,
            onBackClick = onBackClick
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardMenuItem(
                    icon = R.drawable.ic_pallete,
                    title = R.string.label_display_mode,
                    subtitle = when (themeMode) {
                        ThemeMode.LIGHT -> stringResource(R.string.label_light_theme)
                        ThemeMode.DARK -> stringResource(R.string.label_dark_theme)
                        ThemeMode.SYSTEM -> stringResource(R.string.label_system_theme)
                    },
                    containerColor = appColors.cardBackground,
                    contentColor = appColors.textPrimary,
                    onClick = onDisplayModeClick
                )
            }
        }
    }
}

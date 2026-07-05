package ir.kitgroup.hotel.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CardMenuItem
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.theme.ThemeViewModel
import ir.kitgroup.hotel.core.ui.util.ThemeMode

@Composable
fun SettingsScreen(
    viewModel: ThemeViewModel,
    navController: NavController,
) {
    val themeMode by viewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_setting_theme,
            showBackButton = true,
            onBackClick = { navController.popBackStack() })
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
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
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        navController.navigate("theme_mode")
                    }
                )
            }
        }
    }
}


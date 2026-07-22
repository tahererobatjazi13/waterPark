package ir.kitgroup.hotel.feature.profile.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CardMenuItem
import ir.kitgroup.hotel.core.ui.components.CustomDialog
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.theme.HotelTheme
import ir.kitgroup.hotel.core.ui.theme.RedContent

@Composable
fun ProfileScreen(navController: NavController) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_user_profile,
            showBackButton = false
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                UserInfoCard()

                Spacer(modifier = Modifier.height(8.dp))

                CardMenuItem(
                    icon = R.drawable.ic_edit,
                    title = R.string.label_edit_information,
                    containerColor = HotelTheme.colors.infoContainer,
                    contentColor = HotelTheme.colors.onInfoContainer,
                    onClick = { /* Navigate to Edit */ }
                )

                CardMenuItem(
                    icon = R.drawable.ic_lock,
                    title = R.string.label_change_password,
                    containerColor = HotelTheme.colors.warningContainer,
                    contentColor = HotelTheme.colors.onWarningContainer,
                    onClick = { /* Navigate to Password */ }
                )

                CardMenuItem(
                    icon = R.drawable.ic_setting,
                    title = R.string.label_settings,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = {
                        navController.navigate("settings")
                    }
                )

                CardMenuItem(
                    icon = R.drawable.ic_logout,
                    title = R.string.label_log_out_account,
                    containerColor = HotelTheme.colors.errorContainer,
                    contentColor = HotelTheme.colors.onErrorContainer,
                    onClick = {
                        showLogoutDialog = true
                    }
                )
            }
        }
    }

    CustomDialog(
        isVisible = showLogoutDialog,
        title = stringResource(R.string.label_logout_title),
        message = stringResource(R.string.error_logout),
        confirmText = stringResource(R.string.label_logout),
        dismissText = stringResource(R.string.label_cancellation),
        confirmColor = RedContent,
        onConfirm = {
            showLogoutDialog = false
            // عملیات خروج را اینجا فراخوانی کنید
        },
        onDismiss = {
            showLogoutDialog = false
        }
    )

}

@Composable
private fun UserInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            UserInfoRow(
                label = stringResource(R.string.label_username),
                value = "علی رضایی",
                icon = R.drawable.ic_user_name,
                iconColor = HotelTheme.colors.iconBlueContainer,
                tint = HotelTheme.colors.info
            )
            ProfileDivider()
            UserInfoRow(
                label = stringResource(R.string.label_contact_number),
                value = "0915 123 4567",
                icon = R.drawable.ic_call,
                iconColor = HotelTheme.colors.iconGreenContainer,
                tint = HotelTheme.colors.success
            )
            ProfileDivider()
            UserInfoRow(
                label = stringResource(R.string.label_user_role),
                value = "مدیر سیستم",
                icon = R.drawable.ic_user_role,
                iconColor = HotelTheme.colors.iconPurpleContainer,
                tint = HotelTheme.colors.purple
            )
        }
    }
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 0.5.dp,
        color = HotelTheme.colors.border
    )
}
@Composable
private fun UserInfoRow(
    label: String,
    value: String,
    @DrawableRes icon: Int,
    iconColor: Color,
    tint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconColor, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            style = typography.labelSmall,
            color = HotelTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "|",
            style = typography.labelSmall,
            color = HotelTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = value,
            style = typography.labelMedium,
            color = HotelTheme.colors.textPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

package ir.kitgroup.partnerManagement.feature.profile.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CardMenuItem
import ir.kitgroup.partnerManagement.core.ui.components.CustomDialog
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.theme.RedContent

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val username by viewModel.userName.collectAsState()
    val role by viewModel.userRole.collectAsState()

    val roleTitle = remember(role) {
        when (role) {
            "0" -> "بازاریاب"
            "1" -> "سرپرست"
            "2" -> "پذیرش"
            "3" -> "مالی"
            "4" -> "سایر"
            else -> ""
        }
    }

    var showLogoutDialog by remember { mutableStateOf(false) }
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
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
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                UserInfoCard()

                CardMenuItem(
                    icon = R.drawable.ic_setting,
                    title = R.string.label_settings,
                    containerColor = appColors.cardBackground,
                    contentColor = appColors.textPrimary,
                    onClick = onSettingsClick
                )

                CardMenuItem(
                    icon = R.drawable.ic_logout,
                    title = R.string.label_log_out_account,
                    containerColor = appColors.errorContainer,
                    contentColor = appColors.onErrorContainer,
                    onClick = { showLogoutDialog = true }
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

            viewModel.logout {
                onLogoutSuccess()
            }
        },
        onDismiss = {
            showLogoutDialog = false
        }
    )
}

@Composable
private fun UserInfoCard() {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.8.dp,
            color = appColors.border.copy(alpha = 0.8f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            UserInfoRow(
                label = stringResource(R.string.label_person_full_name),
                value = "علی رضایی",
                icon = R.drawable.ic_user_name,
                iconColor = appColors.iconBlueContainer,
                tint = appColors.info
            )

            ProfileDivider()

            UserInfoRow(
                label = stringResource(R.string.label_personal_code),
                value = "4567",
                icon = R.drawable.ic_badge,
                iconColor = appColors.iconOrangeContainer,
                tint = appColors.warning
            )
            ProfileDivider()

            UserInfoRow(
                label = stringResource(R.string.label_mobile),
                value = "09151234567",
                icon = R.drawable.ic_call,
                iconColor = appColors.iconGreenContainer,
                tint = appColors.success
            )
            ProfileDivider()

            UserInfoRow(
                label = stringResource(R.string.label_user_role),
                value = "مدیر سیستم",
                icon = R.drawable.ic_user_role,
                iconColor = appColors.iconPurpleContainer,
                tint = appColors.purple
            )
        }
    }
}


@Composable
private fun ProfileDivider() {
    val appColors = LocalPartnerManagementColors.current
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 0.5.dp,
        color = appColors.border
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
    val appColors = LocalPartnerManagementColors.current

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
            style = typography.labelMedium,
            color = appColors.textSecondary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "|",
            style = typography.labelMedium,
            color = appColors.textSecondary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = value,
            style = typography.labelLarge,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun ProfileScreenPreview() {
    AppScreenPreview {
        ProfileScreen(
            onSettingsClick = {},
            onLogoutSuccess = {}
        )
    }
}
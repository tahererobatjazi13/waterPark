package ir.kitgroup.partnerManagement.feature.profile.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CardMenuItem
import ir.kitgroup.partnerManagement.core.ui.components.CustomDialog
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.theme.RedContent

@Composable
fun ProfileScreen(
    onEditInfoClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserInfoCard()

                CardMenuItem(
                    icon = R.drawable.ic_edit,
                    title = R.string.label_edit_information,
                    containerColor = appColors.infoContainer,
                    contentColor = appColors.onInfoContainer,
                    onClick = onEditInfoClick
                )

                CardMenuItem(
                    icon = R.drawable.ic_lock,
                    title = R.string.label_change_password,
                    containerColor = appColors.warningContainer,
                    contentColor = appColors.onWarningContainer,
                    onClick = onChangePasswordClick
                )

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
            onLogoutConfirm()
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
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            UserInfoRow(
                label = stringResource(R.string.label_username),
                value = "علی رضایی",
                icon = R.drawable.ic_user_name,
                iconColor = appColors.iconBlueContainer,
                tint = appColors.info
            )
            ProfileDivider()
            UserInfoRow(
                label = stringResource(R.string.label_contact_number),
                value = "0915 123 4567",
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
            style = MaterialTheme.typography.labelSmall,
            color = appColors.textSecondary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "|",
            style = MaterialTheme.typography.labelSmall,
            color = appColors.textSecondary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
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
            onEditInfoClick = {},
            onChangePasswordClick = {},
            onSettingsClick = {},
            onLogoutConfirm = {}
        )
    }
}

package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.SecondaryButtonVariant

@Composable
fun DualActionButtonBottomBar(
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryText: String = stringResource(R.string.label_edit),
    secondaryText: String = stringResource(R.string.label_delete),
    primaryIcon: ImageVector? = Icons.Outlined.Edit,
    secondaryIcon: ImageVector? = Icons.Outlined.Delete,
    isPrimaryLoading: Boolean = false,
    isSecondaryLoading: Boolean = false,
    isPrimaryEnabled: Boolean = true,
    isSecondaryEnabled: Boolean = true,
    secondaryVariant: SecondaryButtonVariant = SecondaryButtonVariant.OUTLINED,
    buttonHeight: Dp = 48.dp,
    cornerRadius: Dp = 12.dp,
    containerColor: Color = LocalPartnerManagementColors.current.cardBackground,
    borderStroke: BorderStroke? = BorderStroke(
        width = 0.5.dp,
        color = LocalPartnerManagementColors.current.border
    )
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // دکمه اصلی (ویرایش / ثبت)
            CustomButton(
                text = primaryText,
                onClick = onPrimaryClick,
                icon = primaryIcon,
                isLoading = isPrimaryLoading,
                enabled = isPrimaryEnabled && !isSecondaryLoading,
                fillMaxWidth = false,
                height = buttonHeight,
                cornerRadius = cornerRadius,
                textStyle = MaterialTheme.typography.labelLarge,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.weight(1f)
            )

            // دکمه ثانویه (حذف / خاتمه)
            val secondaryColors = when (secondaryVariant) {
                SecondaryButtonVariant.OUTLINED -> ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.error,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.38f)
                )

                SecondaryButtonVariant.FILLED -> ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            }

            val secondaryBorder = if (secondaryVariant == SecondaryButtonVariant.OUTLINED) {
                BorderStroke(
                    width = 1.dp,
                    color = if (isSecondaryEnabled && !isPrimaryLoading) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.error.copy(alpha = 0.38f)
                    }
                )
            } else null

            CustomButton(
                text = secondaryText,
                onClick = onSecondaryClick,
                icon = secondaryIcon,
                isLoading = isSecondaryLoading,
                enabled = isSecondaryEnabled && !isPrimaryLoading,
                fillMaxWidth = false,
                height = buttonHeight,
                cornerRadius = cornerRadius,
                colors = secondaryColors,
                border = secondaryBorder,
                textStyle = MaterialTheme.typography.labelLarge,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

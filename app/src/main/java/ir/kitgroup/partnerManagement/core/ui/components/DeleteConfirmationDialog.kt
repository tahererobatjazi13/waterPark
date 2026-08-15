package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun DeleteConfirmationDialog(
    itemType: String,
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = appColors.cardBackground,
        iconContentColor = colorScheme.error,
        titleContentColor = appColors.textPrimary,
        textContentColor = appColors.textSecondary,
        tonalElevation = AlertDialogDefaults.TonalElevation,
        icon = {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(
                    R.string.label_delete_title,
                    itemType
                ),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = stringResource(
                    R.string.msg_delete_message,
                    itemName
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.label_delete),
                    color = colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.label_cancel),
                    color = colorScheme.primary
                )
            }
        }
    )
}

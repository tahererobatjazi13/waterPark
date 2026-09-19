package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors


@Composable
fun ServerAddressDialog(
    initialAddress: String,
    errorMessage: Int?,
    isLoading: Boolean,
    onDismissRequest: () -> Unit,
    onSaveClick: (String) -> Unit
) {
    var serverAddress by remember(initialAddress) { mutableStateOf(initialAddress) }
    val appColors = LocalPartnerManagementColors.current

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismissRequest() },
        properties = DialogProperties(dismissOnClickOutside = !isLoading),
        containerColor = appColors.cardBackground,
        title = {
            Text(
                text = stringResource(
                    R.string.label_server_setting,
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CustomEditTextField(
                    value = serverAddress,
                    onValueChange = { serverAddress = it },
                    label = stringResource(R.string.label_server_address),
                    placeholder = "178.131.164.145:52438",
                    leadingIcon = null,
                    errorMessage = errorMessage?.let { stringResource(it) }
                )
            }
        },
        confirmButton = {
            CustomButton(
                text = stringResource(R.string.label_save),
                onClick = { onSaveClick(serverAddress) },
                isLoading = isLoading,
                fillMaxWidth = false,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        dismissButton = {
            CustomOutlinedButton(
                text = stringResource(R.string.label_cancellation),
                onClick = onDismissRequest,
                enabled = !isLoading,
                fillMaxWidth = false
            )
        }
    )
}

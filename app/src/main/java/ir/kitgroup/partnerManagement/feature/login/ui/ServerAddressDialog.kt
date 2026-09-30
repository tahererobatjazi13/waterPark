package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun ServerAddressDialog(
    initialAddress: String,
    errorMessage: String?,
    isLoading: Boolean,
    onDismissRequest: () -> Unit,
    onSaveClick: (String) -> Unit
) {
    var serverAddress by remember(initialAddress) {
        mutableStateOf(initialAddress)
    }

    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black.copy(alpha = 0.32f)
            ),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(28.dp),
            color = appColors.cardBackground,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 16.dp
                    )
            ) {

                Text(
                    text = stringResource(
                        R.string.label_server_setting,
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                CustomEditTextField(
                    value = serverAddress,
                    onValueChange = {
                        serverAddress = it
                    },
                    label = stringResource(R.string.label_server_address),
                    placeholder = "192.168.20.112:95",
                    leadingIcon = null,
                    errorMessage = errorMessage
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {

                    CustomButton(
                        text = stringResource(R.string.label_save),
                        onClick = {
                            onSaveClick(serverAddress)
                        },
                        isLoading = isLoading,
                        fillMaxWidth = false,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    CustomOutlinedButton(
                        text = stringResource(R.string.label_cancellation),
                        onClick = onDismissRequest,
                        enabled = !isLoading,
                        fillMaxWidth = false
                    )
                }
            }
        }
    }
}

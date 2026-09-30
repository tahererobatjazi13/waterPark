package ir.kitgroup.partnerManagement.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun CenterInfoScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val centerName by viewModel.centerName.collectAsState()
    val centerBrandName by viewModel.centerBrandName.collectAsState()
    val centerPhone by viewModel.centerPhone.collectAsState()
    val centerSupportPhone by viewModel.centerSupportPhone.collectAsState()
    val centerAddress by viewModel.centerAddress.collectAsState()
    val centerWebsite by viewModel.centerWebsite.collectAsState()
    val centerLatitude by viewModel.centerLatitude.collectAsState()
    val centerLongitude by viewModel.centerLongitude.collectAsState()

    val appColors = LocalPartnerManagementColors.current
    val emptyPlaceholder = stringResource(R.string.placeholder_empty_value)

    val coordinatesValue = if (!centerLatitude.isNullOrBlank() && !centerLongitude.isNullOrBlank()) {
        stringResource(
            R.string.format_geo_coordinates,
            centerLatitude.orEmpty(),
            centerLongitude.orEmpty()
        )
    } else {
        emptyPlaceholder
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_center_information,
            showBackButton = true,
            onBackClick = onBackClick
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CenterTitleCard(
                    name = centerName.orEmpty(),
                    brandName = centerBrandName.orEmpty(),
                    fallbackText = emptyPlaceholder
                )

                CenterInfoRow(
                    icon = Icons.Default.Call,
                    label = stringResource(R.string.label_phone_number),
                    value = centerPhone?.ifBlank { emptyPlaceholder } ?: emptyPlaceholder
                )

                CenterInfoRow(
                    icon = Icons.Default.SupportAgent,
                    label = stringResource(R.string.label_support_phone),
                    value = centerSupportPhone?.ifBlank { emptyPlaceholder } ?: emptyPlaceholder
                )

                CenterInfoRow(
                    icon = Icons.Default.LocationOn,
                    label = stringResource(R.string.label_address),
                    value = centerAddress?.ifBlank { emptyPlaceholder } ?: emptyPlaceholder
                )

                CenterInfoRow(
                    icon = Icons.Default.Language,
                    label = stringResource(R.string.label_website),
                    value = centerWebsite?.ifBlank { emptyPlaceholder } ?: emptyPlaceholder
                )

            /*    CenterInfoRow(
                    icon = Icons.Default.LocationOn,
                    label = stringResource(R.string.label_geo_coordinates),
                    value = coordinatesValue
                )*/
            }
        }
    }
}

@Composable
private fun CenterTitleCard(
    name: String,
    brandName: String,
    fallbackText: String
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Business,
                contentDescription = null,
                tint = appColors.info,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = name.ifBlank { fallbackText },
                    style = MaterialTheme.typography.titleLarge,
                    color = appColors.textPrimary
                )

                if (brandName.isNotBlank()) {
                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = brandName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = appColors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun CenterInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = appColors.info,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = appColors.textSecondary
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = appColors.textPrimary
                )
            }
        }
    }
}

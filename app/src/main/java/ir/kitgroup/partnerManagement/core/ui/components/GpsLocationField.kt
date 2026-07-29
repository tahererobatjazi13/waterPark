package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme

@Composable
fun GpsLocationField(
    label: String
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = colors.onBackground,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = label,
                color = colors.onBackground,
                style = typography.titleMedium,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(PartnerManagementTheme.colors.successContainer)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = PartnerManagementTheme.colors.onSuccessContainer.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(23.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationSearching,
                    contentDescription = null,
                    tint = PartnerManagementTheme.colors.onSuccessContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = PartnerManagementTheme.colors.onSuccessContainer,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = stringResource(R.string.label_gps_success_msg),
                        color = PartnerManagementTheme.colors.onSuccessContainer,
                        style = typography.titleMedium,
                        maxLines = 1
                    )
                }

                Text(
                    text = stringResource(R.string.gps_address_sample),
                    color = colors.onSecondaryContainer,
                    style = typography.labelMedium,
                    maxLines = 2
                )

                Text(
                    text = stringResource(R.string.gps_time_sample),
                    color = colors.onSecondaryContainer.copy(alpha = 0.75f),
                    style = typography.labelSmall,
                    maxLines = 1
                )
            }
        }
    }
}

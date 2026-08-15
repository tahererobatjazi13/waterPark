package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun DateRangeChip(
    startDate: String,
    endDate: String,
    onClick: () -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = appColors.cardBackground,
        border = BorderStroke(0.8.dp, appColors.border),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = appColors.onInfoContainer
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$startDate - $endDate",
                style = MaterialTheme.typography.titleMedium,
                color = appColors.textPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = appColors.textSecondary
            )
        }
    }
}

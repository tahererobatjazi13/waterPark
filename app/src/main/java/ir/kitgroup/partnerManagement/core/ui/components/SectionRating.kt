package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme

@Composable
fun Rating(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < rating)
                    PartnerManagementTheme.colors.ratingActive
                else
                    PartnerManagementTheme.colors.ratingInactive,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


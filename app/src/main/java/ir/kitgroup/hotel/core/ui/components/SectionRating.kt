package ir.kitgroup.hotel.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.kitgroup.hotel.core.ui.theme.HotelTheme

@Composable
fun Rating(rating: Int) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(5) { index ->
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < rating)
                    HotelTheme.colors.ratingActive
                else
                    HotelTheme.colors.ratingInactive, modifier = Modifier.size(16.dp)
            )
        }
    }
}

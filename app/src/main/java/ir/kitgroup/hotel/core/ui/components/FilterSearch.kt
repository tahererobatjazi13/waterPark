package ir.kitgroup.hotel.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.kitgroup.hotel.core.ui.theme.HotelTheme


@Composable
fun FilterSection(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }

    val iconColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        //  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .onFocusChanged { isFocused = it.isFocused }
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            placeholder = {
                Text(
                    text = hint,
                    style = typography.labelMedium,
                    color = HotelTheme.colors.textSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = typography.labelMedium.copy(
                color = HotelTheme.colors.textPrimary,
                textAlign = TextAlign.End
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = HotelTheme.colors.textPrimary,
                unfocusedTextColor = HotelTheme.colors.textPrimary,
                disabledTextColor = HotelTheme.colors.textSecondary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
    //  }
}

package ir.kitgroup.hotel.core.ui.components

import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomDescriptionField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = label,
            style = typography.titleMedium,
            color = colors.onSurface
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(colors.surface, RoundedCornerShape(12.dp))
                .border(1.dp, colors.outline, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= 500) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                textStyle = typography.bodyMedium.copy(
                    color = colors.onSurface,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = colors.onSurfaceVariant,
                            style = typography.labelMedium
                        )
                    }
                    innerTextField()
                }
            )

            Text(
                text = "${value.length}/۵۰۰",
                color = colors.onSurfaceVariant,
                style = typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            )
        }
    }
}

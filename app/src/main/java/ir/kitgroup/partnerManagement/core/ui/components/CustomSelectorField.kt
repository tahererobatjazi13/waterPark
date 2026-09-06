package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection

@Composable
fun CustomSelectorField(
    value: String,
    label: String,
    isExpanded: Boolean,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = true
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = typography.titleMedium,
                color = colors.onSurface
            )
            if (isRequired) {
                Text(
                    text = " *",
                    color = colors.error,
                    style = typography.labelMedium
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = placeholder,
                        style = typography.labelSmall.copy(
                            textAlign = TextAlign.Start,
                            textDirection = TextDirection.Rtl
                        ),
                        color = PartnerManagementTheme.colors.textSecondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (isExpanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = null,
                        tint = colors.primary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                textStyle = typography.bodyMedium.copy(
                    color = PartnerManagementTheme.colors.textPrimary,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = PartnerManagementTheme.colors.textPrimary,
                    disabledBorderColor = colors.outline,
                    disabledPlaceholderColor = PartnerManagementTheme.colors.textSecondary,
                    disabledContainerColor = colors.surface,
                    disabledTrailingIconColor = colors.primary
                )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClick()
                    }
            )
        }
    }
}

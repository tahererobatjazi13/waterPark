package ir.kitgroup.hotel.core.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.kitgroup.hotel.core.ui.theme.HotelTheme

@SuppressLint("ModifierParameter")
@Composable
fun CustomEditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: Painter? = null,
    isPasswordField: Boolean = false,
    isRequired: Boolean = true,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError = !errorMessage.isNullOrBlank()

    val activeColor =
        if (isError) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary

    val iconColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val borderColor =
        if (isError) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.outline

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (isRequired) {
                    Text(
                        text = " *",
                        color = MaterialTheme.colorScheme.error,
                        style = typography.labelMedium
                    )
                }
            }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource,
                isError = isError,
                placeholder = {
                    Text(
                        text = placeholder,
                        modifier = Modifier.fillMaxWidth(),
                        style = typography.bodySmall.copy(
                            textAlign = TextAlign.Start,
                            textDirection = TextDirection.Rtl
                        ),
                        color = HotelTheme.colors.textSecondary
                    )
                },
                leadingIcon = if (leadingIcon != null) {
                    {
                        Icon(
                            painter = leadingIcon,
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                } else null,
                trailingIcon = {
                    if (isPasswordField) {
                        val visibilityIcon =
                            if (isPasswordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible }
                        ) {
                            Icon(
                                imageVector = visibilityIcon,
                                contentDescription = null,
                                tint = iconColor
                            )
                        }
                    }
                },
                visualTransformation = if (isPasswordField && !isPasswordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                textStyle = typography.bodyMedium.copy(
                    color = HotelTheme.colors.textPrimary,
                    textAlign = TextAlign.Start,
                    textDirection = TextDirection.Rtl
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = HotelTheme.colors.textPrimary,
                    unfocusedTextColor = HotelTheme.colors.textPrimary,
                    disabledTextColor = HotelTheme.colors.textSecondary,
                    errorTextColor = MaterialTheme.colorScheme.error,

                    focusedBorderColor = activeColor,
                    unfocusedBorderColor = borderColor,
                    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    errorBorderColor = MaterialTheme.colorScheme.error,

                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface,

                    cursorColor = activeColor,
                    errorCursorColor = MaterialTheme.colorScheme.error
                )
            )

            if (isError) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

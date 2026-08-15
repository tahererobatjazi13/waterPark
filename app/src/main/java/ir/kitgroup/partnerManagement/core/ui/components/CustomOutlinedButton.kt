package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    fillMaxWidth: Boolean = true,
    height: Dp? = 50.dp,
    cornerRadius: Dp = 16.dp,
    iconSize: Dp = 20.dp,
    iconSpacing: Dp = 8.dp,
    textStyle: TextStyle = typography.titleLarge,
    border: BorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.primary
    ),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    var finalModifier = modifier

    if (fillMaxWidth) {
        finalModifier = finalModifier.fillMaxWidth()
    }

    if (height != null) {
        finalModifier = finalModifier.height(height)
    }

    OutlinedButton(
        onClick = onClick,
        modifier = finalModifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        shape = RoundedCornerShape(cornerRadius),
        border = border,
        colors = colors,
        enabled = enabled,
        contentPadding = contentPadding
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )

                Spacer(modifier = Modifier.width(iconSpacing))
            }

            Text(
                text = text,
                style = textStyle
            )
        }
    }
}

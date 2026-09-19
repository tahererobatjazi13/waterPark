package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ), isLoading: Boolean = false,

    enabled: Boolean = true,
    fillMaxWidth: Boolean = true,
    height: Dp? = 50.dp,
    cornerRadius: Dp = 16.dp,
    textStyle: TextStyle = typography.titleLarge,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    var finalModifier = modifier

    if (fillMaxWidth) {
        finalModifier = finalModifier.fillMaxWidth()
    }

    if (height != null) {
        finalModifier = finalModifier.height(height)
    }

    Button(
        onClick = onClick,
        modifier = finalModifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = colors,
        enabled = enabled && !isLoading,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = contentPadding
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = LocalContentColor.current,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = textStyle
                )
            }
        }
    }
}

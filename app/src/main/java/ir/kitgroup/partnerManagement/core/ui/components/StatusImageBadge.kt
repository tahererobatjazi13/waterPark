package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.core.ui.model.VisitStatusStyle

@Composable
fun StatusImageBadge(
    text: String,
    style: VisitStatusStyle,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = style.badgeContainer
    ) {
        Row(
            modifier = Modifier.padding(
                start = 6.dp,
                end = 12.dp,
                top = 6.dp,
                bottom = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = CircleShape,
                    color = style.iconContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = style.iconTint,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Text(
                text = text,
                color = style.badgeContent,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

package ir.kitgroup.hotel.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.hotel.core.ui.model.VisitStatusStyle

@Composable
fun StatusBadge(
    text: String,
    style: VisitStatusStyle,
    icon: ImageVector? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (icon != null) {
            Surface(
                shape = CircleShape,
                color = style.iconContainer,
                modifier = Modifier.size(18.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = style.iconTint,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(50),
            color = style.badgeContainer
        ) {
            Text(
                text = text,
                color = style.badgeContent,
                style = typography.labelSmall,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

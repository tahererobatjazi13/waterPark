package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun GradeSelector(
    grade: Int,
    onGradeChange: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    val normalizedGrade = grade.coerceIn(1, 5)
    val isReadOnly = onGradeChange == null

    val gradeLetters = mapOf(
        5 to "A",
        4 to "B",
        3 to "C",
        2 to "D",
        1 to "E"
    )

    Column {
        Text(
            text = stringResource(R.string.label_organization_grade),
            style = typography.titleMedium,
            color = colors.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, appColors.border, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (i in 5 downTo 1) {
                val isSelected = i <= normalizedGrade

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (!isReadOnly) {
                                Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        onGradeChange?.invoke(i)
                                    }
                            } else {
                                Modifier
                            }
                        )
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSelected) {
                            Icons.Filled.Star
                        } else {
                            Icons.Outlined.StarBorder
                        },
                        contentDescription = null,
                        tint = if (isSelected) {
                            Color(0xFFFFC107)
                        } else {
                            appColors.border
                        },
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = gradeLetters[i].orEmpty(),
                        style = typography.labelSmall,
                        color = if (isSelected) {
                            colors.onSurface
                        } else {
                            appColors.border
                        }
                    )
                }
            }
        }
    }
}

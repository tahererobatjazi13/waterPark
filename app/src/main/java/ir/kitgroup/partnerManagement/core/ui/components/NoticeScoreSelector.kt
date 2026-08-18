package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R

@Composable
fun NoticeScoreSelector(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.label_score),
            style = MaterialTheme.typography.labelLarge,
            color = colors.onSurface
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (itemScore in 1..5) {
                IconButton(
                    onClick = {
                        onScoreChange(itemScore)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = stringResource(
                            R.string.label_description_notice_score,
                            itemScore
                        ),
                        tint = if (itemScore <= score) {
                            Color(0xFFFFB300)
                        } else {
                            colors.outlineVariant
                        },
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        Text(
            text = scoreLevelLabel(score),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = colors.onSurfaceVariant
        )
    }
}

@Composable
private fun scoreLevelLabel(score: Int): String {
    return when (score) {
        1 -> stringResource(R.string.label_notice_score_level_1)
        2 -> stringResource(R.string.label_notice_score_level_2)
        3 -> stringResource(R.string.label_notice_score_level_3)
        4 -> stringResource(R.string.label_notice_score_level_4)
        5 -> stringResource(R.string.label_notice_score_level_5)
        else -> stringResource(R.string.label_choose_score)
    }
}

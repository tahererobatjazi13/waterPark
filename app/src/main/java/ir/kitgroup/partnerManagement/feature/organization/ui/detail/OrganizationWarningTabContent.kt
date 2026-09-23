package ir.kitgroup.partnerManagement.feature.organization.ui.detail


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextOverflow
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationWarningEntity
import ir.kitgroup.partnerManagement.core.ui.components.ActionIconButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.util.demoOrganizationWarnings

@Composable
fun OrganizationWarningTabContent(
    warnings: List<OrganizationWarningEntity>,
    isSupervisor: Boolean,
    onAddWarningClick: () -> Unit,
    onDeleteWarningClick: (OrganizationWarningEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(title = stringResource(R.string.label_warnings_organization))

        CustomButton(
            text = stringResource(R.string.label_register_organization_warning),
            onClick = onAddWarningClick,
            fillMaxWidth = true,
            height = 46.dp,
            cornerRadius = 12.dp,
            textStyle = typography.titleLarge,
            icon = Icons.Filled.RateReview,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        )

        if (warnings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.RateReview,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = stringResource(R.string.label_no_warning_added),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    items = warnings,
                    key = { _, item -> item.organizationWarningId }
                ) { index, warning ->
                    OrganizationWarningCard(
                        index = index,
                        warning = warning,
                        isSupervisor = isSupervisor,
                        onDeleteClick = { onDeleteWarningClick(warning) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizationWarningCard(
    index: Int,
    warning: OrganizationWarningEntity,
    isSupervisor: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            //هدر کارت
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.RateReview,
                            null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "تذکر ${index + 1}: ${warning.name}",
                        style = typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    WarningScoreChip(score = warning.score!!)
                    if (isSupervisor) {
                        ActionIconButton(
                            icon = Icons.Default.DeleteOutline,
                            contentDescription = stringResource(R.string.label_delete),
                            onClick = onDeleteClick,
                            tint = MaterialTheme.colorScheme.error,
                            backgroundColor = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // توضیحات تذکر
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "توضیحات:",
                    style = typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = warning.description!!.ifBlank { "-" },
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // --- تاریخ و ثبت‌کننده ---
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ثبت‌کننده
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = warning.visitorId.toString(),
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )


                }
                // تاریخ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = warning.dateWarning!!,
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // ---------------------------------
            }
        }
    }
}


@Composable
private fun WarningScoreChip(
    score: Int
) {
    val isPositive = score > 0

    // رنگ پس‌زمینه چیپ
    val chipColor = if (isPositive) {
        PartnerManagementTheme.colors.successContainer
    } else {
        PartnerManagementTheme.colors.errorContainer
    }

    // رنگ متن
    val contentColor = if (isPositive) {
        PartnerManagementTheme.colors.success
    } else {
        PartnerManagementTheme.colors.error
    }

    val scoreText = when {
        score > 0 -> "+$score امتیاز"
        score < 0 -> "$score امتیاز"
        else -> "0 امتیاز"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = scoreText,
                style = typography.labelMedium,
                color = contentColor
            )
        }
    }
}

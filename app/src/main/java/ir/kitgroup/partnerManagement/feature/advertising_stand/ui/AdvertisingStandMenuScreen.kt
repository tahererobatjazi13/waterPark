package ir.kitgroup.partnerManagement.feature.advertising_stand.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.util.UserRole

@Composable
fun AdvertisingStandMenuScreen(
    onBackClick: () -> Unit,
    onManageItemsClick: () -> Unit,
    onStandAssignmentVisitorClick: () -> Unit,
    onStandAssignmentOrganizationClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionViewModel = hiltViewModel()
) {

    val role by viewModel.userRole.collectAsState()
    val isSupervisor = role == UserRole.SUPERVISOR.name

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_advertising_stands,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding()
                ),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = MaterialTheme.colorScheme.background
        ) {
            AdvertisingMenuContent(
                isSupervisor = isSupervisor,
                onManageItemsClick = onManageItemsClick,
                onStandAssignmentVisitorClick = onStandAssignmentVisitorClick,
                onStandAssignmentOrganizationClick = onStandAssignmentOrganizationClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

@Composable
private fun AdvertisingMenuContent(
    isSupervisor: Boolean,
    onManageItemsClick: () -> Unit,
    onStandAssignmentVisitorClick: () -> Unit,
    onStandAssignmentOrganizationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.msg_select_one_of_options),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (isSupervisor) {

            item {
                AdvertisingMenuCard(
                    titleRes = R.string.label_managing_stands_items,
                    subtitleRes = R.string.label_add_edit_view_advertising_items,
                    icon = Icons.Filled.Inventory2,
                    iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onManageItemsClick
                )
            }
        }

        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_assignment_advertising_stand,
                subtitleRes = R.string.label_assignment_stands_to_visitor,
                icon = Icons.Filled.PersonAddAlt1,
                iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onStandAssignmentVisitorClick
            )
        }

        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_assignment_advertising_stand,
                subtitleRes = R.string.label_assignment_stands_to_organization,
                icon = Icons.Filled.Business,
                iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onClick = onStandAssignmentOrganizationClick
            )
        }
    }
}


@Composable
private fun AdvertisingMenuCard(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    icon: ImageVector,
    iconContainerColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = colorScheme.outline.copy(alpha = 0.90f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = iconContainerColor,
                contentColor = iconColor
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(27.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(subtitleRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AdvertisingStandMenuScreenPreview() {
    AppScreenPreview {
        AdvertisingStandMenuScreen(
            onBackClick = {},
            onManageItemsClick = {},
            onStandAssignmentVisitorClick = {},
            onStandAssignmentOrganizationClick = {}
        )
    }
}

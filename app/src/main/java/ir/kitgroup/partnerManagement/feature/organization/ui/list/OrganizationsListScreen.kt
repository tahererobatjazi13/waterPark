package ir.kitgroup.partnerManagement.feature.organization.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.FilterSection
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.Rating
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationsViewModel

@Composable
fun OrganizationsListScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToAdd: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrganizationsViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by rememberSaveable { mutableStateOf<OrganizationStatus?>(null) }
    var isWarningOnly by rememberSaveable { mutableStateOf(false) }

    val allOrganizations by viewModel.allOrganizations.collectAsState()

    val statusOptions = remember { OrganizationStatus.entries }

    // فیلتر کردن لیست
    val filteredOrganizations =
        remember(allOrganizations, searchQuery, selectedStatus, isWarningOnly) {
            allOrganizations.filter { item ->
                val matchesSearch = searchQuery.isBlank() ||
                        item.organization.name.orEmpty().contains(searchQuery, ignoreCase = true) ||
                        item.organization.address.orEmpty().contains(searchQuery, ignoreCase = true)

                val matchesStatus = selectedStatus == null ||
                        item.organization.status == selectedStatus!!.id

                val matchesWarning = !isWarningOnly ||
                        (item.organization.countWarning ?: 0) > 0

                matchesSearch && matchesStatus && matchesWarning
            }
        }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = LocalPartnerManagementColors.current.success,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.label_add_new_organization),
                        style = typography.titleLarge
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = R.string.label_organizations_list,
                showBackButton = false
            )
            Surface(
                modifier = modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                ) {
                    FilterSection(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        hint = stringResource(R.string.label_search)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OrganizationsFiltersRow(
                        selectedStatus = selectedStatus,
                        statusOptions = statusOptions,
                        isWarningOnly = isWarningOnly,
                        onStatusSelected = { selectedStatus = it },
                        onClearStatus = { selectedStatus = null },
                        onToggleWarningOnly = { isWarningOnly = !isWarningOnly }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (filteredOrganizations.isEmpty()) {
                        EmptyState(
                            textRes = R.string.msg_no_item_found,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            itemsIndexed(
                                items = filteredOrganizations,
                                key = { _, item -> item.organization.organizationId }
                            ) { index, organization ->
                                OrganizationsCard(
                                    item = organization,
                                    number = index + 1,
                                    onClick = {
                                        onNavigateToDetail(organization.organization.organizationId)
                                    }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizationsFiltersRow(
    selectedStatus: OrganizationStatus?,
    statusOptions: List<OrganizationStatus>,
    isWarningOnly: Boolean,
    onStatusSelected: (OrganizationStatus) -> Unit,
    onClearStatus: () -> Unit,
    onToggleWarningOnly: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    var statusExpanded by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Row(
                    modifier = Modifier.height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                        tint = appColors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.label_filters),
                        color = appColors.textSecondary,
                        style = typography.titleSmall
                    )
                }
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    Surface(
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = appColors.cardBackground,
                        border = BorderStroke(
                            1.dp,
                            if (selectedStatus != null) MaterialTheme.colorScheme.primary else appColors.border
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedStatus?.let { stringResource(it.titleRes) }
                                    ?: stringResource(R.string.label_status),
                                style = typography.labelSmall,
                                color = if (selectedStatus != null) MaterialTheme.colorScheme.primary else appColors.textPrimary,
                                maxLines = 1,
                                modifier = Modifier.clickable { statusExpanded = true }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (selectedStatus != null) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = appColors.textSecondary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        if (selectedStatus != null) {
                                            onClearStatus()
                                        } else {
                                            statusExpanded = !statusExpanded
                                        }
                                    }
                            )
                        }
                    }

                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(status.titleRes),
                                        style = typography.labelMedium
                                    )
                                },
                                onClick = {
                                    onStatusSelected(status)
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                val warningBorderColor = if (isWarningOnly) appColors.warning else appColors.border
                val warningBgColor =
                    if (isWarningOnly) appColors.warning.copy(alpha = 0.12f) else appColors.cardBackground
                val warningTextColor =
                    if (isWarningOnly) appColors.warning else appColors.textPrimary

                Surface(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onToggleWarningOnly() },
                    shape = RoundedCornerShape(8.dp),
                    color = warningBgColor,
                    border = BorderStroke(1.dp, warningBorderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WarningAmber,
                            contentDescription = null,
                            tint = if (isWarningOnly) appColors.warning else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.label_has_warnings),
                            style = typography.labelSmall,
                            color = warningTextColor,
                            maxLines = 1
                        )
                        if (isWarningOnly) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = appColors.warning,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onToggleWarningOnly() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrganizationsCard(
    item: OrganizationWithDetail,
    number: Int,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    val warningCount = item.organization.countWarning ?: 0
    val orgGrade = item.organization.grade ?: 0
    val orgName = item.organization.name ?: ""
    val orgAddress = item.organization.address ?: ""

    val locationText = when {
        !item.organization.address.isNullOrBlank() -> {
            item.organization.address!!
        }

        !item.cityName.isNullOrBlank() || !item.regionName.isNullOrBlank() -> {
            listOf(
                item.cityName,
                item.regionName
            ).filter { it.isNotBlank() }.joinToString("، ")
        }

        else -> {
            "-"
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = colors.outline.copy(alpha = 0.90f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*  Image(
                  painter = painterResource(R.drawable.ic_logo),
                  contentDescription = null,
                  modifier = Modifier
                      .size(70.dp)
                      .clip(RoundedCornerShape(12.dp)),
                  contentScale = ContentScale.Crop
              )

              Spacer(modifier = Modifier.width(10.dp))*/

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$number.",
                            style = typography.titleLarge,
                            color = colors.onSurface
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = orgName,
                            style = typography.titleLarge,
                            color = colors.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    StatusBadge(status = OrganizationStatus.fromId(item.organization.status))
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Rating(orgGrade)

                    if (warningCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = appColors.warning.copy(alpha = 0.15f),
                            border = BorderStroke(
                                width = 0.4.dp,
                                color = appColors.warning
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 6.dp,
                                    vertical = 2.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.WarningAmber,
                                    contentDescription = null,
                                    tint = appColors.warning,
                                    modifier = Modifier.size(12.dp)
                                )

                                Text(
                                    text = "$warningCount تذکر",
                                    style = typography.labelSmall,
                                    color = appColors.warning
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LocationRow(location = locationText)
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OrganizationsListScreenPreview() {
    AppScreenPreview {
        OrganizationsListScreen(
            onNavigateToDetail = {},
            onNavigateToAdd = {}
        )
    }
}

package ir.kitgroup.partnerManagement.feature.card.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.FilterSection
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.core.ui.util.CardStatusFilter
import ir.kitgroup.partnerManagement.feature.card.model.CardModel

@Composable
fun CardsScreen(
    onRegisterCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(CardStatusFilter.All) }

    val cards = remember {
        listOf(
            CardModel(
                title = "کارت تخفیف",
                cardNumber = "1405 200 01",
                collectionName = "هتل پارسیان آزادی",
                date = "1403/02/28",
                status =Status.USED
            ),
            CardModel(
                title = "کارت تخفیف",
                cardNumber = "1405 200 02",
                collectionName = "هتل اسپیناس پالاس",
                date = "1403/03/01",
                status = Status.DELIVERED
            ),
            CardModel(
                title = "کارت تخفیف",
                cardNumber = "1405 200 03",
                collectionName = "هتل پارسیان کوثر",
                date = "1403/03/03",
                status = Status.USED
            )
        )
    }

    val filteredCards = remember(cards, searchQuery, selectedStatus) {
        cards.filter { card ->
            val matchesSearch =
                searchQuery.isBlank() || card.cardNumber.contains(searchQuery, ignoreCase = true)

            val matchesStatus = when (selectedStatus) {
                CardStatusFilter.All -> true
                CardStatusFilter.Used -> card.status == Status.USED
                CardStatusFilter.Delivered -> card.status == Status.DELIVERED
            }

            matchesSearch && matchesStatus
        }

    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onRegisterCardClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_registration_delivery_card),
                    style = typography.titleMedium
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            CustomHeader(
                title = R.string.label_cards_list,
                showBackButton = false
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
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
                        hint = stringResource(R.string.hint_search_card_number)
                    )

                    Spacer(Modifier.height(8.dp))

                    CardFiltersRow(
                        selectedStatus = selectedStatus,
                        onStatusSelected = { selectedStatus = it }
                    )

                    Spacer(Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = filteredCards,
                            key = { it.cardNumber }
                        ) { card ->
                            CardItemView(card = card)
                        }

                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardFiltersRow(
    selectedStatus: CardStatusFilter,
    onStatusSelected: (CardStatusFilter) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.infoContainer,
                contentColor = appColors.onInfoContainer
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier
                .height(40.dp)
                .wrapContentWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = null,
                tint = appColors.info,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.label_filters),
                color = appColors.textPrimary,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            Surface(
                modifier = Modifier
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = appColors.cardBackground,
                border = BorderStroke(
                    1.dp,
                    appColors.border
                )
            ) {
                Row(
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = selectedStatus.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = stringResource(selectedStatus.titleRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = appColors.textPrimary,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = appColors.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                CardStatusFilter.entries.forEach { status ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(status.titleRes),
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {
                            onStatusSelected(status)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CardItemView(card: CardModel) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = appColors.border
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = appColors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = card.cardNumber,
                    color = appColors.textPrimary,
                    style = typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = card.collectionName,
                    color = appColors.textSecondary,
                    style = typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                DetailRow(
                    icon = Icons.Default.CalendarToday,
                    text = card.date
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.heightIn(min = 92.dp)
            ) {

                StatusBadge(card.status)

                Spacer(modifier = Modifier.height(18.dp))

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = appColors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    text: String
) {
    val appColors = LocalPartnerManagementColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = appColors.textSecondary
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            textAlign = TextAlign.End,
            color = appColors.textSecondary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun CardsScreenPreview() {
    AppScreenPreview {
        CardsScreen(
            onRegisterCardClick = {}
        )
    }
}


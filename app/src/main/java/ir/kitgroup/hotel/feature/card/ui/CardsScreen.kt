package ir.kitgroup.hotel.feature.card.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.components.FilterSection
import ir.kitgroup.hotel.core.ui.theme.HotelTheme
import ir.kitgroup.hotel.core.ui.util.CardStatus
import ir.kitgroup.hotel.core.ui.util.CardStatusFilter
import ir.kitgroup.hotel.core.ui.util.extensions.labelRes
import ir.kitgroup.hotel.feature.card.model.CardModel
import ir.kitgroup.hotel.navigation.Screen


@Composable
fun CardsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(CardStatusFilter.All) }

    val cards = remember {
        listOf(
            CardModel(
                title = "کارت تخفیف",
                cardNumber = "1405 200 01",
                hotelName = "هتل پارسیان آزادی",
                date = "1403/02/28",
                status = CardStatus.Used
            ),
            CardModel(
                title = "کارت تخفیف",
                cardNumber = "1405 200 02",
                hotelName = "هتل اسپیناس پالاس",
                date = "1403/03/01",
                status = CardStatus.Delivered
            ),
            CardModel(
                title = "کارت تخفیف",
                cardNumber = "1405 200 03",
                hotelName = "هتل پارسیان کوثر",
                date = "1403/03/03",
                status = CardStatus.Used
            )
        )
    }

    val filteredCards = remember(cards, searchQuery, selectedStatus) {
        cards.filter { card ->
            val matchesSearch =
                searchQuery.isBlank() || card.cardNumber.contains(searchQuery, ignoreCase = true)

            val matchesStatus = when (selectedStatus) {
                CardStatusFilter.All -> true
                CardStatusFilter.Used -> card.status == CardStatus.Used
                CardStatusFilter.Delivered -> card.status == CardStatus.Delivered
            }

            matchesSearch && matchesStatus
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.RegisterCard.route) },
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
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = HotelTheme.colors.infoContainer,
                contentColor = HotelTheme.colors.onInfoContainer
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
                tint = HotelTheme.colors.info,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.label_filters),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = typography.labelSmall
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
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
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
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                style = typography.labelMedium
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
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
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = card.cardNumber,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = card.hotelName,
                    color = MaterialTheme.colorScheme.onSurface,
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
                StatusChip(status = card.status)

                Spacer(modifier = Modifier.height(18.dp))

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = HotelTheme.colors.textSecondary
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = typography.labelSmall
        )
    }
}

@Composable
private fun StatusChip(status: CardStatus) {
    val text = stringResource(status.labelRes())

    val (bg, fg) = when (status) {
        CardStatus.Used ->
            HotelTheme.colors.success.copy(alpha = 0.12f) to HotelTheme.colors.success

        CardStatus.Delivered ->
            HotelTheme.colors.info.copy(alpha = 0.12f) to HotelTheme.colors.info
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = text,
            color = fg,
            style = typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardListScreenPreview() {
    MaterialTheme {
        // CardsScreen(...)
    }
}

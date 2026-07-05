package ir.kitgroup.hotel.feature.visits.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ir.kitgroup.hotel.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.components.FilterChip
import ir.kitgroup.hotel.core.ui.components.FilterSection
import ir.kitgroup.hotel.core.ui.model.FilterItem
import ir.kitgroup.hotel.core.ui.theme.HotelTheme
import ir.kitgroup.hotel.feature.visits.model.VisitModel
import ir.kitgroup.hotel.navigation.Screen

@Composable
fun VisitsScreen(navController: NavController) {

    val items = listOf(
        VisitModel(
            1,
            "هتل قصر طلایی",
            "بازدید حضوری",
            "علی محمدی",
            "۱۴۰۳/۰۲/۱۵",
            Icons.Default.DirectionsWalk
        ),
        VisitModel(
            2,
            "هتل پارسیان آزادی",
            "تماس تلفنی",
            "مریم رضایی",
            "۱۴۰۳/۰۲/۱۵",
            Icons.Default.Phone
        ),
        VisitModel(
            3,
            "هتل الماس",
            "بازدید حضوری",
            "علی رضایی",
            "۱۴۰۳/۰۲/۱۵",
            Icons.Default.DirectionsWalk
        ),
        VisitModel(
            4,
            "هتل پارسیان",
            "تماس تلفنی",
            "مریم مفرد",
            "۱۴۰۳/۰۲/۱۵",
            Icons.Default.Phone
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_visits_list,
            showBackButton = false
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
            ) {
                FilterSection()
                Spacer(Modifier.height(8.dp))

                VisitFiltersRow()

                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(items) { item ->
                        VisitCard(
                            item = item,
                            onClick = {
                                navController.navigate(Screen.VisitDetail.createRoute(item.id))
                            }
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }

            }
        }
    }

}

@Composable
fun VisitFiltersRow() {
    val filters = listOf(
        FilterItem(R.string.label_date, Icons.Default.DateRange),
        FilterItem(R.string.label_visitor, Icons.Default.Person)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            items(filters) { filter ->

                FilterChip(
                    text = stringResource(filter.titleRes),
                    icon = filter.icon
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = HotelTheme.colors.infoContainer,
                contentColor = HotelTheme.colors.onInfoContainer
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier
                .height(36.dp)
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
    }
}

@Composable
fun VisitCard(
    item: VisitModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.90f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = item.title,
                    textAlign = TextAlign.End,
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                VisitTypeChip(
                    text = item.type,
                    icon = item.icon
                )

                Spacer(Modifier.height(6.dp))

                DetailRow(Icons.Default.Person, item.person)

                Spacer(Modifier.height(6.dp))

                DetailRow(Icons.Default.DateRange, item.date)
            }

            Spacer(Modifier.width(12.dp))

            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Text(
            textAlign = TextAlign.End,
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = typography.labelSmall
        )

        Spacer(Modifier.width(4.dp))

        Icon(
            icon,
            null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun VisitTypeChip(
    text: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface,
            style = typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 6.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

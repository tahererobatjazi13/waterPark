package ir.kitgroup.partnerManagement.feature.collaborative_collection.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.Rating
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun CollectionDetailScreen(
    collectionId: Int,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDisableClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_collections_details,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                InfoGrid()

                Spacer(Modifier.height(12.dp))

                SectionTitle(stringResource(R.string.label_collection_images))

                Spacer(Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(3) { index ->
                        ImageCard(
                            imageRes = when (index) {
                                0 -> R.drawable.ic_logo
                                1 -> R.drawable.ic_logo
                                else -> R.drawable.ic_logo
                            }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                AddressMapCard()

                Spacer(Modifier.height(12.dp))

                RatingSection()

                Spacer(Modifier.height(12.dp))

                ActionButtons(
                    onEdit = onEditClick,
                    onDisable = onDisableClick
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ImageCard(imageRes: Int) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(width = 110.dp, height = 88.dp)
            .clip(RoundedCornerShape(14.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun InfoGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_name_collaborative),
                value = "هتل آبی پارسیان",
                icon = Icons.Filled.Business
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_capacity),
                value = "120 نفر",
                icon = Icons.Filled.Groups
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_name_group_manager),
                value = "احمد رضایی",
                icon = Icons.Filled.ManageAccounts
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_name_recipient),
                value = "حسینی",
                icon = Icons.Filled.Person
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_reception_phone),
                value = "02112345678",
                icon = Icons.Filled.Phone
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_mobile_number),
                value = "09129876543",
                icon = Icons.Filled.Smartphone
            )
        }
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(colors.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(15.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    style = typography.labelSmall,
                    color = colors.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = value,
                    style = typography.titleSmall,
                    color = colors.onSurface
                )
            }
        }
    }
}

@Composable
private fun AddressMapCard() {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 92.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.label_address),
                    style = typography.labelMedium,
                    color = colors.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "تهران: تهران بلوار آبشار، پارک آبی",
                    style = typography.bodyMedium,
                    color = colors.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}


@Composable
private fun RatingSection() {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.label_rating_evaluation),
                    style = typography.titleSmall,
                    color = appColors.textPrimary
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_save),
                        style = typography.labelLarge
                    )
                }
                Spacer(Modifier.width(20.dp))

                Rating(rating = 4)
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onEdit: () -> Unit,
    onDisable: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onEdit,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.label_edit_collection))
        }

        Button(
            onClick = onDisable,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.NotInterested,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.label_deactivation))
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun CollectionDetailScreenPreview() {
    AppScreenPreview {
        CollectionDetailScreen(
            collectionId = 1,
            onBackClick = {},
            onEditClick = {},
            onDisableClick = {}
        )
    }
}
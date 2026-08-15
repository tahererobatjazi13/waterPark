package ir.kitgroup.partnerManagement.feature.organization.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.Rating
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.organization.model.PersonOrganization

@Composable
fun OrganizationDetailScreen(
    organizationId: Int,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDisableClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current


    val persons = listOf(
        PersonOrganization(
            name = "علی حسینی",
            mobile = "09121234567",
            phone = "02112345678",
            gender = "آقا",
            status = "فعال",
            description = "مدیر داخلی هتل"
        ),
        PersonOrganization(
            name = "مریم احمدی",
            mobile = "09351234567",
            phone = "",
            gender = "خانم",
            status = "فعال",
            description = "مسئول پذیرش"
        )
    )

    val images = listOf(
        R.drawable.ic_logo,
        R.drawable.ic_logo,
        R.drawable.ic_logo,
        R.drawable.ic_logo
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_details,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                SectionTitle(stringResource(R.string.label_organization_basic_information))

                BasicInfoSection()

                SectionTitle(stringResource(R.string.label_contact_information))

                ContactInfoSection()

                SectionTitle(stringResource(R.string.label_location))

                AddressMapCard(
                    address = "تهران، بلوار آبشار، پارک آبی"
                )

                SectionTitle(stringResource(R.string.label_analytical_information))

                AnalysisInfoSection()

                SectionTitle(stringResource(R.string.label_statuses))

                StatusInfoSection()

                SectionTitle(stringResource(R.string.label_related_persons))

                RelatedPersonsDetailSection(
                    persons = persons
                )

                SectionTitle(stringResource(R.string.label_description))

                DescriptionCard(
                    description = "این سازمان از مشتریان فعال مجموعه است و تمایل به دریافت استند تبلیغاتی دارد."
                )

                SectionTitle(stringResource(R.string.label_images))

                ImagesSection(
                    images = images
                )

                RatingSection()

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
private fun BasicInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.label_organization_name),
            value = "هتل آبی پارسیان",
            icon = Icons.Filled.Business,
            isMainTitle = true
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_organization_type),
                    value = "هتل",
                    icon = Icons.Filled.Category
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_organization_level),
                    value = "معمولی",
                    icon = Icons.Filled.WorkspacePremium
                )
            }
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_owner_name),
                    value = "حسینی",
                    icon = Icons.Filled.ManageAccounts
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_english_name),
                    value = "parsian",
                    icon = Icons.Filled.Language
                )
            }
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_national_id),
                    value = "125",
                    icon = Icons.Filled.Badge
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_organization_status),
                    value = "فعال",
                    icon = Icons.Filled.Verified
                )
            }
        )
    }
}

@Composable
private fun ContactInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_organization_land_line),
                    value = "025685",
                    icon = Icons.Filled.Phone
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_phone),
                    value = "02112345678",
                    icon = Icons.Filled.Phone
                )
            }
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_mobile),
                    value = "09129876543",
                    icon = Icons.Filled.Smartphone
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_email),
                    value = "info@parsian.com",
                    icon = Icons.Filled.Email
                )
            }
        )
    }
}

@Composable
private fun AnalysisInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        GradeDisplayCard(
            grade = 4
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_ticket_sale_count_history),
                    value = "1250",
                    icon = Icons.Filled.Star
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_customer_capacity),
                    value = "350 نفر",
                    icon = Icons.Filled.Groups
                )
            }
        )
    }
}

@Composable
private fun StatusInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        TwoColumnInfoRow(
            start = {
                BooleanStatusCard(
                    title = stringResource(R.string.label_foreign_guest_reception),
                    enabled = true
                )
            },
            end = {
                BooleanStatusCard(
                    title = stringResource(R.string.label_desire_receive_advertising_stands),
                    enabled = false
                )
            }
        )
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    isMainTitle: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = if (isMainTitle) 14.dp else 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(if (isMainTitle) 38.dp else 30.dp)
                    .background(colors.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(if (isMainTitle) 20.dp else 15.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = typography.labelSmall,
                    color = colors.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = value.ifBlank { "-" },
                    style = if (isMainTitle) typography.titleMedium else typography.titleSmall,
                    color = colors.onSurface,
                    maxLines = if (isMainTitle) 2 else 1
                )
            }
        }
    }
}

@Composable
private fun TwoColumnInfoRow(
    start: @Composable () -> Unit,
    end: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            start()
        }

        Box(modifier = Modifier.weight(1f)) {
            end()
        }
    }
}

@Composable
private fun AddressMapCard(
    address: String
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
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

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.label_address),
                    style = typography.labelMedium,
                    color = colors.onSurfaceVariant
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = address.ifBlank { "-" },
                    style = typography.bodyMedium,
                    color = colors.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun GradeDisplayCard(
    grade: Int
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.label_organization_grade),
                style = typography.labelSmall,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 5 downTo 1) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (i <= grade) {
                            androidx.compose.ui.graphics.Color(0xFFFFC107)
                        } else {
                            colors.outlineVariant
                        },
                        modifier = Modifier.size(28.dp)
                    )

                    if (i != 1) {
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BooleanStatusCard(
    title: String,
    enabled: Boolean
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = typography.labelSmall,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (enabled) stringResource(R.string.label_has) else stringResource(R.string.label_has_not),
                style = typography.titleSmall,
                color = colors.onSurface
            )
        }
    }
}

@Composable
private fun RelatedPersonsDetailSection(
    persons: List<PersonOrganization>
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(1.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (persons.isEmpty()) {
                Text(
                    text = stringResource(R.string.label_no_person_added),
                    style = typography.bodyMedium,
                    color = appColors.textSecondary
                )
            } else {
                Text(
                    text = "تعداد اشخاص مرتبط: ${persons.size}",
                    style = typography.labelMedium,
                    color = appColors.textSecondary
                )

                HorizontalDivider(
                    color = appColors.border.copy(alpha = 0.6f)
                )

                persons.forEachIndexed { index, person ->
                    RelatedPersonDetailCard(
                        index = index,
                        person = person
                    )
                }
            }
        }
    }
}

@Composable
private fun RelatedPersonDetailCard(
    index: Int,
    person: PersonOrganization
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackgroundAlt
        ),
        border = BorderStroke(
            width = 1.dp,
            color = appColors.border.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.People,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${index + 1}. ${person.name}".trim(),
                        style = typography.titleSmall,
                        color = appColors.textPrimary
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(
                            status = personStatusToStatus(person.status)
                        )

                        if (person.gender.isNotBlank()) {
                            Text(
                                text = person.gender,
                                style = typography.labelSmall,
                                color = appColors.textSecondary
                            )
                        }
                    }
                }
            }

            PersonInfoRow(
                title = stringResource(R.string.label_mobile),
                value = person.mobile
            )

            PersonInfoRow(
                title = stringResource(R.string.label_phone),
                value = person.phone
            )

            PersonInfoRow(
                title = stringResource(R.string.label_description),
                value = person.description
            )
        }
    }
}

@Composable
private fun DescriptionCard(
    description: String
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = description.ifBlank { "-" },
                style = typography.bodyMedium,
                color = colors.onSurface,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun ImagesSection(
    images: List<Int>
) {
    if (images.isEmpty()) {
        Text(
            text = "تصویری ثبت نشده است",
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    ) {
        items(images.size) { index ->
            ImageCard(
                imageRes = images[index]
            )
        }
    }
}

@Composable
private fun ImageCard(
    imageRes: Int
) {
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
private fun RatingSection() {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
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

            Text(
                text = stringResource(R.string.label_edit_collection)
            )
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

            Text(
                text = stringResource(R.string.label_deactivation)
            )
        }
    }
}

@Composable
private fun PersonInfoRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    if (value.isBlank()) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$title:",
            style = typography.labelMedium,
            color = appColors.textSecondary,
            maxLines = 1
        )

        Text(
            text = value,
            style = typography.labelMedium,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

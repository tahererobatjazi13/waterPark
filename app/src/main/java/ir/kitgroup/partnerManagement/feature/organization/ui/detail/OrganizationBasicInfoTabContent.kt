package ir.kitgroup.partnerManagement.feature.organization.ui.detail


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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.components.GradeSelector
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Storefront
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionCard
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus

@Composable
fun OrganizationBasicInfoTabContent(organization: OrganizationEntity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(stringResource(R.string.label_organization_basic_information))
        BasicInfoSection(organization)

        SectionTitle(stringResource(R.string.label_contact_information))
        ContactInfoSection()

        SectionTitle(stringResource(R.string.label_location))
        AddressMapCard(
            city = organization.cityId!!,
            region = organization.regionId!!,
            address = organization.address!!
        )

        SectionTitle(stringResource(R.string.label_analytical_information))
        AnalysisInfoSection(grade = organization.grade!!)

        SectionTitle(stringResource(R.string.label_statuses))
        StatusInfoSection()

        SectionTitle(stringResource(R.string.label_description))
        CustomDescriptionCard(description = "این سازمان از مشتریان فعال است...")

        SectionTitle(stringResource(R.string.label_images))
        ImagesSection(images = listOf(R.drawable.ic_logo))

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun BasicInfoSection(
    organization: OrganizationEntity
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // کادر یکپارچه اطلاعات پایه سازمان
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            border = BorderStroke(1.dp, colors.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                InfoRowItem(
                    title = stringResource(R.string.label_organization_name),
                    value = organization.name!!,
                    icon = Icons.Filled.Business,
                    isHighlighted = true
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_organization_type),
                    value = "هتل",
                    icon = Icons.Filled.Category
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_organization_level),
                    value = "معمولی",
                    icon = Icons.Filled.WorkspacePremium
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_owner_name),
                    value = "حسینی",
                    icon = Icons.Filled.ManageAccounts
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_english_name),
                    value = "parsian",
                    icon = Icons.Filled.Language
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_national_id),
                    value = "125",
                    icon = Icons.Filled.Badge
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                // نمایش وضعیت
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_status),
                        style = typography.bodyMedium,
                        color = appColors.textSecondary
                    )

                    StatusBadge(
                        status = OrganizationStatus.fromId(organization.status)
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val colors = MaterialTheme.colorScheme

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            border = BorderStroke(1.dp, colors.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                InfoRowItem(
                    title = stringResource(R.string.label_organization_land_line),
                    value = "025685",
                    icon = Icons.Filled.Phone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_phone),
                    value = "0512336888",
                    icon = Icons.Filled.Phone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_mobile),
                    value = "09129876543",
                    icon = Icons.Filled.Smartphone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_email),
                    value = "info@parsian.com",
                    icon = Icons.Filled.Email
                )
            }
        }
    }
}

@Composable
private fun InfoRowItem(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isHighlighted) colors.primary else colors.onSurfaceVariant
            )
            Text(
                text = title,
                style = typography.labelMedium,
                color = colors.onSurfaceVariant
            )
        }

        Text(
            text = value.ifBlank { "—" },
            style = if (isHighlighted) typography.titleLarge else typography.labelLarge,
            color = if (isHighlighted) colors.primary else colors.onSurface
        )
    }
}


@Composable
private fun AddressMapCard(
    city: String = "",
    region: String = "",
    address: String = ""
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
            // باکس آیکون موقعیت مکانی / پیش‌نمایش نقشه
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 96.dp)
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

            // ستون نمایش جزئیات شهر، منطقه و آدرس
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // نمایش شهر و منطقه در صورت وجود
                val locationScope = listOf(city.trim(), region.trim())
                    .filter { it.isNotBlank() }
                    .joinToString("، ")

                if (locationScope.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stringResource(R.string.label_city)} / ${stringResource(R.string.label_region)}:",
                            style = typography.labelSmall,
                            color = colors.onSurfaceVariant
                        )
                        Text(
                            text = locationScope,
                            style = typography.labelMedium,
                            color = colors.onSurface
                        )
                    }
                }

                // عنوان و متن آدرس
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_address) + ":",
                        style = typography.labelSmall,
                        color = colors.onSurfaceVariant
                    )

                    Text(
                        text = address.ifBlank { "-" },
                        style = typography.bodyMedium,
                        color = colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalysisInfoSection(
    grade: Int,
    ticketSaleCount: String = "1250",
    customerCapacity: String = "350 نفر",
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── بخش درجه‌بندی ──
        GradeSelector(
            grade = grade
        )

        // ── کارت یکپارچه اطلاعات آماری/تحلیلی ──
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
                    .padding(vertical = 4.dp)
            ) {
                // سابقه تعداد فروش بلیت
                InfoRowItem(
                    title = stringResource(R.string.label_ticket_sale_count_history),
                    value = ticketSaleCount,
                    icon = Icons.Outlined.ConfirmationNumber,
                    isHighlighted = false
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.5f)
                )

                // ظرفیت مشتریان
                InfoRowItem(
                    title = stringResource(R.string.label_customer_capacity),
                    value = customerCapacity,
                    icon = Icons.Outlined.Groups,
                    isHighlighted = false
                )
            }
        }
    }
}

@Composable
private fun StatusInfoSection(
    hasForeignGuests: Boolean = false,
    wantsAdvertisingStands: Boolean = false,
    modifier: Modifier = Modifier
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            // ── پذیرش مهمان خارجی ──
            InfoRowItem(
                title = stringResource(R.string.label_foreign_guest_reception),
                value = if (hasForeignGuests) {
                    stringResource(R.string.label_has)
                } else {
                    stringResource(R.string.label_has_not)
                },
                icon = Icons.Outlined.Public,
                isHighlighted = hasForeignGuests
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                thickness = 0.8.dp,
                color = colors.outlineVariant.copy(alpha = 0.5f)
            )

            // ── تمایل به دریافت استند تبلیغاتی ──
            InfoRowItem(
                title = stringResource(R.string.label_desire_receive_advertising_stands),
                value = if (wantsAdvertisingStands) {
                    stringResource(R.string.label_has)
                } else {
                    stringResource(R.string.label_has_not)
                },
                icon = Icons.Outlined.Storefront,
                isHighlighted = wantsAdvertisingStands
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
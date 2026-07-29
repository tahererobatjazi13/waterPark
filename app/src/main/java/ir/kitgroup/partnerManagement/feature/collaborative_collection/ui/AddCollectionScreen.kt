package ir.kitgroup.partnerManagement.feature.collaborative_collection.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun AddCollectionScreen(
    onBackClick: () -> Unit,
    onCancel: () -> Unit,
    onSaveClick: () -> Unit,
    onSelectLocation: () -> Unit,
    onAddImage: () -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current

    var collectionDegree by rememberSaveable { mutableIntStateOf(3) }
    var collaborativeName by rememberSaveable { mutableStateOf("") }
    var managerName by rememberSaveable { mutableStateOf("") }
    var capacity by rememberSaveable { mutableStateOf("") }
    var receptionPhone by rememberSaveable { mutableStateOf("") }
    var receptionName by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var mobileNumber by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var arabGuestReception by rememberSaveable { mutableStateOf(true) }
    var collectionStatus by rememberSaveable { mutableStateOf(true) }

    val images = remember {
        mutableStateListOf(
            R.drawable.ic_logo,
            R.drawable.ic_logo,
            R.drawable.ic_logo,
            R.drawable.ic_logo
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_add_new_collection,
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
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomEditTextField(
                    value = collaborativeName,
                    onValueChange = { collaborativeName = it },
                    label = stringResource(R.string.label_name_collaborative),
                    placeholder = stringResource(R.string.hint_enter_name_collection),
                    leadingIcon = null,
                )

                TwoColumnRow(
                    start = {
                        CustomEditTextField(
                            value = managerName,
                            onValueChange = { managerName = it },
                            label = stringResource(R.string.label_name_group_manager),
                            placeholder = stringResource(R.string.hint_enter_name_group_manager),
                            leadingIcon = null,
                        )
                    },
                    end = {
                        CustomEditTextField(
                            value = receptionName,
                            onValueChange = { receptionName = it },
                            label = stringResource(R.string.label_name_recipient),
                            placeholder = stringResource(R.string.hint_enter_name_recipient),
                            leadingIcon = null,
                        )
                    }
                )

                TwoColumnRow(
                    start = {
                        CustomEditTextField(
                            value = capacity,
                            onValueChange = { capacity = it },
                            label = stringResource(R.string.label_capacity),
                            placeholder = stringResource(R.string.hint_enter_collection_capacity),
                            leadingIcon = null,
                        )
                    },
                    end = {
                        DegreeSelector(
                            degree = collectionDegree,
                            onDegreeChange = { collectionDegree = it }
                        )
                    }
                )

                TwoColumnRow(
                    start = {
                        CustomEditTextField(
                            value = receptionPhone,
                            onValueChange = { receptionPhone = it },
                            label = stringResource(R.string.label_reception_phone),
                            placeholder = stringResource(R.string.hint_enter_reception_phone),
                            leadingIcon = null,
                        )
                    },
                    end = {
                        CustomEditTextField(
                            value = mobileNumber,
                            onValueChange = { mobileNumber = it },
                            label = stringResource(R.string.label_mobile_number),
                            placeholder = stringResource(R.string.hint_enter_mobile_number),
                            leadingIcon = null,
                        )
                    }
                )

                CustomEditTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = stringResource(R.string.label_address),
                    placeholder = stringResource(R.string.hint_enter_address),
                    leadingIcon = null,
                )

                Spacer(Modifier.height(4.dp))
                SectionTitle(stringResource(R.string.label_location))
                MapCard(onSelectLocation = onSelectLocation)
                Spacer(Modifier.height(8.dp))

                GuestReceptionToggle(
                    value = arabGuestReception,
                    onValueChange = { arabGuestReception = it }
                )
                StatusToggle(
                    value = collectionStatus,
                    onValueChange = { collectionStatus = it }
                )

                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                SectionTitle(stringResource(R.string.label_collection_images))

                CollectionImagesSection(
                    images = images,
                    onAddImage = onAddImage,
                    onRemoveImage = { index -> images.removeAt(index) }
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_save),
                        onClick = onSaveClick,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = stringResource(R.string.label_cancel),
                            style = typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TwoColumnRow(
    start: @Composable () -> Unit,
    end: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) { start() }
        Box(modifier = Modifier.weight(1f)) { end() }
    }
}

@Composable
private fun DegreeSelector(
    degree: Int,
    onDegreeChange: (Int) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_collection_grade),
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, appColors.border, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 5 downTo 1) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDegreeChange(i) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (i <= degree) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = if (i <= degree) Color(0xFFFFC107) else appColors.border
                    )
                    Text(
                        text = i.toString(),
                        style = typography.labelSmall,
                        color = appColors.border
                    )
                }
            }
        }
    }
}

@Composable
private fun MapCard(
    onSelectLocation: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(appColors.cardBackgroundAlt)
    ) {
        Button(
            onClick = onSelectLocation,
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("انتخاب موقعیت")
        }
    }
}

@Composable
private fun GuestReceptionToggle(
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label_arab_guest_reception),
            style = typography.titleMedium,
            textAlign = TextAlign.End
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_has_not),
                style = typography.labelMedium
            )
            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = value,
                onCheckedChange = onValueChange
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.label_has),
                style = typography.labelMedium
            )
        }
    }
}

@Composable
private fun StatusToggle(
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label_collection_status),
            style = typography.titleMedium,
            textAlign = TextAlign.End
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_inactive),
                style = typography.labelMedium
            )
            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = value,
                onCheckedChange = onValueChange
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.label_active),
                style = typography.labelMedium
            )
        }
    }
}

@Composable
private fun CollectionImagesSection(
    images: List<Int>,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                AddImageCard(onClick = onAddImage)
            }

            itemsIndexed(images) { index, imageRes ->
                Box(
                    modifier = Modifier
                        .size(92.dp, 110.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { onRemoveImage(index) },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(24.dp)
                            .background(Color.Red, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddImageCard(
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    Box(
        modifier = Modifier
            .size(92.dp, 110.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.label_add_image),
                style = typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.label_maximum_10_images),
                style = typography.labelSmall,
                color = appColors.border,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddCollectionScreenPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme {
            AddCollectionScreen(
                onBackClick = {},
                onCancel = {},
                onSaveClick = {},
                onSelectLocation = {},
                onAddImage = {}
            )
        }
    }
}

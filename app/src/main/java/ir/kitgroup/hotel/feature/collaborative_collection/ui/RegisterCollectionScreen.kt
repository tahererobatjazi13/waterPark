package ir.kitgroup.hotel.feature.collaborative_collection.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CustomDescriptionField
import ir.kitgroup.hotel.core.ui.components.CustomEditTextField
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.components.SectionTitle

@Composable
fun RegisterCollectionScreen(
    navController: NavController,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    onSelectLocation: () -> Unit = {},
    onAddImage: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    var hotelDegree by remember { mutableIntStateOf(3) }
    var collaborativeName by remember { mutableStateOf("") }
    var managerName by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var receptionPhone by remember { mutableStateOf("") }
    var receptionName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var arabGuestReception by remember { mutableStateOf(true) }
    var hotelStatus by remember { mutableStateOf(true) }

    val images = remember {
        mutableStateListOf(
            R.drawable.ic_logo,
            R.drawable.ic_logo,
            R.drawable.ic_logo,
            R.drawable.ic_logo
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_add_new_collection,
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomEditTextField(
                    value = collaborativeName,
                    onValueChange = { collaborativeName = it },
                    label = stringResource(R.string.label_name_collaborative_collection),
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
                            degree = hotelDegree,
                            onDegreeChange = { hotelDegree = it }
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

                Spacer(Modifier.height(16.dp))

                GuestReceptionToggle(
                    value = arabGuestReception,
                    onValueChange = { arabGuestReception = it }
                )
                StatusToggle(
                    value = hotelStatus,
                    onValueChange = { hotelStatus = it }
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

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(stringResource(R.string.label_save), style = typography.titleMedium)
                    }

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(stringResource(R.string.label_cancel), style = typography.titleMedium)
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
                .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 5 downTo 1) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (i <= degree) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = if (i <= degree) Color(0xFFFFC107) else Color(0xFFBDBDBD)
                    )
                    Text(
                        text = i.toString(),
                        style = typography.labelSmall,
                        color = Color(0xFF8C8C8C)
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE6F2FF))
    ) {

        Button(
            onClick = onSelectLocation,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF008CFF)
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
        modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
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

            items(images) { imageRes ->
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
                        onClick = { onRemoveImage(images.indexOf(imageRes)) },
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
    Box(
        modifier = Modifier
            .size(92.dp, 110.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF9CCBFF), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = Color(0xFF008CFF)
            )
            Text(
                text = stringResource(R.string.label_add_image),
                style = typography.labelSmall,
                color = Color(0xFF008CFF),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.label_maximum_10_images),
                style = typography.labelSmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}


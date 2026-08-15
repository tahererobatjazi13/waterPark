package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.foundation.background
import androidx.compose.runtime.*

@Composable
fun DropdownSelectorField(
    value: String,
    label: String,
    placeholder: String,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = true
) {
    val appColors = LocalPartnerManagementColors.current
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        CustomSelectorField(
            value = value,
            label = label,
            placeholder = placeholder,
            isExpanded = expanded,
            isRequired = isRequired,
            onClick = {
                expanded = !expanded
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(appColors.cardBackground)
        ) {
            items.forEachIndexed { index, item ->
                val backgroundColor =
                    if (index % 2 == 0) {
                        appColors.cardBackground
                    } else {
                        appColors.cardBackgroundAlt
                    }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = backgroundColor
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                color = appColors.textPrimary
                            )
                        },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
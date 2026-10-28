package ir.kitgroup.partnerManagement.feature.organization.ui.detail.meeting


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.feature.meeting.ui.list.MeetingCard

@Composable
fun OrganizationMeetingsTabContent(
    organizationId: String,
    onAddMeetingClick: () -> Unit,
    onMeetingClick: ((String) -> Unit),
    viewModel: OrganizationMeetingsViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()

) {
    val appColors = LocalPartnerManagementColors.current

    val meetings by viewModel.meetings.collectAsStateWithLifecycle()
    val roleCode by sessionViewModel.roleCode.collectAsState()
    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    LaunchedEffect(organizationId) {
        viewModel.observeOrganizationContracts(organizationId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_visits_list))
            }
            FilledTonalButton(
                onClick = onAddMeetingClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.label_register_visit),
                    style = typography.titleLarge
                )
            }
        }


        if (meetings.isEmpty()) {
            EmptyState(
                textRes = R.string.msg_no_visit_found,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(meetings) { index, item ->
                    MeetingCard(
                        item = item,
                        isSupervisor = isSupervisor,
                        onClick = { onMeetingClick(item.meeting.meetingId) }
                    )
                }
            }
        }
    }
}

package ir.kitgroup.partnerManagement.feature.organization.ui.detail.ticket

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrganizationTicketIssueViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val organizationId: String = savedStateHandle.get<String>("organizationId") ?: ""

    private val _ticketIssueState =
        MutableStateFlow<DataState<List<TicketIssueLineDto>>>(DataState.Idle)
    val ticketIssueState: StateFlow<DataState<List<TicketIssueLineDto>>> =
        _ticketIssueState.asStateFlow()

    private var hasLoadedOnce = false

    fun ensureTicketIssuesLoaded(isOnline: Boolean) {
        if (hasLoadedOnce && _ticketIssueState.value is DataState.Success) return
        fetchOrganizationTicketIssue(isOnline)
    }

    fun retry(isOnline: Boolean) {
        fetchOrganizationTicketIssue(isOnline)
    }

    private fun fetchOrganizationTicketIssue(isOnline: Boolean) {
        if (organizationId.isBlank()) return

        viewModelScope.launch {
            if (!isOnline) {
                _ticketIssueState.value = DataState.Error(
                    UiText.StringResource(R.string.error_network_internet)
                )
                return@launch
            }

            _ticketIssueState.value = DataState.Loading

            organizationRepository.getOrganizationTicketIssue(organizationId)
                .onSuccess { ticketIssues ->
                    hasLoadedOnce = true
                    _ticketIssueState.value = DataState.Success(ticketIssues)
                }
                .onFailure { error ->
                    _ticketIssueState.value = DataState.Error(
                        UiText.DynamicString(
                            error.localizedMessage ?: context.getString(R.string.msg_server_error)
                        )
                    )
                }
        }
    }
}

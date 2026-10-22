package ir.kitgroup.partnerManagement.feature.organization.ui.detail.visitor

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrganizationVisitorViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val organizationId: String = savedStateHandle.get<String>("organizationId") ?: ""

    private val _visitorsState =
        MutableStateFlow<DataState<List<VisitorOrganizationDto>>>(DataState.Idle)
    val visitorsState: StateFlow<DataState<List<VisitorOrganizationDto>>> =
        _visitorsState.asStateFlow()

    private var hasLoadedOnce = false

    fun ensureVisitorsLoaded(isOnline: Boolean) {
        if (hasLoadedOnce && _visitorsState.value is DataState.Success) return
        fetchVisitors(isOnline)
    }

    fun retry(isOnline: Boolean) {
        fetchVisitors(isOnline)
    }

    private fun fetchVisitors(isOnline: Boolean) {
        if (organizationId.isBlank()) return

        viewModelScope.launch {
            if (!isOnline) {
                _visitorsState.value = DataState.Error(
                    UiText.StringResource(R.string.error_network_internet)
                )
                return@launch
            }

            _visitorsState.value = DataState.Loading

            organizationRepository.getOrganizationVisitors(organizationId)
                .onSuccess { visitors ->
                    hasLoadedOnce = true
                    _visitorsState.value = DataState.Success(visitors)
                }
                .onFailure { error ->
                    _visitorsState.value = DataState.Error(
                        UiText.DynamicString(
                            error.localizedMessage ?: context.getString(R.string.msg_server_error)
                        )
                    )
                }
        }
    }
}

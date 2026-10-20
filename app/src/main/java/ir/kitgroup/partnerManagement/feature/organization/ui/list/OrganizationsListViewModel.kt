package ir.kitgroup.partnerManagement.feature.organization.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOrganizationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class OrganizationsListViewModel @Inject constructor(
    organizationRepository: OrganizationRepository,
    private val checkCanAddOrganizationUseCase: CheckCanAddOrganizationUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    val organizations: StateFlow<List<OrganizationWithDetail>?> =
        organizationRepository.observeAll()
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(throwable)))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun checkCanNavigateToAddOrganization(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val error = checkCanAddOrganizationUseCase()

                if (error != null) {
                    _uiEvent.emit(UiEvent.ShowError(error))
                } else {
                    onSuccess()
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiEvent.emit(
                    UiEvent.ShowError(
                        ir.kitgroup.partnerManagement.core.ui.util.UiText
                            .DynamicString("بررسی امکان ثبت سازمان ناموفق بود")
                    )
                )
            }
        }
    }
}


package ir.kitgroup.partnerManagement.feature.organization.ui.detail.person

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.repository.OrganizationPersonRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.core.ui.util.isValidIranianMobile
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class OrganizationPersonViewModel @Inject constructor(
    private val organizationPersonRepository: OrganizationPersonRepository,
    private val organizationRepository: OrganizationRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val organizationId: String? = savedStateHandle["organizationId"]

    private val _persons = MutableStateFlow<List<OrganizationPersonEntity>>(emptyList())
    val persons: StateFlow<List<OrganizationPersonEntity>> = _persons.asStateFlow()

    private val _formState = MutableStateFlow(AddOrganizationPersonUiState())
    val formState: StateFlow<AddOrganizationPersonUiState> = _formState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun observeOrganizationPersons(orgId: String) {
        organizationPersonRepository.observeByOrganization(orgId)
            .onEach { personList ->
                _persons.value = personList
            }
            .launchIn(viewModelScope)
    }


    fun updateFullName(value: String) = _formState.update { it.copy(fullName = value) }
    fun updateMobile(value: String) {
        val filteredValue = value
            .filter { it.isDigit() }
            .take(11)

        _formState.update { state ->
            state.copy(
                mobile = filteredValue,
                mobileError = when {
                    filteredValue.isBlank() -> null

                    filteredValue.length < 11 ->
                        context.getString(R.string.error_invalid_mobile)

                    !isValidIranianMobile(filteredValue) ->
                        context.getString(R.string.error_invalid_mobile)

                    else -> null
                }
            )
        }
    }

    fun updatePhone(value: String) = _formState.update { it.copy(phone = value) }
    fun updateGender(value: Int?) = _formState.update { it.copy(gender = value) }
    fun updateRole(value: Int) = _formState.update { it.copy(role = value) }
    fun updateDescription(value: String) = _formState.update { it.copy(description = value) }
    fun updateCommissionEligible(value: Boolean) =
        _formState.update { it.copy(isCommissionEligible = value) }

    fun resetForm() {
        _formState.value = AddOrganizationPersonUiState()
    }

    fun submitPerson(
        organizationId: String,
        onSuccess: () -> Unit
    ) {
        val state = _formState.value
        if (state.isSaving) return

        if (state.fullName.isBlank()) {
            _uiEvent.tryEmit(
                UiEvent.ShowError(
                    UiText.StringResource(R.string.label_person_full_name_required)
                )
            )
            return
        }

        if (state.mobile.isBlank()) {
            _uiEvent.tryEmit(
                UiEvent.ShowError(
                    UiText.StringResource(R.string.error_invalid_mobile)
                )
            )
            return
        }

        if (!isValidIranianMobile(state.mobile)) {
            _uiEvent.tryEmit(
                UiEvent.ShowError(
                    UiText.StringResource(R.string.error_invalid_mobile)
                )
            )
            return
        }

        if (state.role == 0) {
            _uiEvent.tryEmit(
                UiEvent.ShowError(
                    UiText.StringResource(R.string.label_organization_role_required)
                )
            )
            return
        }
        viewModelScope.launch {
            _formState.update { it.copy(isSaving = true) }

            try {
                val request = AddOrganizationPersonRequest(
                    organizationId = organizationId,
                    fullName = state.fullName.trim(),
                    role = state.role,
                    mobile = state.mobile.trim().ifBlank { null },
                    phone = state.phone.trim().ifBlank { null },
                    gender = state.gender,
                    description = state.description.trim().ifBlank { null },
                    isCommissionEligible = state.isCommissionEligible
                )

                val result = organizationRepository.addOrganizationPerson(request)

                result.fold(
                    onSuccess = {
                        _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_person_created)))
                        resetForm()
                        delay(300)
                        onSuccess()
                    },
                    onFailure = { error ->
                        if (error.isNetworkError()) {
                            val localUUID = UUID.randomUUID().toString()
                            val offlineEntity = OrganizationPersonEntity(
                                orgPersonId = localUUID,
                                organizationId = organizationId,
                                fullName = state.fullName.trim(),
                                role = state.role,
                                mobile = state.mobile.trim(),
                                phone = state.phone.trim().ifBlank { null },
                                gender = state.gender,
                                description = state.description.trim().ifBlank { null },
                                isCommissionEligible = state.isCommissionEligible,
                                syncStatus = SyncStatus.PENDING_INSERT,
                                isLocallyModified = true
                            )
                            organizationPersonRepository.saveLocal(offlineEntity)
                            _uiEvent.emit(
                                UiEvent.ShowMessage(
                                    UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)
                                )
                            )
                            resetForm()
                            delay(300)
                            onSuccess()
                        } else {
                            // نمایش خطای مستقیم سرور
                            _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                        }
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _formState.update { it.copy(isSaving = false) }
            }
        }
    }
}




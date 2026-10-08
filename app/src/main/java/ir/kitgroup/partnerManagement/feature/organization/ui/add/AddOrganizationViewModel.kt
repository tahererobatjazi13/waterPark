package ir.kitgroup.partnerManagement.feature.organization.ui.add

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.CreateOrganizationRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrganizationViewModel @Inject constructor(
    private val application: Application,
    private val repository: OrganizationRepository
) : ViewModel() {

    private val _cities = MutableStateFlow<List<CityEntity>>(emptyList())
    val cities: StateFlow<List<CityEntity>> = _cities.asStateFlow()

    private val _regions = MutableStateFlow<List<RegionEntity>>(emptyList())
    val regions: StateFlow<List<RegionEntity>> = _regions.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAllCity().collect { result ->
                _cities.value = result
            }
        }
    }

    private var regionsJob: Job? = null

    fun loadRegions(cityId: String?) {

        regionsJob?.cancel()

        _regions.value = emptyList()

        if (cityId.isNullOrBlank()) {
            return
        }

        regionsJob = viewModelScope.launch {

            repository
                .observeRegionByCityId(cityId)
                .collect { result ->
                    _regions.value = result
                }
        }
    }

    private val _createState =
        MutableStateFlow<DataState<ModelResponse>>(DataState.Idle)
    val createState = _createState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun resetState() {
        _createState.value = DataState.Idle
    }
    val formState = AddOrganizationFormState()

    fun saveOrganization(
        state: AddOrganizationFormState,
        organizationId: String?
    ) {
        if (state.organizationName.isBlank()) {
            viewModelScope.launch {
                _toastEvent.emit(
                    application.getString(R.string.label_organization_name_required)
                )
            }
            return
        }
        if (state.organizationType == 0) {
            viewModelScope.launch {
                _toastEvent.emit(
                    application.getString(R.string.label_organization_type_required)
                )
            }
            return
        }
        if (state.cityId.isBlank()) {
            viewModelScope.launch {
                _toastEvent.emit(
                    application.getString(R.string.label_city_name_required)
                )
            }
            return
        }
        if (state.regionId.isBlank()) {
            viewModelScope.launch {
                _toastEvent.emit(
                    application.getString(R.string.label_region_name_required)
                )
            }
            return
        }

        viewModelScope.launch {
            _createState.value = DataState.Loading
            try {
                val request = CreateOrganizationRequest(
                    organizationId = organizationId,
                    name = state.organizationName.trim(),
                    ownerName = state.ownerName.trim().takeIf { it.isNotEmpty() },
                    nationalId = state.nationalId.trim().takeIf { it.isNotEmpty() },
                    organizationType = state.organizationType,
                    grade = state.organizationGrade,
                    statusGetStand = state.statusGetStand,
                    level = state.organizationLevel,
                    status = state.organizationStatus.id,
                    phone = state.phone.trim().takeIf { it.isNotEmpty() },
                    landLine = state.landLine.trim().takeIf { it.isNotEmpty() },
                    mobile = state.mobile.trim().takeIf { it.isNotEmpty() },
                    address = state.address.trim().takeIf { it.isNotEmpty() },
                    cityId = state.cityId,
                    regionId = state.regionId,
                    latitude = state.latitude,
                    longitude = state.longitude,
                    statusExternalCustomer = state.statusExtenalCutomer,
                    englishName = state.englishName.trim().takeIf { it.isNotEmpty() },
                    document = state.description,
                    customerCapacity = state.customerCapacity.toIntOrNull() ?: 0,
                    email = state.email.trim().takeIf { it.isNotEmpty() },
                    ticketSaleCountHistory = state.ticketSaleCountHistory.toIntOrNull() ?: 0
                )

                println("========== SAVE ORGANIZATION ==========")
                println("organizationId = [$organizationId]")
                println("isEdit = ${!organizationId.isNullOrBlank()}")
                println("request = $request")

                val result = if (organizationId.isNullOrBlank()) {
                    println(">>> CREATE ORGANIZATION")

                    repository.createOrganization(request)
                } else {
                    println(">>> UPDATE ORGANIZATION")

                    repository.updateOrganization(request)
                }


                result.onSuccess { response ->
                    _createState.value = DataState.Success(response)
                }.onFailure { error ->
                    _createState.value = DataState.Error(
                        error.localizedMessage?.let { UiText.DynamicString(it) }
                            ?: UiText.StringResource(R.string.error_unknown)
                    )
                }
            } catch (e: Exception) {
                _createState.value =
                    DataState.Error(
                        e.localizedMessage
                            ?.let { UiText.DynamicString(it) }
                            ?: UiText.StringResource(
                                R.string.error_unknown
                            )
                    )
            }
        }
    }

    fun observeOrganization(
        organizationId: String
    ): Flow<OrganizationWithDetail?> {
        return repository.observeById(organizationId)
    }

}

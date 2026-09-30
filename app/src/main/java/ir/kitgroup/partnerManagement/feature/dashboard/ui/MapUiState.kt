package ir.kitgroup.partnerManagement.feature.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MapUiState(
    val organizations: List<OrganizationEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val organizationDao: OrganizationDao
) : ViewModel() {

    val uiState: StateFlow<MapUiState> = organizationDao.observeAll()
        .map { orgDetailsList ->
            // استخراج موجودیت‌های سازمان و فیلتر کردن رکوردهایی که دارای مختصات معتبر هستند
            val validOrganizations = orgDetailsList
                .map { it.organization }
                .filter { org ->
                    val lat = org.latitude?.toDoubleOrNull()
                    val lon = org.longitude?.toDoubleOrNull()
                    lat != null && lon != null
                }

            MapUiState(
                organizations = validOrganizations,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MapUiState()
        )
}

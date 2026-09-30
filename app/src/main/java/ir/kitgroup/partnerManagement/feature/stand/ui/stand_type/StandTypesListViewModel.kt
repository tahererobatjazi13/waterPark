package ir.kitgroup.partnerManagement.feature.stand.ui.stand_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.repository.StandTypesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StandTypesListViewModel @Inject constructor(
    standTypesRepository: StandTypesRepository
) : ViewModel() {

    val stands: StateFlow<List<StandTypeEntity>> = standTypesRepository
        .observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteStand(stand: StandTypeEntity) {
        viewModelScope.launch {

            //standTypeDao.deleteById(stand.standTypeId)
        }
    }
}

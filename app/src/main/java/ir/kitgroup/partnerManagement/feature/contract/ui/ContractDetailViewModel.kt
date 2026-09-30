package ir.kitgroup.partnerManagement.feature.contract.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ContractDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    contractDao: ContractDao
) : ViewModel() {

    private val contractId: String? = savedStateHandle["contractId"]

    val contract: StateFlow<ContractEntity?> = (
        contractId?.let { contractDao.observeById(it) } ?: emptyFlow()
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )
}

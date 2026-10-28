package ir.kitgroup.partnerManagement.feature.dashboard.ui

data class DashboardUiState(
    val isLoading: Boolean = false,
    val pendingUploadCount: Int = 0,
    val isSyncing: Boolean = false,
    val errorMessage: String? = null
)

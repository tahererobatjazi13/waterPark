package ir.kitgroup.partnerManagement.core

import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddContractUseCase
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOrganizationUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationGuard @Inject constructor(
    private val checkCanAddOrganizationUseCase: CheckCanAddOrganizationUseCase,
    private val checkCanAddContractUseCase: CheckCanAddContractUseCase
) {

    suspend fun checkCanAddOrganization(): String? {
        return checkCanAddOrganizationUseCase().toString()
    }

    suspend fun checkCanAddContract(): String? {
        return checkCanAddContractUseCase().toString()
    }
}
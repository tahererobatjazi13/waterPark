package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractDao {
    @Upsert
    suspend fun upsertAll(items: List<ContractEntity>)

    @Query("""
    SELECT * FROM contracts
    ORDER BY startDate DESC
""")
    fun observeAll(): Flow<List<ContractEntity>>


    @Query("""
        SELECT * FROM contracts
        WHERE organizationId = :organizationId
        ORDER BY startDate DESC
    """)
    fun observeByOrganization(
        organizationId: String
    ): Flow<List<ContractEntity>>

    @Query("SELECT * FROM contracts WHERE contractId = :contractId LIMIT 1")
    fun observeById(contractId: String): Flow<ContractEntity?>

    @Query("DELETE FROM contracts")
    suspend fun deleteAll()
}
package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.kitgroup.partnerManagement.core.database.entity.ContractTerminationEntity

@Dao
interface ContractTerminationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ContractTerminationEntity)

    @Query("SELECT * FROM contract_terminations")
    suspend fun getAll(): List<ContractTerminationEntity>

    @Query("DELETE FROM contract_terminations WHERE contractId = :contractId")
    suspend fun deleteById(contractId: String)

    @Query("DELETE FROM  contract_terminations")
    suspend fun deleteAll()
}

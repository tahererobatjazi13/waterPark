package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail

@Dao
interface AssignedStandDao {
    @Upsert
    suspend fun upsertAll(items: List<AssignedStandEntity>)

    @Transaction
    @Query("SELECT * FROM assigned_stands WHERE organizationId = :organizationId")
    fun observeByOrganizationWithDetail(organizationId: String): Flow<List<AssignedStandWithDetail>>

    @Transaction
    @Query("SELECT * FROM assigned_stands WHERE assignmentType = 2 ")
    fun getOrganizationAssignedStandsWithDetail(): Flow<List<AssignedStandWithDetail>>

    @Transaction
    @Query("SELECT * FROM assigned_stands WHERE assignmentType = 1 ")
    fun getVisitorAssignedStandsWithDetail(): Flow<List<AssignedStandWithDetail>>

    @Transaction
    @Query("SELECT * FROM assigned_stands WHERE assignedStandId = :assignedStandId LIMIT 1")
    fun observeByIdWithDetail(assignedStandId: String): Flow<AssignedStandWithDetail>

    @Query("SELECT * FROM assigned_stands ")
    fun getAllAssignedStands(): Flow<List<AssignedStandEntity>>

    @Query("DELETE FROM assigned_stands")
    suspend fun deleteAll()
}
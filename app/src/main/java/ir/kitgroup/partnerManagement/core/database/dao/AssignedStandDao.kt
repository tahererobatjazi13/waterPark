package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction
import androidx.room.Update
import ir.kitgroup.partnerManagement.core.database.entity.AssignStandChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus

@Dao
interface AssignedStandDao {
    @Upsert
    suspend fun upsertAll(items: List<AssignedStandEntity>)
    @Upsert
    suspend fun upsert(item: AssignedStandEntity)

    @Transaction
    @Query("SELECT * FROM assigned_stands WHERE organizationId = :organizationId")
    fun observeByOrganizationWithDetail(organizationId: String): Flow<List<AssignedStandWithDetail>>

    @Transaction
    @Query("SELECT * FROM assigned_stands  WHERE assignmentType = 2  ORDER BY modifiedOn DESC")
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


    @Update
    suspend fun update(item: AssignedStandEntity)

    @Query(
        """
    UPDATE assigned_stands
    SET statusAssign = :status
    WHERE assignedStandId = :assignedStandId
"""
    )
    suspend fun updateStatus(
        assignedStandId: String,
        status: Int
    )

    @Query(
        """
    SELECT * FROM assigned_stands
    WHERE assignedStandId = :assignedStandId
    LIMIT 1
"""
    )
    suspend fun getById(
        assignedStandId: String
    ): AssignedStandEntity?

    @Query("DELETE FROM assigned_stands WHERE assignedStandId = :assignedStandId")
    suspend fun deleteById(assignedStandId: String)

    /**
     * دریافت لیست تمام قراردادهای ایجاد شده آفلاین جهت قرار دادن در pendingContracts
     */
    @Query(
        """
        SELECT * FROM assigned_stands 
        WHERE syncStatus = :syncStatus
        ORDER BY lastModifiedTimestamp ASC
    """
    )
    suspend fun getAssignedStandBySyncStatus(syncStatus: SyncStatus): List<AssignedStandEntity>

    @Query("UPDATE assigned_stands SET syncStatus = :status, isLocallyModified = :isLocallyModified WHERE assignedStandId = :assignedStandId")
    suspend fun updateSyncStatus(
        assignedStandId: String,
        status: SyncStatus,
        isLocallyModified: Boolean
    )


    @Query("SELECT COUNT(*) FROM assigned_stands WHERE syncStatus IN ('PENDING_INSERT', 'PENDING_UPDATE')")
    fun observePendingCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AssignStandChangeStatusEntity)

}
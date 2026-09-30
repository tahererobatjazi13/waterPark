package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitSubjectDao {
    @Upsert
    suspend fun upsertAll(items: List<VisitSubjectEntity>)

    @Query("SELECT * FROM visit_subjects ORDER BY title")
    fun observeAll(): Flow<List<VisitSubjectEntity>>

    @Query("DELETE FROM visit_subjects")
    suspend fun deleteAll()
}
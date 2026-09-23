package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.*
import ir.kitgroup.partnerManagement.core.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BaseDataDao {

    // --- Cities & Regions ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegions(regions: List<RegionEntity>)

    @Query("SELECT * FROM region WHERE cityId = :cityId")
    fun getRegionsByCity(cityId: Long): Flow<List<RegionEntity>>

    // --- Topics & Warnings ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitTopics(topics: List<SubjectVisitEntity>)

    @Query("SELECT * FROM subject_visit")
    fun getVisitTopics(): Flow<List<SubjectVisitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarningTypes(warnings: List<WarningsEntity>)

    @Query("SELECT * FROM warnings")
    fun getWarningsTypes(): Flow<List<WarningsEntity>>

    // --- Organizations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizations(organizations: List<OrganizationEntity>)

    @Query("SELECT * FROM organization")
    fun getAllOrganizations(): Flow<List<OrganizationEntity>>

    @Query("SELECT * FROM organization WHERE organizationId = :organizationId LIMIT 1")
    suspend fun getOrganizationById(organizationId: Long): OrganizationEntity?
}

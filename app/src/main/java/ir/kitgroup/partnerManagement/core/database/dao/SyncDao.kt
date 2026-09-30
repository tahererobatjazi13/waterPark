package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ir.kitgroup.partnerManagement.core.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncDao {

    // --- Cities & Regions ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegions(regions: List<RegionEntity>)

    @Query("SELECT * FROM regions WHERE cityId = :cityId")
    fun getRegionsByCity(cityId: Long): Flow<List<RegionEntity>>

    // --- VisitSubjects ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitSubjects(topics: List<VisitSubjectEntity>)

    @Query("SELECT * FROM visit_subjects")
    fun getVisitSubjects(): Flow<List<VisitSubjectEntity>>


    // --- StandTypes ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandTypes(items: List<StandTypeEntity>)

    @Query("SELECT * FROM stand_Types")
    fun getStandTypes(): Flow<List<StandTypeEntity>>


    // --- WarningTypes ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarningTypes(warnings: List<WarningTypeEntity>)

    @Query("SELECT * FROM warning_types")
    fun getWarningsTypes(): Flow<List<WarningTypeEntity>>


    // --- Products ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(items: List<ProductEntity>)

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<ProductEntity>>


    // --- Offers & Offer Details ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(items: List<OfferEntity>)

    @Query("SELECT * FROM offers")
    fun getOffers(): Flow<List<OfferEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfferDetails(items: List<OfferDetailEntity>)

    @Query("SELECT * FROM offer_details")
    fun getOfferDetails(): Flow<List<OfferDetailEntity>>


    // --- Organizations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizations(organizations: List<OrganizationEntity>)

    @Query("SELECT * FROM organizations")
    fun getAllOrganizations(): Flow<List<OrganizationEntity>>

    @Query("SELECT * FROM organizations WHERE organizationId = :organizationId LIMIT 1")
    suspend fun getOrganizationById(organizationId: Long): OrganizationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizationPersons(items: List<OrganizationPersonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeetings(items: List<MeetingEntity>)

    @Query("SELECT * FROM meetings")
    fun getAllMeetings(): Flow<List<MeetingEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarnings(items: List<WarningEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignedStands(items: List<AssignedStandEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContracts(items: List<ContractEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignedSerials(items: List<AssignedSerialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistics(items: List<StatisticEntity>)


    @Transaction
    suspend fun saveAllSyncData(
        cities: List<CityEntity>,
        regions: List<RegionEntity>,
        visitSubjects: List<VisitSubjectEntity>,
        standTypes: List<StandTypeEntity>,
        warningTypes: List<WarningTypeEntity>,
        products: List<ProductEntity>,
        offers: List<OfferEntity>,
        offerDetails: List<OfferDetailEntity>,
        organizations: List<OrganizationEntity>,
        organizationPersons: List<OrganizationPersonEntity>,
        meetings: List<MeetingEntity>,
        warnings: List<WarningEntity>,
        assignedStands: List<AssignedStandEntity>,
        contracts: List<ContractEntity>,
        assignedSerials: List<AssignedSerialEntity>,
        statistics: List<StatisticEntity>
    ) {
        if (cities.isNotEmpty()) insertCities(cities)
        if (regions.isNotEmpty()) insertRegions(regions)
        if (visitSubjects.isNotEmpty()) insertVisitSubjects(visitSubjects)
        if (standTypes.isNotEmpty()) insertStandTypes(standTypes)
        if (warningTypes.isNotEmpty()) insertWarningTypes(warningTypes)
        if (products.isNotEmpty()) insertProducts(products)
        if (offers.isNotEmpty()) insertOffers(offers)
        if (offerDetails.isNotEmpty()) insertOfferDetails(offerDetails)
        if (organizations.isNotEmpty()) insertOrganizations(organizations)
        if (organizationPersons.isNotEmpty()) insertOrganizationPersons(organizationPersons)
        if (meetings.isNotEmpty()) insertMeetings(meetings)
        if (warnings.isNotEmpty()) insertWarnings(warnings)
        if (assignedStands.isNotEmpty()) insertAssignedStands(assignedStands)
        if (contracts.isNotEmpty()) insertContracts(contracts)
        if (assignedSerials.isNotEmpty()) insertAssignedSerials(assignedSerials)
        if (statistics.isNotEmpty()) insertStatistics(statistics)
    }
}

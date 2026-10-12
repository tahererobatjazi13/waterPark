package ir.kitgroup.partnerManagement.core.network.apiService

import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.feature.contract.model.CreateContractRequest
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import ir.kitgroup.partnerManagement.feature.dashboard.model.ConfirmLastSyncResponse
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataRequest
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataResponse
import ir.kitgroup.partnerManagement.feature.login.model.LoginRequest
import ir.kitgroup.partnerManagement.feature.login.model.LoginResponse
import ir.kitgroup.partnerManagement.feature.meeting.model.ChangeStatusMeetingRequest
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import ir.kitgroup.partnerManagement.feature.offer.model.CreateOfferDetailPlanRequest
import ir.kitgroup.partnerManagement.feature.offer.model.CreateOfferPlanRequest
import ir.kitgroup.partnerManagement.feature.offer.model.CreateSerialOfferRequest
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeOrganizationStatusRequest
import ir.kitgroup.partnerManagement.feature.organization.model.CreateOrganizationWarningRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.CreateOrganizationRequest
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import ir.kitgroup.partnerManagement.feature.stand.model.CreateAssignStandRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/Auth/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/Sync/syncData")
    suspend fun syncData(
        @Body request: SyncDataRequest
    ): SyncDataResponse

    @POST("api/Sync/confirmLastSync")
    suspend fun confirmLastSync(
        @Query("userId") userId: String
    ): ConfirmLastSyncResponse


    @POST("api/Organizations/create")
    suspend fun createOrganization(
        @Body request: CreateOrganizationRequest
    ): Response<ModelResponse>

    @PATCH("api/Organizations/update")
    suspend fun updateOrganization(
        @Body request: CreateOrganizationRequest
    ): Response<ModelResponse>

    @POST("api/Organizations/persons")
    suspend fun addOrganizationPerson(
        @Body request: AddOrganizationPersonRequest
    ): Response<ModelResponse>

    @POST("api/Organizations/Createcontracts")
    suspend fun createContract(
        @Body request: CreateContractRequest
    ): Response<ModelResponse>

    @PUT("api/Organizations/Updatecontracts")
    suspend fun updateContract(
        @Body request: CreateContractRequest
    ): Response<ModelResponse>

    @POST("api/Organizations/TerminateActiveContract")
    suspend fun terminateActiveContract(
        @Body request: TerminateActiveContractRequest
    ): Response<ModelResponse>

    @PATCH("api/Organizations/change-status")
    suspend fun changeOrganizationStatus(
        @Body request: ChangeOrganizationStatusRequest
    ): Response<ModelResponse>


    @GET("api/Sync/GetVisitorOrg/{organizationId}")
    suspend fun getOrganizationVisitors(
        @Path("organizationId") organizationId: String
    ): Response<List<VisitorOrganizationDto>>


    @POST("api/Inspection/Meeting/create")
    suspend fun createMeeting(
        @Body request: CreateMeetingRequest
    ): Response<ModelResponse>


    @PUT("api/Inspection/Meeting/update")
    suspend fun updateMeeting(
        @Body request: CreateMeetingRequest
    ): Response<ModelResponse>

    @POST("api/Inspection/Meeting/change-status")
    suspend fun changeMeetingStatus(
        @Body request: ChangeStatusMeetingRequest
    ): Response<ModelResponse>

    @POST("api/Inspection/AssignWarnings/create")
    suspend fun createOrganizationWarning(
        @Body request: CreateOrganizationWarningRequest
    ): Response<ModelResponse>


    @PUT("api/Inspection/AssignWarnings/update")
    suspend fun updateOrganizationWarning(
        @Body request: CreateOrganizationWarningRequest
    ): Response<ModelResponse>


    @POST("api/Inspection/AssignStand/create")
    suspend fun createAssignStand(
        @Body request: CreateAssignStandRequest
    ): Response<ModelResponse>


    @PUT("api/Inspection/AssignStand/update")
    suspend fun updateAssignStand(
        @Body request: CreateAssignStandRequest
    ): Response<ModelResponse>


    @PATCH("api/Inspection/AssignStand/change-status")
    suspend fun changeAssignStandStatus(
        @Body request: ChangeAssignStandStatusRequest
    ): Response<ModelResponse>

    @POST("api/promotion-ticket/offer-plan/Create")
    suspend fun createOfferPlan(
        @Body request: CreateOfferPlanRequest
    ): Response<ModelResponse>


    @PUT("api/promotion-ticket/offer-plan/Update")
    suspend fun updateOfferPlan(
        @Body request: CreateOfferPlanRequest
    ): Response<ModelResponse>

    @POST("api/promotion-ticket/offer-detailplan/Create")
    suspend fun createOfferDetailPlan(
        @Body request: CreateOfferDetailPlanRequest
    ): Response<ModelResponse>

    @PUT("api/promotion-ticket/offer-detailplan/Update")
    suspend fun updateOfferDetailPlan(
        @Body request: CreateOfferDetailPlanRequest
    ): Response<ModelResponse>

    @DELETE("api/promotion-ticket/offer-detailplan/Delete{id}")
    suspend fun deleteOfferDetailPlan(
        @Path("id") offerTicketPlanLineId: String
    ): Response<ModelResponse>

    @POST("api/promotion-ticket/AssignSerialOffer")
    suspend fun assignSerialOffer(
        @Body request: CreateSerialOfferRequest
    ): Response<ModelResponse>


    @PUT("api/promotion-ticket/UpdateSerialOffer")
    suspend fun updateSerialOffer(
        @Body request: CreateSerialOfferRequest
    ): Response<ModelResponse>


    @PATCH("api/promotion-ticket/change-status/{id}/status")
    suspend fun serialOfferChangeStatus(
        @Path("id") id: String,
        @Query("status") status: String
    ): Response<ModelResponse>


}
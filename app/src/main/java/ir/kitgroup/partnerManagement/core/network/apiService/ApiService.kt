package ir.kitgroup.partnerManagement.core.network.apiService

import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.feature.contract.model.ContractRequest
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import ir.kitgroup.partnerManagement.feature.dashboard.model.ConfirmLastSyncResponse
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncRequestDto
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataResponse
import ir.kitgroup.partnerManagement.feature.login.model.LoginRequest
import ir.kitgroup.partnerManagement.feature.login.model.LoginResponse
import ir.kitgroup.partnerManagement.feature.meeting.model.ChangeStatusMeetingRequest
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import ir.kitgroup.partnerManagement.feature.offer.model.OfferDetailPlanRequest
import ir.kitgroup.partnerManagement.feature.offer.model.OfferPlanRequest
import ir.kitgroup.partnerManagement.feature.offer.model.SerialOfferRequest
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeStatusOrganizationRequest
import ir.kitgroup.partnerManagement.feature.organization.model.CreateOrganizationWarningRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.CreateOrganizationRequest
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import ir.kitgroup.partnerManagement.feature.stand.model.CreateAssignStandRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface ApiService {

    // Auth
    @POST("api/Auth/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // Sync
    @POST("api/Sync/syncData")
    suspend fun syncData(
        @Body request: SyncRequestDto
    ): SyncDataResponse

    @POST("api/Sync/confirmLastSync")
    suspend fun confirmLastSync(
        @Query("userId") userId: String
    ): ConfirmLastSyncResponse

    // Organizations
    @POST("api/Organizations/create")
    suspend fun createOrganization(
        @Body request: CreateOrganizationRequest
    ): Response<ModelResponse>

    @PATCH("api/Organizations/update")
    suspend fun updateOrganization(
        @Body request: CreateOrganizationRequest
    ): Response<ModelResponse>

    @PATCH("api/Organizations/change-status")
    suspend fun changeStatusOrganization(
        @Body request: ChangeStatusOrganizationRequest
    ): Response<ModelResponse>

    @POST("api/Organizations/persons")
    suspend fun addOrganizationPerson(
        @Body request: AddOrganizationPersonRequest
    ): Response<ModelResponse>

    @POST("api/Organizations/Createcontracts")
    suspend fun createContract(
        @Body request: ContractRequest
    ): Response<ModelResponse>

    @PUT("api/Organizations/Updatecontracts")
    suspend fun updateContract(
        @Body request: ContractRequest
    ): Response<ModelResponse>

    @POST("api/Organizations/TerminateActiveContract")
    suspend fun terminateActiveContract(
        @Body request: TerminateActiveContractRequest
    ): Response<ModelResponse>

    @GET("api/Sync/GetVisitorOrg/{organizationId}")
    suspend fun getOrganizationVisitors(
        @Path("organizationId") organizationId: String
    ): Response<List<VisitorOrganizationDto>>

    @GET("api/TicketIssue/organizations/{organizationId}/lines")
    suspend fun getOrganizationTicketIssue(
        @Path("organizationId") organizationId: String
    ): Response<List<TicketIssueLineDto>>


    //Inspection
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

    @POST("api/Inspection/AssignWarnings/create")
    suspend fun createOrganizationWarning(
        @Body request: CreateOrganizationWarningRequest
    ): Response<ModelResponse>

    @PUT("api/Inspection/AssignWarnings/update")
    suspend fun updateOrganizationWarning(
        @Body request: CreateOrganizationWarningRequest
    ): Response<ModelResponse>

    //PromotionTicket
    @POST("api/promotion-ticket/offer-plan/Create")
    suspend fun createOfferPlan(
        @Body request: OfferPlanRequest
    ): Response<ModelResponse>

    @PUT("api/promotion-ticket/offer-plan/Update")
    suspend fun updateOfferPlan(
        @Body request: OfferPlanRequest
    ): Response<ModelResponse>

    @POST("api/promotion-ticket/offer-detailplan/Create")
    suspend fun createOfferDetailPlan(
        @Body request: OfferDetailPlanRequest
    ): Response<ModelResponse>

    @PUT("api/promotion-ticket/offer-detailplan/Update")
    suspend fun updateOfferDetailPlan(
        @Body request: OfferDetailPlanRequest
    ): Response<ModelResponse>

    @DELETE("api/promotion-ticket/offer-detailplan/Delete{id}")
    suspend fun deleteOfferDetailPlan(
        @Path("id") offerTicketPlanLineId: String
    ): Response<ModelResponse>

    @POST("api/promotion-ticket/AssignSerialOffer")
    suspend fun assignSerialOffer(
        @Body request: SerialOfferRequest
    ): Response<ModelResponse>

    @PUT("api/promotion-ticket/UpdateSerialOffer")
    suspend fun updateSerialOffer(
        @Body request: SerialOfferRequest
    ): Response<ModelResponse>

    @PATCH("api/promotion-ticket/change-status/{id}/status")
    suspend fun serialOfferChangeStatus(
        @Path("id") id: String,
        @Query("status") status: String
    ): Response<ModelResponse>


    @Multipart
    @POST("api/OrgAttachmentFiles/upload")
    suspend fun uploadAttachment(
        @Part file: MultipartBody.Part,
        @Part("Name") name: RequestBody?,
        @Part("OrganizationId") organizationId: RequestBody?,
        @Part("MeetingId") meetingId: RequestBody?,
        @Part("FileType") fileType: RequestBody?,
        @Part("Description") description: RequestBody?
    ): Response<AttachmentUploadResponse>

    @Streaming
    @GET("api/OrgAttachmentFiles/download/{id}")
    suspend fun downloadAttachment(
        @Path("id") fileId: String
    ): Response<ResponseBody>

    @GET("api/OrgAttachmentFiles/getAttachments/{id}")
    suspend fun getOrganizationAttachments(
        @Path("id") organizationId: String
    ): Response<List<AttachmentFileDto>>


}
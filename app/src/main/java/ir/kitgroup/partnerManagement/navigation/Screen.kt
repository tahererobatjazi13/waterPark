package ir.kitgroup.partnerManagement.navigation


sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Map : Screen("map")
    data object Settings : Screen("settings")
    data object ThemeMode : Screen("themeMode")
    data object CenterInfo : Screen("center_info")


    // Organization
    data object AddOrganization : Screen("add_organization")

    data object EditOrganization : Screen("add_organization/{organizationId}") {
        fun createRoute(organizationId: String) = "add_organization/$organizationId"
    }

    data object OrganizationDetail : Screen("organization_detail/{organizationId}") {
        fun createRoute(organizationId: String) = "organization_detail/$organizationId"
    }

    data object AssignVisitor :
        Screen("assign_visitor/{organizationId}")

    // Meetings
    data object MeetingDetail : Screen("meeting_detail/{meetingId}") {
        fun createRoute(meetingId: String): String {
            return "meeting_detail/$meetingId"
        }
    }

    data object AddMeeting :
        Screen("add_meeting?meetingId={meetingId}&organizationId={organizationId}") {
        fun createRoute(
            meetingId: String? = null,
            organizationId: String? = null
        ): String {
            val params = mutableListOf<String>()
            if (meetingId != null) {
                params.add("meetingId=$meetingId")
            }
            if (organizationId != null) {
                params.add("organizationId=$organizationId")
            }
            return if (params.isEmpty()) "add_meeting" else "add_meeting?${
                params.joinToString(
                    "&"
                )
            }"
        }
    }


    //  Stand Types
    data object AdvertisingStandMenu : Screen("advertising_stand_menu")

    data object StandTypesList : Screen("stand_types_list")

    data object AddStandTypes : Screen(
        route = "add_stand_types/{standTypeId}"
    ) {
        const val ITEM_ID_ARGUMENT = "standTypeId"
        const val NEW_ITEM_ID = "new"

        fun createRoute(
            standTypeId: String = NEW_ITEM_ID
        ): String {
            return "add_stand_types/$standTypeId"
        }
    }

    data object AssignedStandsVisitorList :
        Screen("assigned_stands_visitor_list")

    data object AddAssignedStandsVisitorVisitor :
        Screen("add_assigned_stands_visitor")

    data object AssignedStandsOrganizationList :
        Screen("assigned_stands_organization_list")

    data object AddAssignedStandsOrganization:
        Screen("add_assigned_stands_organization?organizationId={organizationId}") {
        fun createRoute(organizationId: String? = null): String {
            return if (organizationId != null) {
                "add_assigned_stands_organization?organizationId=$organizationId"
            } else {
                "add_assigned_stands_organization"
            }
        }
    }

    data object AssignedStandsDetail : Screen(
        route = "assigned_stands_detail/{assignedStandId}"
    ) {
        fun createRoute(assignedStandId: String): String {
            return "assigned_stands_detail/$assignedStandId"
        }
    }

    //Report
    data object ReportMenu : Screen("report_menu")
    data object VisitorList : Screen("visitor_list")
    data object VisitorsPerformance : Screen("visitors_performance_dashboard")
    data object ReportContract : Screen("report_contract")
    data object OrganizationPerformance : Screen("organization_performance")


    // Contracts
    data object ContractsList : Screen("contract_list")

    data object ContractDetail : Screen("contract_detail/{contractId}") {
        fun createRoute(contractId: String) = "contract_detail/$contractId"
    }

    data object AddContract : Screen("add_contract?organizationId={organizationId}") {
        fun createRoute(organizationId: String? = null): String {
            return if (organizationId != null) {
                "add_contract?organizationId=$organizationId"
            } else {
                "add_contract"
            }
        }
    }

    data object AddContractOffer : Screen("add_contract_Offer")

    // add Offer
    data object AddOffer : Screen("add_Offer")

    // offer detail list
    data object OfferDetailList : Screen("offer_detail_list/{offerId}") {
        fun createRoute(offerId: String) = "offer_detail_list/$offerId"
    }

    // add Offer detail
    data object AddOfferDetail : Screen("add_offer_detail/{offerTicketPlanLineName}") {
        fun createRoute(planTitle: String): String {
            val encodedTitle = java.net.URLEncoder.encode(planTitle.ifBlank { " " }, "UTF-8")
            return "add_offer_detail/$encodedTitle"
        }
    }


    // offer detail
    data object OfferDetail : Screen("offer_detail/{offerDetailId}") {
        fun createRoute(offerDetailId: String) = "offer_detail/$offerDetailId"
    }
}

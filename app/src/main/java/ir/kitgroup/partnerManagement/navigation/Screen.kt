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

    data object EditMeeting :
        Screen("edit_meeting/{meetingId}") {

        fun createRoute(
            meetingId: String
        ): String {
            return "edit_meeting/$meetingId"
        }
    }

    data object AddMeeting :
        Screen("add_meeting?meetingId={meetingId}&organizationId={organizationId}") {

        fun createRoute(
            meetingId: String? = null,
            organizationId: String? = null
        ): String {
            val params = mutableListOf<String>()
            if (!meetingId.isNullOrBlank()) {
                params.add("meetingId=$meetingId")
            }
            if (!organizationId.isNullOrBlank()) {
                params.add("organizationId=$organizationId")
            }
            return if (params.isEmpty()) {
                "add_meeting"
            } else {
                "add_meeting?${params.joinToString("&")}"
            }
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


    object AddAssignedStandsOrganization {
        const val route =
            "add_assigned_stands_org?organizationId={organizationId}&assignedStandId={assignedStandId}"

        fun createRoute(organizationId: String? = null, assignedStandId: String? = null): String {
            val orgParam = organizationId?.let { "organizationId=$it" } ?: ""
            val standParam = assignedStandId?.let { "assignedStandId=$it" } ?: ""
            val query = listOf(orgParam, standParam).filter { it.isNotEmpty() }.joinToString("&")
            return if (query.isNotEmpty()) "add_assigned_stands_org?$query" else "add_assigned_stands_org"
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

    data object AddContract :
        Screen("addContract") {

        const val ORGANIZATION_ID_ARGUMENT =
            "organizationId"

        const val CONTRACT_ID_ARGUMENT =
            "contractId"

        fun createRoute(
            organizationId: String? = null
        ): String {

            return if (
                organizationId.isNullOrBlank()
            ) {
                route
            } else {

                "$route?" +
                        "$ORGANIZATION_ID_ARGUMENT=$organizationId"
            }
        }
    }

    data object EditContract :
        Screen("edit_contract/{contractId}") {

        fun createRoute(
            contractId: String
        ): String {
            return "edit_contract/$contractId"
        }
    }

    data object AddContractOffer : Screen("add_contract_offer?organizationId={organizationId}") {
        fun createRoute(organizationId: String? = null): String {
            return if (!organizationId.isNullOrBlank()) {
                "add_contract_offer?organizationId=$organizationId"
            } else {
                "add_contract_offer"
            }
        }
    }

    data object AssignedSerialDetail : Screen("assigned_serial_detail/{serialId}") {
        fun createRoute(serialId: String) = "assigned_serial_detail/$serialId"
    }

    // add Offer
    data object AddOffer : Screen("add_Offer")

    object EditOffer : Screen("edit_offer/{offerId}") {
        fun createRoute(offerId: String): String =
            "edit_offer/$offerId"
    }

    // offer detail list
    data object OfferDetailList : Screen("offer_detail_list/{offerId}") {
        fun createRoute(offerId: String) = "offer_detail_list/$offerId"
    }

    // add Offer detail
    object AddOfferDetail : Screen("add_offer_detail/{offerTicketPlanId}/{offerTicketPlanName}") {
        fun createRoute(offerTicketPlanId: String, offerTicketPlanName: String): String {
            val encodedName = runCatching {
                java.net.URLEncoder.encode(offerTicketPlanName, "UTF-8")
            }.getOrDefault(offerTicketPlanName)

            return "add_offer_detail/$offerTicketPlanId/$encodedName"
        }
    }

    data object EditOfferDetail : Screen("edit_offer_detail/{offerDetailId}") {
        fun createRoute(offerDetailId: String): String = "edit_offer_detail/$offerDetailId"
    }

    // offer detail
    data object OfferDetail : Screen("offer_detail/{offerDetailId}") {
        fun createRoute(offerDetailId: String) = "offer_detail/$offerDetailId"
    }
}

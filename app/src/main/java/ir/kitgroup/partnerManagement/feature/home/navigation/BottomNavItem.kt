package ir.kitgroup.partnerManagement.feature.home.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ir.kitgroup.partnerManagement.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {

    data object Dashboard : BottomNavItem(
        route = "dashboard",
        title = R.string.nav_dashboard,
        icon = R.drawable.ic_dashboard
    )

    data object Organizations : BottomNavItem(
        route = "organizations",
        title = R.string.nav_organizations,
        icon = R.drawable.ic_organizations
    )

    data object Visits : BottomNavItem(
        route = "visits",
        title = R.string.nav_visits,
        icon = R.drawable.ic_visits
    )

    data object PlanOffer : BottomNavItem(
        route = "planOffer",
        title = R.string.nav_plan_offer,
        icon = R.drawable.ic_plan_offer
    )

    data object Profile : BottomNavItem(
        route = "profile",
        title = R.string.nav_profile,
        icon = R.drawable.ic_profile
    )
}

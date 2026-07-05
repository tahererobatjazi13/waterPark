package ir.kitgroup.hotel.feature.home.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ir.kitgroup.hotel.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {

    data object Dashboard : BottomNavItem(
        route = "dashboard",
        title = R.string.nav_dashboard,
        icon = R.drawable.ic_home
    )

    data object Collections : BottomNavItem(
        route = "collections",
        title = R.string.nav_collaborative_collections,
        icon = R.drawable.ic_collaborative_collections
    )

    data object Visits : BottomNavItem(
        route = "visits",
        title = R.string.nav_visits,
        icon = R.drawable.ic_visits
    )

    data object Cards : BottomNavItem(
        route = "cards",
        title = R.string.nav_cards,
        icon = R.drawable.ic_cards
    )

    data object Profile : BottomNavItem(
        route = "profile",
        title = R.string.nav_profile,
        icon = R.drawable.ic_profile
    )
}

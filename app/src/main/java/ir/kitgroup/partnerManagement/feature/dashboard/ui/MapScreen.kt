package ir.kitgroup.partnerManagement.feature.dashboard.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.viewinterop.AndroidView
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun MapScreen(
    organizations: List<OrganizationEntity>,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val appColors = LocalPartnerManagementColors.current

    // راه‌اندازی مقادیر پیش‌فرض کتابخانه OSMdroid
    DisposableEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName

        onDispose {
            Configuration.getInstance().save(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        }
    }

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_map,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        controller.setZoom(12.0)
                        controller.setCenter(
                            GeoPoint(36.2972, 59.6067) // مرکز مشهد
                        )
                        setMultiTouchControls(true)
                    }
                },
                update = { mapView ->

                    // 1. نمایش مارکر برای وی سازمان
                    mapView.overlays.clear()

                    organizations.forEach { org ->
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(org.latitude!!, org.longitude!!)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = org.name
                        marker.infoWindow = null
                        mapView.overlays.add(marker)
                    }
                }
            )
           /* ExtendedFloatingActionButton(
                onClick = {  },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(52.dp)
            ) {
                Icon(
                    imageVector =  Icons.Default.MyLocation,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
        }*/

        }
    }
}

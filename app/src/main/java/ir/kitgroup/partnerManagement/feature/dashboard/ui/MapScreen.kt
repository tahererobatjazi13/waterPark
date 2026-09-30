package ir.kitgroup.partnerManagement.feature.dashboard.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MapScreen(
        organizations = uiState.organizations,
        isLoading = uiState.isLoading,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun MapScreen(
    organizations: List<OrganizationEntity>,
    isLoading: Boolean = false,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // راه‌اندازی و مدیریت چرخه حیات MapView و تنظیمات OSMdroid
    DisposableEffect(mapView) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName

        mapView.onResume()

        onDispose {
            mapView.onPause()
            Configuration.getInstance().save(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        }
    }

    Scaffold(
        modifier = modifier,
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
                factory = {
                    mapView.apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        controller.setZoom(12.0)
                        controller.setCenter(GeoPoint(36.2972, 59.6067)) // مرکز مشهد
                        setMultiTouchControls(true)
                    }
                },
                update = { view ->
                    // بازنشانی و ترسیم مجدد مارکرها در صورت تغییر لیست سازمان‌ها در دیتابیس
                    view.overlays.clear()

                    organizations.forEach { org ->
                        val lat = org.latitude?.toDoubleOrNull()
                        val lon = org.longitude?.toDoubleOrNull()

                        if (lat != null && lon != null) {
                            val marker = Marker(view).apply {
                                position = GeoPoint(lat, lon)
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = org.name
                                snippet = org.phone ?: ""
                                infoWindow = null
                            }
                            view.overlays.add(marker)
                        }
                    }
                    view.invalidate()
                }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

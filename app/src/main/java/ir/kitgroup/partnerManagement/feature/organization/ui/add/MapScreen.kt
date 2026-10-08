package ir.kitgroup.partnerManagement.feature.organization.ui.add

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import androidx.core.content.ContextCompat

import androidx.navigation.NavController

import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader

import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(
    onBackClick: () -> Unit,
    navController: NavController
) {

    val context = LocalContext.current

    // ---------------------------------------------------------
    // Map configuration
    // ---------------------------------------------------------

    Configuration.getInstance().userAgentValue =
        context.packageName

    // ---------------------------------------------------------
    // Selected location
    // ---------------------------------------------------------

    var selectedLocation by remember {
        mutableStateOf<GeoPoint?>(null)
    }

    var isLocationLoading by remember {
        mutableStateOf(true)
    }

    // ---------------------------------------------------------
    // Map references
    // ---------------------------------------------------------

    var mapViewRef by remember {
        mutableStateOf<MapView?>(null)
    }

    var markerRef by remember {
        mutableStateOf<Marker?>(null)
    }

    var myLocationOverlay by remember {
        mutableStateOf<MyLocationNewOverlay?>(null)
    }

    // ---------------------------------------------------------
    // Permission
    // ---------------------------------------------------------

    var hasLocationPermission by remember {

        mutableStateOf(

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||

                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            hasLocationPermission =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (!hasLocationPermission) {
                isLocationLoading = false
            }
        }

    // ---------------------------------------------------------
    // Update selected location and marker
    // ---------------------------------------------------------

    fun updateSelectedLocation(
        point: GeoPoint,
        mapView: MapView? = mapViewRef
    ) {

        selectedLocation = point

        val currentMap = mapView ?: return

        val marker = markerRef ?: Marker(currentMap).apply {

            setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            title = context.getString(
                R.string.location_organization
            )

            isDraggable = true

        }.also {

            markerRef = it
            currentMap.overlays.add(it)

        }

        marker.position = point

        currentMap.controller.animateTo(point)

        currentMap.invalidate()
    }

    // ---------------------------------------------------------
    // Request permission
    // ---------------------------------------------------------

    LaunchedEffect(mapViewRef) {

        if (
            mapViewRef != null &&
            !hasLocationPermission
        ) {

            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // ---------------------------------------------------------
    // Get current GPS location automatically
    // ---------------------------------------------------------

    LaunchedEffect(
        mapViewRef,
        hasLocationPermission
    ) {

        val mapView = mapViewRef
            ?: return@LaunchedEffect

        if (!hasLocationPermission) {
            isLocationLoading = false
            return@LaunchedEffect
        }

        isLocationLoading = true

        // Create GPS overlay
        val overlay = MyLocationNewOverlay(
            GpsMyLocationProvider(context),
            mapView
        )

        myLocationOverlay = overlay

        mapView.overlays.add(overlay)

        // Start GPS
        overlay.enableMyLocation()

        // Wait for first GPS fix
        overlay.runOnFirstFix {

            val location = overlay.myLocation

            if (location != null) {

                val currentPoint = GeoPoint(
                    location.latitude,
                    location.longitude
                )

                mapView.post {

                    updateSelectedLocation(
                        point = currentPoint,
                        mapView = mapView
                    )

                    mapView.controller.setZoom(17.0)

                    isLocationLoading = false

                    mapView.invalidate()
                }
            }
        }
    }

    // ---------------------------------------------------------
    // Map lifecycle
    // ---------------------------------------------------------

    DisposableEffect(mapViewRef) {

        val mapView = mapViewRef

        onDispose {

            myLocationOverlay?.disableMyLocation()

            myLocationOverlay?.disableFollowLocation()

            mapView?.onPause()

            mapView?.onDetach()

            myLocationOverlay = null
            markerRef = null
        }
    }

    // ---------------------------------------------------------
    // UI
    // ---------------------------------------------------------

    Scaffold(

        topBar = {

            CustomHeader(
                title = stringResource(
                    R.string.label_location_map
                ),
                showBackButton = true,
                onBackClick = onBackClick
            )
        },

        floatingActionButton = {

            Column(

                verticalArrangement = Arrangement.spacedBy(12.dp),

                horizontalAlignment = Alignment.End

            ) {

                // Go to current GPS location

                SmallFloatingActionButton(

                    onClick = {

                        if (!hasLocationPermission) {

                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )

                            return@SmallFloatingActionButton
                        }

                        val location = myLocationOverlay?.myLocation

                        if (location != null) {

                            val point = GeoPoint(
                                location.latitude,
                                location.longitude
                            )

                            updateSelectedLocation(point)

                            mapViewRef?.controller?.setZoom(17.0)

                            isLocationLoading = false

                        } else {

                            isLocationLoading = true

                            myLocationOverlay?.enableMyLocation()
                        }
                    },

                    containerColor = MaterialTheme.colorScheme.secondaryContainer

                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(
                            R.string.location_current
                        )
                    )
                }

                // Confirm selected location

                ExtendedFloatingActionButton(

                    onClick = {

                        val location = selectedLocation
                            ?: return@ExtendedFloatingActionButton

                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                "selected_latitude",
                                location.latitude.toString()
                            )

                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                "selected_longitude",
                                location.longitude.toString()
                            )

                        navController.popBackStack()
                    },

                    icon = {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(
                                R.string.action_confirm
                            )
                        )
                    },

                    text = {
                        Text(stringResource(R.string.action_confirm_location))
                    })
            }
        }

    ) { paddingValues ->

        Box(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

        ) {

            // -------------------------------------------------
            // MapView
            // -------------------------------------------------

            AndroidView(

                modifier = Modifier.fillMaxSize(),

                factory = { ctx ->

                    MapView(ctx).apply {

                        // Map settings

                        setTileSource(
                            TileSourceFactory.MAPNIK
                        )

                        setMultiTouchControls(true)

                        controller.setZoom(15.0)

                        // Do not set Tehran as initial center.

                        // Map tap events

                        val mapEventsReceiver =
                            object : MapEventsReceiver {

                                override fun singleTapConfirmedHelper(
                                    p: GeoPoint
                                ): Boolean {

                                    updateSelectedLocation(
                                        point = GeoPoint(
                                            p.latitude,
                                            p.longitude
                                        ),
                                        mapView = this@apply
                                    )

                                    return true
                                }

                                override fun longPressHelper(
                                    p: GeoPoint
                                ): Boolean {

                                    updateSelectedLocation(
                                        point = GeoPoint(
                                            p.latitude,
                                            p.longitude
                                        ),
                                        mapView = this@apply
                                    )

                                    return true
                                }
                            }

                        overlays.add(
                            MapEventsOverlay(mapEventsReceiver)
                        )

                        // Save map reference

                        mapViewRef = this

                        onResume()
                    }
                },

                update = { mapView ->

                    selectedLocation?.let { point ->

                        markerRef?.position = point
                    }

                    mapView.invalidate()
                }
            )

            // -------------------------------------------------
            // Loading indicator
            // -------------------------------------------------

            if (isLocationLoading) {

                Column(

                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(
                                alpha = 0.95f
                            ),
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(20.dp),

                    horizontalAlignment = Alignment.CenterHorizontally,

                    verticalArrangement = Arrangement.spacedBy(12.dp)

                ) {

                    CircularProgressIndicator()

                    Text(
                        text = (stringResource(R.string.label_retrieving_phone_current_location)),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // -------------------------------------------------
            // Permission denied message
            // -------------------------------------------------

            if (
                !hasLocationPermission &&
                !isLocationLoading
            ) {

                Text(
                    text = (stringResource(R.string.msg_enable_location_access_current_location)),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
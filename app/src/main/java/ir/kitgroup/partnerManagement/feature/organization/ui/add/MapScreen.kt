package ir.kitgroup.partnerManagement.feature.organization.ui.add

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import android.graphics.Bitmap
import android.graphics.Canvas

@Composable
fun MapScreen(
    onBackClick: () -> Unit,
    navController: NavController,
    initialLatitude: Double? = null,
    initialLongitude: Double? = null,
    isReadOnly: Boolean = false
) {
    val context = LocalContext.current

    val hasInitialLocation = initialLatitude != null && initialLongitude != null

    // مقداردهی اولیه مکان بر اساس پارامترهای ورودی
    var selectedLocation by remember {
        mutableStateOf(
            if (hasInitialLocation) GeoPoint(initialLatitude!!, initialLongitude!!) else null
        )
    }

    // اگر مختصات اولیه داریم، دیگر منتظر لود GPS نمی‌مانیم
    var isLocationLoading by remember {
        mutableStateOf(!hasInitialLocation)
    }

    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    var markerRef by remember { mutableStateOf<Marker?>(null) }
    var myLocationOverlay by remember { mutableStateOf<MyLocationNewOverlay?>(null) }

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

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (!hasLocationPermission) {
            isLocationLoading = false
        }
    }

    fun updateSelectedLocation(
        point: GeoPoint,
        mapView: MapView? = mapViewRef,
        animate: Boolean = true
    ) {
        selectedLocation = point
        val currentMap = mapView ?: return

        val marker = markerRef ?: Marker(currentMap).apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = context.getString(R.string.location_organization)
            isDraggable = !isReadOnly

            icon = ContextCompat.getDrawable(context, R.drawable.ic_location_pin)
        }.also {
            markerRef = it
            if (!currentMap.overlays.contains(it)) {
                currentMap.overlays.add(it)
            }
        }

        marker.position = point

        if (animate) {
            currentMap.controller.animateTo(point)
        } else {
            currentMap.controller.setCenter(point)
        }
        currentMap.invalidate()
    }


    // درخواست پرمیشن فقط در صورتی که موقعیت اولیه وجود نداشته باشد
    LaunchedEffect(mapViewRef) {
        if (mapViewRef != null && !hasLocationPermission && !hasInitialLocation) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // مدیریت لایه GPS
    LaunchedEffect(mapViewRef, hasLocationPermission) {
        val mapView = mapViewRef ?: return@LaunchedEffect

        if (!hasLocationPermission) {
            if (!hasInitialLocation) {
                isLocationLoading = false
            }
            return@LaunchedEffect
        }

        val overlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
            // تغییر آیکون نقطه مکانی کنونی کاربر
            val customPersonBitmap = getBitmapFromDrawable(context, R.drawable.ic_my_location_dot)
            if (customPersonBitmap != null) {
                setPersonIcon(customPersonBitmap)
                setPersonAnchor(0.5f, 0.5f)
                // در صورت تمایل برای حالت جهت‌نما حین حرکت (Direction Arrow):
                // setDirectionIcon(customDirectionBitmap)
                // setDirectionAnchor(0.5f, 0.5f)
            }
        }
        myLocationOverlay = overlay
        mapView.overlays.add(overlay)
        overlay.enableMyLocation()

        // فقط در حالتی که مختصات اولیه وجود ندارد، نقشه به مکان کاربر منتقل شود
        if (!hasInitialLocation) {
            isLocationLoading = true
            overlay.runOnFirstFix {
                val location = overlay.myLocation
                if (location != null) {
                    val currentPoint = GeoPoint(location.latitude, location.longitude)
                    mapView.post {
                        updateSelectedLocation(point = currentPoint, mapView = mapView)
                        mapView.controller.setZoom(17.0)
                        isLocationLoading = false
                        mapView.invalidate()
                    }
                }
            }
        }
    }

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

    Scaffold(
        topBar = {
            CustomHeader(
                title = stringResource(R.string.label_location_map),
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                // دکمه بازگشت به مکان GPS کاربر
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
                            val point = GeoPoint(location.latitude, location.longitude)
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
                        contentDescription = stringResource(R.string.location_current)
                    )
                }

                // دکمه تایید موقعیت (تنها در صورتی که ReadOnly نباشد نمایش می‌یابد)
                if (!isReadOnly) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            val location = selectedLocation ?: return@ExtendedFloatingActionButton
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_latitude", location.latitude.toString())
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_longitude", location.longitude.toString())

                            navController.popBackStack()
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(R.string.action_confirm)
                            )
                        },
                        text = {
                            Text(stringResource(R.string.action_confirm_location))
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(16.5)

                        // در صورتی که نقطه ارسالی موجود باشد، مستقیماً مرکز نقشه قرار می‌گیرد
                        if (hasInitialLocation) {
                            val initialPoint = GeoPoint(initialLatitude!!, initialLongitude!!)
                            controller.setCenter(initialPoint)

                            val marker = Marker(this).apply {
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = context.getString(R.string.location_organization)
                                isDraggable = !isReadOnly
                                position = initialPoint
                                icon =
                                    ContextCompat.getDrawable(context, R.drawable.ic_location_pin)
                            }

                            markerRef = marker
                            overlays.add(marker)
                        }

                        if (!isReadOnly) {
                            val mapEventsReceiver = object : MapEventsReceiver {
                                override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                                    updateSelectedLocation(
                                        point = GeoPoint(p.latitude, p.longitude),
                                        mapView = this@apply
                                    )
                                    return true
                                }

                                override fun longPressHelper(p: GeoPoint): Boolean {
                                    updateSelectedLocation(
                                        point = GeoPoint(p.latitude, p.longitude),
                                        mapView = this@apply
                                    )
                                    return true
                                }
                            }
                            overlays.add(MapEventsOverlay(mapEventsReceiver))
                        }

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

            if (isLocationLoading) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BouncingDotsIndicator()
                    Text(
                        text = stringResource(R.string.label_retrieving_phone_current_location),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (!hasLocationPermission && !isLocationLoading && !hasInitialLocation) {
                Text(
                    text = stringResource(R.string.msg_enable_location_access_current_location),
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

private fun getBitmapFromDrawable(context: android.content.Context, drawableId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth.takeIf { it > 0 } ?: 96,
        drawable.intrinsicHeight.takeIf { it > 0 } ?: 96,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}


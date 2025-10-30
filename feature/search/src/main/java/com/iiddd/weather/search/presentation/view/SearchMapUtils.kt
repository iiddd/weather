package com.iiddd.weather.search.presentation.view

import androidx.compose.ui.geometry.Offset
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlin.math.PI
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.tan

/**
 * Converts the marker's LatLng to screen coordinates
 * based on the map's dimensions and current camera position.
 * Uses the same Web Mercator projection calculation as in SearchContent.
 */
fun computeScreenPosition(
    marker: LatLng,
    cameraPosition: CameraPosition,
    mapWidth: Float,
    mapHeight: Float
): Offset {
    if (mapWidth <= 0f || mapHeight <= 0f) return Offset.Zero

    val zoom = cameraPosition.zoom.toDouble()

    fun lonToX(lon: Double): Double = (lon + 180.0) / 360.0
    fun latToY(lat: Double): Double {
        val latRad = Math.toRadians(lat)
        return 0.5 - ln(tan(PI / 4.0 + latRad / 2.0)) / (2.0 * PI)
    }

    val worldPixelSize = 256.0 * 2.0.pow(zoom)

    val markerX = lonToX(marker.longitude) * worldPixelSize
    val markerY = latToY(marker.latitude) * worldPixelSize

    val center = cameraPosition.target
    val centerX = lonToX(center.longitude) * worldPixelSize
    val centerY = latToY(center.latitude) * worldPixelSize

    val dx = markerX - centerX
    val dy = markerY - centerY

    val screenCenterX = mapWidth / 2.0
    val screenCenterY = mapHeight / 2.0

    val pxX = (screenCenterX + dx).toFloat()
    val pxY = (screenCenterY + dy).toFloat()

    return Offset(pxX, pxY)
}
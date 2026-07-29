package ir.kitgroup.partnerManagement.core.ui.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class SideCurvedHeaderShape(private val curveRadius: Float = 60f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - curveRadius)

            quadraticTo(
                size.width, size.height,
                size.width - curveRadius, size.height
            )

            lineTo(curveRadius, size.height)

            quadraticTo(
                0f, size.height,
                0f, size.height - curveRadius
            )

            close()
        }
        return Outline.Generic(path)
    }
}

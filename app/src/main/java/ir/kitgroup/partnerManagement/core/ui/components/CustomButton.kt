package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    isLoading: Boolean = false,
    enabled: Boolean = true,
    fillMaxWidth: Boolean = true,
    height: Dp? = 50.dp,
    cornerRadius: Dp = 16.dp,
    textStyle: TextStyle = typography.titleLarge,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    val finalModifier = modifier
        .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)

    Button(
        onClick = onClick,
        modifier = finalModifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = colors,
        enabled = enabled && !isLoading,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = contentPadding
    ) {
        // تغییر نرم و با انیمیشن بین حالت دکمه و حالت لودینگ
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
            },
            label = "ButtonContentTransition"
        ) { loading ->
            if (loading) {
                BouncingDotsIndicator(
                    color = LocalContentColor.current,
                    dotSize = 7.dp,
                    bounceHeight = 6.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = text,
                        style = textStyle
                    )
                }
            }
        }
    }
}

/**
 * کامپوننت ۳ نقطه متحرک (Bouncing Dots)
 * کاملاً برداری، سبک و هماهنگ با تم جاری
 */
@Composable
fun BouncingDotsIndicator(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    dotSize: Dp = 7.dp,
    bounceHeight: Dp = 6.dp,
    delayUnit: Int = 140
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BouncingDots")

    @Composable
    fun animateDot(delay: Int): Float {
        val offset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -bounceHeight.value,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    0f at delay using FastOutSlowInEasing
                    -bounceHeight.value at (delay + 300) using FastOutSlowInEasing
                    0f at (delay + 600) using FastOutSlowInEasing
                    0f at 1000
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "DotOffset"
        )
        return offset
    }

    val offset1 = animateDot(delay = 0)
    val offset2 = animateDot(delay = delayUnit)
    val offset3 = animateDot(delay = delayUnit * 2)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .offset(y = offset1.dp)
                .size(dotSize)
                .clip(CircleShape)
                .background(color)
        )
        Box(
            modifier = Modifier
                .offset(y = offset2.dp)
                .size(dotSize)
                .clip(CircleShape)
                .background(color)
        )
        Box(
            modifier = Modifier
                .offset(y = offset3.dp)
                .size(dotSize)
                .clip(CircleShape)
                .background(color)
        )
    }
}

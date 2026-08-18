package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {

    var showContent by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        showContent = true
    }
    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                appColors.appBackground
            ),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(
                animationSpec = tween(700)
            )
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.ic_logo
                    ),
                    contentDescription = stringResource(
                        R.string.app_name
                    ),
                    modifier = Modifier.size(200.dp)
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = stringResource(
                        R.string.app_name
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = stringResource(
                        R.string.splash_loading
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun SplashScreenPreview() {
    AppScreenPreview {
        SplashScreen()
    }
}
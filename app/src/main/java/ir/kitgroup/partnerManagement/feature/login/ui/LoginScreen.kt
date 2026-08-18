package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import androidx.compose.ui.tooling.preview.Preview
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToDashboard -> {
                    navController.navigate(
                        BottomNavItem.Dashboard.route
                    ) {
                        popUpTo("login") {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            }
        }
    }
    LoginContent(
        uiState = uiState,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLoginClick,
        onForgotPasswordClick = {
            // navController.navigate("forgot_password")
        }
    )
}
@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(appColors.appBackground)
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
            .padding(horizontal = 32.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 40.dp),
            contentScale = ContentScale.Fit
        )

        CustomEditTextField(
            value = uiState.username,
            onValueChange = onUsernameChange,
            label = stringResource(R.string.label_username),
            placeholder = stringResource(R.string.hint_enter_your_username),
            leadingIcon = painterResource(R.drawable.ic_user_name),
            errorMessage = uiState.usernameErrorRes?.let { stringResource(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomEditTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.label_password),
            placeholder = stringResource(R.string.hint_enter_your_password),
            leadingIcon = painterResource(R.drawable.ic_lock),
            isPasswordField = true,
            errorMessage = uiState.passwordErrorRes?.let { stringResource(it) }
        )

        uiState.loginErrorRes?.let { errorRes ->
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        TextButton(
            onClick = onForgotPasswordClick,
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = stringResource(R.string.label_forgot_password),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        CustomButton(
            text = stringResource(R.string.label_login),
            onClick = {
                focusManager.clearFocus()
                onLoginClick()
            },
            icon = Icons.AutoMirrored.Filled.Login,
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun LoginContentPreview() {
    AppScreenPreview {
        LoginContent(
            uiState = LoginUiState(
                username = "s",
                password = "123"
            ),
            onUsernameChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onForgotPasswordClick = {}
        )
    }
}
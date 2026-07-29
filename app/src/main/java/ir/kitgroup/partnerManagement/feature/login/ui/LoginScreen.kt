package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.navigation.NavController
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem

@Composable
fun LoginScreen(
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val usernameRequiredError = stringResource(R.string.error_username_required)
    val passwordRequiredError = stringResource(R.string.error_password_required)

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            value = username,
            onValueChange = {
                username = it
                usernameError = null
            },
            label = stringResource(id = R.string.label_username),
            placeholder = stringResource(id = R.string.hint_enter_your_username),
            leadingIcon = painterResource(id = R.drawable.ic_user_name),
            errorMessage = usernameError
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomEditTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = stringResource(id = R.string.label_password),
            placeholder = stringResource(id = R.string.hint_enter_your_password),
            leadingIcon = painterResource(id = R.drawable.ic_lock),
            isPasswordField = true,
            errorMessage = passwordError
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextButton(
            onClick = { /* ناوبری به بازیابی رمز */ },
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = stringResource(id = R.string.label_forgot_password),
                style = typography.titleMedium,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        CustomButton(
            text = stringResource(R.string.label_login),
            onClick = {
                focusManager.clearFocus()

                usernameError = null
                passwordError = null

                var isValid = true

                if (username.isBlank()) {
                    usernameError = usernameRequiredError
                    isValid = false
                }

                if (password.isBlank()) {
                    passwordError = passwordRequiredError
                    isValid = false
                }

                if (isValid) {
                    navController.navigate(BottomNavItem.Dashboard.route) {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = painterResource(R.drawable.ic_login_arrow)
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}

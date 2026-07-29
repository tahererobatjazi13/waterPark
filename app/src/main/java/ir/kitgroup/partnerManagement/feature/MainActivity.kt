package ir.kitgroup.partnerManagement.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // بسیار مهم برای استفاده از by
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.navigation.AppNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // استفاده از delegation ابزار activity-ktx برای کلاس‌های هیلت خارج از اسکوپ کامپوز
    private val viewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // خواندن استیت تم دقیقا داخل متد کامپوزبل
            val themeMode by viewModel.themeMode.collectAsState()

            PartnerManagementTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

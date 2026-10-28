package ir.kitgroup.partnerManagement

import android.app.Application
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import ir.kitgroup.partnerManagement.core.network.BaseUrlProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import javax.inject.Inject

@HiltAndroidApp
class MyBaseApplication : Application() {

    @Inject
    lateinit var baseUrlProvider: BaseUrlProvider

    override fun onCreate() {
        super.onCreate()

        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        ).launch {
            baseUrlProvider.init()
        }
    }
}
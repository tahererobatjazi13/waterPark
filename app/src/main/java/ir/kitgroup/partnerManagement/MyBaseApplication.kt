package ir.kitgroup.partnerManagement

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ir.kitgroup.partnerManagement.core.network.BaseUrlProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyBaseApplication : Application() {

    @Inject
    lateinit var baseUrlProvider: BaseUrlProvider

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        ).launch {
            baseUrlProvider.init()
        }
    }
}
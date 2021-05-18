package com.gamapp.sportshopapplication


import android.app.Application
import com.backendless.Backendless
import com.gamapp.sportshopapplication.network.connection.NetworkMonitor
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import ir.tapsell.sdk.Tapsell


class AppOld : Application() {


    companion object {
        const val TAPSELL_KEY: String =
            "sppccjfqpmonttbghsqlnfrgtqahoaglgoseprodsojjmgqqqqljhelirrcmkotfbnhqaq"
        const val DATA_URL: String =
            "https://api.backendless.com/D3464AB6-36B7-319F-FF8D-87E818BCA100/97FD11C8-1B81-4002-80FE-17B1CEA57C98/data/"
        var TAG = "tag345345"
        private val applicationId = "D3464AB6-36B7-319F-FF8D-87E818BCA100"
        private val androidKey = "97FD11C8-1B81-4002-80FE-17B1CEA57C98"
        private val SERVER_URL = "https://api.backendless.com"
    }

    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        Tapsell.initialize(this, TAPSELL_KEY);
        NetworkMonitor(this).startNetworkCallback()
        initBackendLess()
    }

    private fun initBackendLess() {
        Backendless.initApp(applicationContext, applicationId, androidKey)
        Backendless.setUrl(SERVER_URL)
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }
}
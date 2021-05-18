package com.gamapp.sportshopapplication;

import android.app.Application;

import com.backendless.Backendless;
import com.gamapp.sportshopapplication.network.connection.NetworkMonitor;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.tapsell.sdk.Tapsell;

public class App extends Application {
    private final static String TAPSELL_KEY =
            "sppccjfqpmonttbghsqlnfrgtqahoaglgoseprodsojjmgqqqqljhelirrcmkotfbnhqaq";
    public final static String DATA_URL =
            "https://api.backendless.com/86115250-B3DB-A58E-FF2F-42BCB05E2700/A8FDEA85-FD26-4740-B893-1083E079092C/data/";
    String TAG = "tag345345";
    final private String applicationId = "86115250-B3DB-A58E-FF2F-42BCB05E2700";
    final private String androidKey = "463629CE-895A-4399-B668-0E116334B6EE";
    final private String SERVER_URL = "https://api.backendless.com";


    @Override
    public void onCreate() {
        super.onCreate();
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("font/dana_fanu_light.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        Tapsell.initialize(this, TAPSELL_KEY);
        new NetworkMonitor(this).startNetworkCallback();
        initBackendLess();

    }

    private void initBackendLess() {
        Backendless.initApp(getApplicationContext(), applicationId, androidKey);
        Backendless.setUrl(SERVER_URL);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        new NetworkMonitor(this).stopNetworkCallback();
    }
}

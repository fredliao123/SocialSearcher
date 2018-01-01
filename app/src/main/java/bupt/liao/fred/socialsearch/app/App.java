package bupt.liao.fred.socialsearch.app;

import android.app.Application;
import android.content.Context;

import bupt.liao.fred.socialsearch.BuildConfig;
import bupt.liao.fred.socialsearch.app.data.SharedPrefsHelperModule;
import bupt.liao.fred.socialsearch.app.gps.GPSmanagerModule;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:Application of the AppModule
 */

public final class App extends Application {
    private AppComponent component;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        buildApplicationComponent();
        plantLoggingTree();
        context = getApplicationContext();
    }

    /**
     * Init DI, provide Application Component for other object to use
     */
    private void buildApplicationComponent() {
        component = DaggerAppComponent.builder().
                appModule(new AppModule(this)).
                gPSmanagerModule(new GPSmanagerModule()).
                sharedPrefsHelperModule(new SharedPrefsHelperModule()).
                build();
    }

    /**
     * For log
     */
    private void plantLoggingTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent getComponent() {
        return component;
    }

    public Context getContext() {
        return context;
    }


}

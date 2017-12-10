package bupt.liao.fred.socialsearch.app;

import android.app.Application;
import android.content.Context;

import bupt.liao.fred.socialsearch.BuildConfig;
import bupt.liao.fred.socialsearch.app.di.ApplicationComponent;
import bupt.liao.fred.socialsearch.app.di.ApplicationModule;
import bupt.liao.fred.socialsearch.app.di.DaggerApplicationComponent;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:Application of the App
 */

public final class BaseApplication extends Application {

    private static ApplicationComponent component;
    private static Context context;

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
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    /**
     * For log
     */
    private void plantLoggingTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /**
     * For crash report in release version
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            // implement crash reporting here
        }
    }

    public static ApplicationComponent getComponent() {
        return component;
    }

    public static Context getContext() {
        return context;
    }
}

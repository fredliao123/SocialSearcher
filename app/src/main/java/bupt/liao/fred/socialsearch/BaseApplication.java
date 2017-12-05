package bupt.liao.fred.socialsearch;

import android.app.Application;

import bupt.liao.fred.socialsearch.di.ApplicationComponent;
import bupt.liao.fred.socialsearch.di.DaggerApplicationComponent;
import bupt.liao.fred.socialsearch.di.module.NetWorkModule;
import bupt.liao.fred.socialsearch.di.module.TwitterModule;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public final class BaseApplication extends Application {
    private ApplicationComponent component;

    @Override public void onCreate() {
        super.onCreate();
        buildApplicationComponent();
        plantLoggingTree();
    }

    private void buildApplicationComponent() {
        component = DaggerApplicationComponent.builder()
                .twitterModule(new TwitterModule())
                .netWorkModule(new NetWorkModule())
                .build();
    }

    private void plantLoggingTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            // implement crash reporting with Crashlytics, Bugsnag or whatever if necessary
        }
    }
}

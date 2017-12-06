package bupt.liao.fred.socialsearch.app.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.app.BaseApplication;
import bupt.liao.fred.socialsearch.app.annotation.ApplicationContext;
import bupt.liao.fred.socialsearch.app.network.INetWorkApi;
import dagger.Component;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {
    void inject(BaseApplication baseApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    INetWorkApi getNetWorkApi();
}

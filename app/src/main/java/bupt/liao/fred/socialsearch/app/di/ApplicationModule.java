package bupt.liao.fred.socialsearch.app.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.app.annotation.ApplicationContext;
import bupt.liao.fred.socialsearch.app.network.INetWorkApi;
import bupt.liao.fred.socialsearch.app.network.NetWorkApiImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application app){
        application = app;
    }

    @Provides
    @Singleton
    INetWorkApi provideNetWorkApi(){
        return new NetWorkApiImpl(new ReactiveNetwork());
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    SharedPreferences provideSharedPreferences(){
        return application.getSharedPreferences("SocialSearch", Context.MODE_PRIVATE);
    }

}

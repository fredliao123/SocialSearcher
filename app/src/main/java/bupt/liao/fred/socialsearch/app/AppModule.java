package bupt.liao.fred.socialsearch.app;

import android.app.Application;
import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.app.network.INetWorkApi;
import bupt.liao.fred.socialsearch.app.network.NetWorkApiImpl;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description:Module for the Application
 */

@Module(includes = AndroidInjectionModule.class)
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    INetWorkApi provideNetWorkApi(Context context) {
        return new NetWorkApiImpl(new ReactiveNetwork(), context);
    }
}

package bupt.liao.fred.socialsearch.app;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.app.data.SharedPrefsHelper;
import bupt.liao.fred.socialsearch.app.data.SharedPrefsHelperModule;
import bupt.liao.fred.socialsearch.app.gps.GPSmanager;
import bupt.liao.fred.socialsearch.app.gps.GPSmanagerModule;
import bupt.liao.fred.socialsearch.app.network.INetWorkApi;
import bupt.liao.fred.socialsearch.app.network.NetWorkModule;
import bupt.liao.fred.socialsearch.app.rxbus.RxBus;
import bupt.liao.fred.socialsearch.app.rxbus.RxBusModule;
import bupt.liao.fred.socialsearch.web.WebActivity;
import dagger.Component;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description: Component for the Application
 */

@Singleton
@Component(modules = {
        AppModule.class,
        GPSmanagerModule.class,
        SharedPrefsHelperModule.class,
        RxBusModule.class,
        NetWorkModule.class})
public interface AppComponent {
    SharedPrefsHelper sharedPreferenceHelper();

    GPSmanager gpsManager();

    INetWorkApi netWorkApi();

    RxBus rxBus();

    void inject(WebActivity webActivity);
}

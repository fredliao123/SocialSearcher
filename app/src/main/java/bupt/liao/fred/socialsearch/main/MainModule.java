package bupt.liao.fred.socialsearch.main;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;

import bupt.liao.fred.socialsearch.app.data.SharedPrefsHelper;
import bupt.liao.fred.socialsearch.app.scope.PerAcitivity;
import bupt.liao.fred.socialsearch.main.permission.LocationPermissionManager;
import bupt.liao.fred.socialsearch.twitter.di.TwitterModule;
import bupt.liao.fred.socialsearch.twitter.di.TwitterViewModule;
import bupt.liao.fred.socialsearch.twitter.view.TwitterFragment;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Fred.Liao on 2017/12/28.
 * Email:fredliaobupt@qq.com
 * Description:
 */
@Module
public class MainModule {
    private MainContract.MainView view;

    MainModule(MainContract.MainView view){
        this.view = view;
    }

    @Provides
    @PerAcitivity
    MainContract.MainView provideMainView(){
        return view;
    }

    @Provides
    @PerAcitivity
    LocationPermissionManager provideLoacationPermissionManager(){
        return new LocationPermissionManager((AppCompatActivity) view, LocationPermissionManager.LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, false);
    }
}

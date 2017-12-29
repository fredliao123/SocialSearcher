package bupt.liao.fred.socialsearch.app.gps;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2017/12/29.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Module
public class GPSmanagerModule {

    @Provides
    @Singleton
    GPSmanager provideGPSmanager(Context context){
        return new GPSmanager(context);
    }
}

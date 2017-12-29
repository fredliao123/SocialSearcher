package bupt.liao.fred.socialsearch.app.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2017/12/29.
 * Email:fredliaobupt@qq.com
 * Description:
 */
@Module
public class SharedPrefsHelperModule {

    private static final String PREFS_KEY = "SocialSearch";

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
    }
}

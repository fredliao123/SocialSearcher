package bupt.liao.fred.socialsearch.main;

import bupt.liao.fred.socialsearch.app.data.SharedPrefsHelper;
import bupt.liao.fred.socialsearch.app.scope.PerAcitivity;
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
}

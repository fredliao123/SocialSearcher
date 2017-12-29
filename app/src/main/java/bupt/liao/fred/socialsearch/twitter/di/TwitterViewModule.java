package bupt.liao.fred.socialsearch.twitter.di;

import bupt.liao.fred.socialsearch.app.scope.PerFragment;
import bupt.liao.fred.socialsearch.twitter.TwitterContract;
import bupt.liao.fred.socialsearch.twitter.view.TwitterFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2017/12/28.
 * Email:fredliaobupt@qq.com
 * Description:
 */
@Module
public class TwitterViewModule {

    TwitterContract.TwitterView view;

    public TwitterViewModule(TwitterContract.TwitterView view){
        this.view = view;
    }

    @Provides
    @PerFragment
    public TwitterContract.TwitterView provideTwitterView(){
        return view;
    }

}

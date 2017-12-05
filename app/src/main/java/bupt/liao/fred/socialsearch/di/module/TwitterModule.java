package bupt.liao.fred.socialsearch.di.module;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.mvp.model.twitter.ITwitterApi;
import bupt.liao.fred.socialsearch.mvp.model.twitter.TwitterApiImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Module
public final class TwitterModule {
    @Provides
    @Singleton
    public ITwitterApi provideTwitterApi() {
        return new TwitterApiImpl();
    }
}

package bupt.liao.fred.socialsearch.twitter.di;

import android.content.Context;

import bupt.liao.fred.socialsearch.BuildConfig;
import bupt.liao.fred.socialsearch.app.gps.GPSmanager;
import bupt.liao.fred.socialsearch.app.network.INetWorkApi;
import bupt.liao.fred.socialsearch.app.scope.PerFragment;
import bupt.liao.fred.socialsearch.twitter.TwitterContract;
import bupt.liao.fred.socialsearch.twitter.model.ITwitterApi;
import bupt.liao.fred.socialsearch.twitter.model.TwitterApiImpl;
import bupt.liao.fred.socialsearch.twitter.presenter.TwitterPresenter;
import dagger.Module;
import dagger.Provides;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:DI Module for twitter.
 */

@Module
public final class TwitterModule {

    @Provides
    @PerFragment
    public ITwitterApi provideTwitterApi(Configuration configuration) {
        final TwitterFactory twitterFactory = new TwitterFactory(configuration);
        return new TwitterApiImpl(twitterFactory.getInstance());
    }

    @Provides
    @PerFragment
    public Configuration provideConfiguration() {
        final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken(BuildConfig.TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(BuildConfig.TWITTER_ACCESS_TOKEN_SECRET);

        return configurationBuilder.build();
    }

}

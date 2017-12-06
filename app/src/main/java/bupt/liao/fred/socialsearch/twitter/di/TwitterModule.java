package bupt.liao.fred.socialsearch.twitter.di;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.BuildConfig;
import bupt.liao.fred.socialsearch.twitter.model.ITwitterApi;
import bupt.liao.fred.socialsearch.twitter.model.TwitterApiImpl;
import dagger.Module;
import dagger.Provides;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

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
        final Configuration configuration = createConfiguration();
        final TwitterFactory twitterFactory = new TwitterFactory(configuration);
        return new TwitterApiImpl(twitterFactory.getInstance());
    }

    private Configuration createConfiguration() {
        final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken(BuildConfig.TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(BuildConfig.TWITTER_ACCESS_TOKEN_SECRET);

        return configurationBuilder.build();
    }
}

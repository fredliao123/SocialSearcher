package bupt.liao.fred.socialsearch;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import bupt.liao.fred.socialsearch.twitter.model.TwitterApiImpl;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by Fred.Liao on 2017/12/10.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@RunWith(AndroidJUnit4.class)
public class TwitterApiImplTest {
    @Test
    public void twitterApiImplTest_CanSearchTweets_ReturnsTrue(){
        // given
        Twitter twitter = Mockito.mock(Twitter.class);
        TwitterApiImpl twitterApiProvider = new TwitterApiImpl(twitter);
        String sampleKeyword = "sampleKeyword";

        // when
        boolean canSearchTweets = twitterApiProvider.canSearchTweets(sampleKeyword);

        // then
        assertThat(canSearchTweets).isTrue();
    }

    @Test
    public void twitterApiImplTest_CanSearchTweets_ReturnsFalse(){
        // given
        Twitter twitter = Mockito.mock(Twitter.class);
        TwitterApiImpl twitterApiProvider = new TwitterApiImpl(twitter);
        String sampleKeyword = "";

        // when
        boolean canSearchTweets = twitterApiProvider.canSearchTweets(sampleKeyword);

        // then
        assertThat(canSearchTweets).isFalse();
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

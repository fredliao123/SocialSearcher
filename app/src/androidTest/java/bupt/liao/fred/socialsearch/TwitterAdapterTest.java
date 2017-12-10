package bupt.liao.fred.socialsearch;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import bupt.liao.fred.socialsearch.twitter.view.TwitterAdapter;
import twitter4j.Status;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by Fred.Liao on 2017/12/10.
 * Email:fredliaobupt@qq.com
 * Description:Unit Test for TwitterAdapter
 */

@RunWith(AndroidJUnit4.class)
public class TwitterAdapterTest {


    @Test
    public void twitterAdapterTest_GetItemCount_ShouldBeSame() {
        // given
        Context context = InstrumentationRegistry.getContext();
        List<Status> tweets = new ArrayList<>();
        Status status = Mockito.mock(Status.class);
        tweets.add(status);

        // when
        TwitterAdapter tweetsAdapter = new TwitterAdapter(context, tweets);

        // then
        assertThat(tweetsAdapter.getItemCount()).isEqualTo(tweets.size());
    }

    @Test public void twitterAdapterTest_getLastTweetId_ShouldBeSame() {
        // given
        Context context = InstrumentationRegistry.getContext();
        List<Status> tweets = new ArrayList<>();
        Status status = Mockito.mock(Status.class);
        final long givenLastTweetId = 123L;
        Mockito.when(status.getId()).thenReturn(givenLastTweetId);
        tweets.add(status);

        // when
        TwitterAdapter tweetsAdapter = new TwitterAdapter(context, tweets);

        // then
        assertThat(tweetsAdapter.getLastTweetId()).isEqualTo(givenLastTweetId);
    }

    @Test public void twitterAdapterTest_getTweets_Size_ShouldBeSame() {
        // given
        Context context = InstrumentationRegistry.getContext();
        List<Status> tweets = new ArrayList<>();
        Status status = Mockito.mock(Status.class);
        tweets.add(status);
        tweets.add(status);

        // when
        TwitterAdapter tweetsAdapter = new TwitterAdapter(context, tweets);

        // then
        assertThat(tweetsAdapter.getTweets().size()).isEqualTo(tweets.size());
    }
}

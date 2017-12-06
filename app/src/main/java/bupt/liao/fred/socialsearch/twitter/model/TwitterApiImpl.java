package bupt.liao.fred.socialsearch.twitter.model;


import java.util.List;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.BuildConfig;
import bupt.liao.fred.socialsearch.app.Conf;
import rx.Observable;
import rx.Subscriber;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public final class TwitterApiImpl implements ITwitterApi {
    private static final int API_RATE_LIMIT_EXCEEDED_ERROR_CODE = 88;
    private final Twitter twitterInstance;

    @Inject
    public TwitterApiImpl(Twitter twitter) {
        twitterInstance = twitter;
    }



    @Override
    public Observable<List<Status>> searchTweets(final String keyword) {
        return Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                try {
                    final Query query = new Query(keyword).count(Conf.MAX_TWEET_PER_REQUEST);
                    final QueryResult result = twitterInstance.search(query);
                    subscriber.onNext(result.getTweets());
                    subscriber.onCompleted();
                } catch (TwitterException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<Status>> searchTweets(final String keyword, final long maxTweetId) {
        return Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                try {
                    final Query query = new Query(keyword).maxId(maxTweetId).count(Conf.MAX_TWEET_PER_REQUEST);
                    final QueryResult result = twitterInstance.search(query);
                    subscriber.onNext(result.getTweets());
                    subscriber.onCompleted();
                } catch (TwitterException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public int getApiRateLimitExceededErrorCode() {
        return API_RATE_LIMIT_EXCEEDED_ERROR_CODE;
    }

    @Override
    public boolean canSearchTweets(final String keyword) {
        return (!keyword.trim().isEmpty());
    }
}


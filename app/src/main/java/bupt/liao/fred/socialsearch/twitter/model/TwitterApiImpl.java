package bupt.liao.fred.socialsearch.twitter.model;


import java.util.List;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.app.Conf;
import rx.Observable;
import rx.Subscriber;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

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

    /**
     * Search for tweets
     *
     * @param keyword
     * @return
     */
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

    /**
     * Search for more tweets
     *
     * @param keyword
     * @param maxTweetId
     * @return
     */
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

    /**
     * Search for tweets with location
     *
     * @param keyword
     * @param latitude
     * @param longitude
     * @return
     */
    @Override
    public Observable<List<Status>> searchTweetsWithLocation(final String keyword, final double latitude, final double longitude) {
        final GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        return Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                try {
                    final Query query = new Query(keyword).count(Conf.MAX_TWEET_PER_REQUEST);
                    query.setGeoCode(geoLocation, Conf.GEOLOCATION_RADIUS, Query.MILES);
                    final QueryResult result = twitterInstance.search(query);
                    subscriber.onNext(result.getTweets());
                    subscriber.onCompleted();
                } catch (TwitterException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * Search for more tweets with location
     *
     * @param keyword
     * @param latitude
     * @param longitude
     * @param maxTweetId
     * @return
     */
    @Override
    public Observable<List<Status>> searchTweetsWithLocation(final String keyword, final double latitude, final double longitude, final long maxTweetId) {
        final GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        return Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                try {
                    final Query query = new Query(keyword).maxId(maxTweetId).count(Conf.MAX_TWEET_PER_REQUEST);
                    query.setGeoCode(geoLocation, Conf.GEOLOCATION_RADIUS, Query.MILES);
                    final QueryResult result = twitterInstance.search(query);
                    subscriber.onNext(result.getTweets());
                    subscriber.onCompleted();
                } catch (TwitterException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * Search for tweets until a date
     *
     * @param keyword
     * @param date
     * @return
     */
    @Override
    public Observable<List<Status>> searchTweetsWithDate(final String keyword, final String date) {
        return Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                try {
                    final Query query = new Query(keyword).count(Conf.MAX_TWEET_PER_REQUEST);
                    query.setUntil(date);
                    final QueryResult result = twitterInstance.search(query);
                    subscriber.onNext(result.getTweets());
                    subscriber.onCompleted();
                } catch (TwitterException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * Search for more tweets until a date
     *
     * @param keyword
     * @param date
     * @param maxTweetId
     * @return
     */
    @Override
    public Observable<List<Status>> searchTweetsWithDate(final String keyword, final String date, final long maxTweetId) {
        return Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                try {
                    final Query query = new Query(keyword).maxId(maxTweetId).count(Conf.MAX_TWEET_PER_REQUEST);
                    query.setUntil(date);
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


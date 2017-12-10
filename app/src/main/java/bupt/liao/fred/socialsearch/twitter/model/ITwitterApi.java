package bupt.liao.fred.socialsearch.twitter.model;

import java.util.List;

import rx.Observable;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:Interface for Twitter API
 */

public interface ITwitterApi {
    //Search tweets
    Observable<List<Status>> searchTweets(final String keyword);

    //Search more tweets
    Observable<List<Status>> searchTweets(final String keyword, final long maxTweetId);

    //Search for near tweets
    Observable<List<Status>> searchTweetsWithLocation(final String keyword, final double latitude, final double longitude);

    //Search for more near tweets
    Observable<List<Status>> searchTweetsWithLocation(final String keyword, final double latitude, final double longitude, final long maxTweetId);

    //Search for tweets until a date
    Observable<List<Status>> searchTweetsWithDate(final String keyword, final String date);

    //Search for more tweets until a date
    Observable<List<Status>> searchTweetsWithDate(final String keyword, final String date, final long maxTweetId);

    int getApiRateLimitExceededErrorCode();

    boolean canSearchTweets(final String keyword);
}

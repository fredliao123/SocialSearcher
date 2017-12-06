package bupt.liao.fred.socialsearch.twitter.model;

import java.util.List;

import rx.Observable;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public interface ITwitterApi {
    Observable<List<Status>> searchTweets(final String keyword);

    Observable<List<Status>> searchTweets(final String keyword, final long maxTweetId);

    int getApiRateLimitExceededErrorCode();


    boolean canSearchTweets(final String keyword);
}

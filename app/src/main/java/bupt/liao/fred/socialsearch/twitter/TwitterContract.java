package bupt.liao.fred.socialsearch.twitter;

import android.support.annotation.StringRes;

import java.util.List;

import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/27.
 * Email:fredliaobupt@qq.com
 * Description:
 */
public interface TwitterContract {
    interface TwitterView{
        void showSearchResult(final List<Status> tweets, final String keyword);
        void showErrorView();
        void showLoadingView();
        void showEmptyView();
        void showSnackBar(String msg);
        long getLastTweetId();
        void setPbLoadMoreTweetsVisible();
        void setPbLoadMoreTweetsGone();
        void createNewTweetsAdapterAndRefresh(List<Status> newTweets, int position);
        String getStringRes(@StringRes int id);
    }

    interface TwitterPresenter{
        void searchTweets(String keywords);
        void scrollToEnd(int firstVisibleItemPosition);
    }
}

package bupt.liao.fred.socialsearch.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.BaseApplication;
import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.model.network.INetWorkApi;
import bupt.liao.fred.socialsearch.mvp.model.twitter.ITwitterApi;
import bupt.liao.fred.socialsearch.mvp.view.adapter.TwitterAdapter;
import bupt.liao.fred.socialsearch.mvp.view.ui.TwitterFragment;
import butterknife.BindString;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class TwitterPresenter extends BasePresenter<TwitterFragment> {

    @BindString(R.string.no_internet_connection) public String msgNoInternetConnection;
    @BindString(R.string.cannot_load_more_tweets) public String msgCannotLoadMoreTweets;
    @BindString(R.string.no_tweets) public String msgNoTweets;
    @BindString(R.string.no_tweets_formatted) public String msgNoTweetsFormatted;
    @BindString(R.string.searched_formatted) public String msgSearchedFormatted;
    @BindString(R.string.api_rate_limit_exceeded) public String msgApiRateLimitExceeded;
    @BindString(R.string.error_during_search) public String msgErrorDuringSearch;

    private String keywords = "";

    private Subscription subDelayedSearch;
    private Subscription subSearchTweets;
    private Subscription subLoadMoreTweets;

    @Inject
    protected ITwitterApi twitterApi;
    @Inject
    protected INetWorkApi networkApi;

    public TwitterPresenter(){
        BaseApplication.getComponent().inject(this);
    }

    public static TwitterPresenter newInstance() {
        return new TwitterPresenter();
    }

    public void scrollToEnd(final int firstVisibleItemPosition){
        if (subLoadMoreTweets != null && !subLoadMoreTweets.isUnsubscribed()) {
            return;
        }

        final long lastTweetId = ((TwitterAdapter)getV().getRecyclerView().getAdapter()).getLastTweetId();

        subLoadMoreTweets = twitterApi.searchTweets(keywords, lastTweetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Status>>() {
                    @Override public void onStart() {
                        getV().getPbLoadMoreTweets().setVisibility(View.VISIBLE);
                        Timber.d("loading more tweets");
                    }

                    @Override public void onCompleted() {
                        getV().getPbLoadMoreTweets().setVisibility(View.GONE);
                        Timber.d("more tweets loaded");
                        unsubscribe();
                    }

                    @Override public void onError(Throwable e) {
                        if (!networkApi.isConnectedToInternet(BaseApplication.getContext())) {
                            getV().showSnackBar(msgNoInternetConnection);
                            Timber.d("no internet connection");
                        } else {
                            getV().showSnackBar(msgCannotLoadMoreTweets);
                        }
                        getV().getPbLoadMoreTweets().setVisibility(View.GONE);
                        Timber.d("couldn't load more tweets");
                    }

                    @Override public void onNext(List<Status> newTweets) {
                        getV().createNewTweetsAdapterAndRefresh(newTweets, firstVisibleItemPosition);
                    }
                });
    }
}

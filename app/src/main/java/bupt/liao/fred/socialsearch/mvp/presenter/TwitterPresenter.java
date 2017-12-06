package bupt.liao.fred.socialsearch.mvp.presenter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.BaseApplication;
import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.model.network.INetWorkApi;
import bupt.liao.fred.socialsearch.mvp.model.twitter.ITwitterApi;
import bupt.liao.fred.socialsearch.mvp.view.adapter.TwitterAdapter;
import bupt.liao.fred.socialsearch.mvp.view.ui.TwitterFragment;
import butterknife.BindString;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class TwitterPresenter extends BasePresenter<TwitterFragment> {

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
                            getV().showSnackBar(getV().msgNoInternetConnection);
                            Timber.d("no internet connection");
                        } else {
                            getV().showSnackBar(getV().msgCannotLoadMoreTweets);
                        }
                        getV().getPbLoadMoreTweets().setVisibility(View.GONE);
                        Timber.d("couldn't load more tweets");
                    }

                    @Override public void onNext(List<Status> newTweets) {
                        getV().createNewTweetsAdapterAndRefresh(newTweets, firstVisibleItemPosition);
                    }
                });
    }

    public void searchTweets(final String keyword) {
        Timber.d("attempting to search tweets with keyword %s", keyword);
        safelyUnsubscribe(subDelayedSearch, subLoadMoreTweets, subSearchTweets);
        keywords = keyword;

        if (!networkApi.isConnectedToInternet(BaseApplication.getContext())) {
            Timber.d("cannot search tweets - no internet connection");
            getV().showSnackBar(getV().msgNoInternetConnection);
            return;
        }

        if (!twitterApi.canSearchTweets(keyword)) {
            Timber.d("cannot search tweets - invalid keyword: %s", keyword);
            return;
        }

        subSearchTweets = twitterApi.searchTweets(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Status>>() {

                    @Override public void onStart() {
                        Timber.d("searching tweets for keyword: %s", keyword);
                        getV().getRecyclerView().setVisibility(View.GONE);
                        getV().getPbLoadingTweets().setVisibility(View.VISIBLE);
                    }

                    @Override public void onCompleted() {
                        // we don't have to implement this method

                    }

                    @Override public void onError(final Throwable e) {
                        final String message = getErrorMessage((TwitterException) e);
                        getV().showSnackBar(message);
                        getV().showErrorMessageContainer(message, R.drawable.no_tweets);
                        Timber.d("error during search: %s", message);
                    }

                    @Override public void onNext(final List<Status> tweets) {
                        Timber.d("search finished");
                        getV().getRecyclerView().setVisibility(View.VISIBLE);
                        getV().getPbLoadingTweets().setVisibility(View.GONE);
                        handleSearchResults(tweets, keyword);
                    }
                });
    }

    public void searchTweetsWithDelay(final String keyword) {
        Timber.d("starting delayed search");
        safelyUnsubscribe(subDelayedSearch);

        if (!twitterApi.canSearchTweets(keyword)) {
            Timber.d("cannot search tweets keyword %s is invalid", keyword);
            return;
        }

        // we are creating this delay to let user provide keyword
        // and omit not necessary requests
        subDelayedSearch = Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long milliseconds) {
                        searchTweets(keyword);
                    }
                });
    }

    public void safelyUnsubscribeAll(){
        safelyUnsubscribe(subDelayedSearch, subLoadMoreTweets, subSearchTweets);
    }

    private void safelyUnsubscribe(final Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
                Timber.d("subscription %s unsubscribed", subscription.toString());
            }
        }
    }

    @NonNull
    private String getErrorMessage(final TwitterException e) {
        if (e.getErrorCode() == twitterApi.getApiRateLimitExceededErrorCode()) {
            return getV().msgApiRateLimitExceeded;
        }
        return getV().msgErrorDuringSearch;
    }

    private void handleSearchResults(final List<Status> tweets, final String keyword) {
        Timber.d("handling search results");
        if (tweets.isEmpty()) {
            Timber.d("no tweets");
            final String message = String.format(getV().msgNoTweetsFormatted, keyword);
            getV().showSnackBar(message);
            getV().showErrorMessageContainer(message, R.drawable.no_tweets);
            return;
        }

        Timber.d("passing search results to view");
        getV().showSearchResult(tweets,keyword);
    }
}

package bupt.liao.fred.socialsearch.twitter.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.app.gps.GPSmanager;
import bupt.liao.fred.socialsearch.app.network.INetWorkApi;
import bupt.liao.fred.socialsearch.app.rxbus.CategoryEvent;
import bupt.liao.fred.socialsearch.app.rxbus.RxBus;
import bupt.liao.fred.socialsearch.twitter.TwitterContract;
import bupt.liao.fred.socialsearch.twitter.model.ITwitterApi;
import bupt.liao.fred.socialsearch.ui.common.BasePresenter;
import io.reactivex.functions.Consumer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:The Presenter for twitter activity
 */

public class TwitterPresenter extends BasePresenter<TwitterContract.TwitterView> implements TwitterContract.TwitterPresenter {
    //Search content
    private String keywords = "";

    private Subscription subSearchTweets;
    private Subscription subLoadMoreTweets;

    protected ITwitterApi twitterApi;
    protected GPSmanager mGPSmanager;
    protected INetWorkApi netWorkApi;
    protected RxBus rxBus;

    //flag for video search
    private volatile static boolean SEARCH_VIDEO = false;
    //flag for time search
    private volatile static boolean SEARCH_UNTIL = false;
    //flag for near search
    private volatile static boolean SEARCH_NEAR = false;
    //Date for time search
    private volatile static String UNITL_DATE = null;
    //Add string for video search
    private static final String VIDEO_FILTER = " filter:vine";

    @Inject
    public TwitterPresenter(TwitterContract.TwitterView view,
                            ITwitterApi twitterApi,
                            GPSmanager mGPSmanager,
                            INetWorkApi netWorkApi,
                            RxBus rxBus
    ) {
        super(view);
        this.twitterApi = twitterApi;
        this.mGPSmanager = mGPSmanager;
        this.netWorkApi = netWorkApi;
        this.rxBus = rxBus;
        initRxBus();
    }

    private void initRxBus() {
        rxBus.getEvents().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object event) {
                if (event instanceof CategoryEvent) {
                    clearSearchHint();
                    switch (((CategoryEvent) event).getTag()) {
                        case CategoryEvent.TAG_CLEAR:
                            break;
                        case CategoryEvent.TAG_NEAR:
                            SEARCH_NEAR = true;
                            break;
                        case CategoryEvent.TAG_VIDEO:
                            SEARCH_VIDEO = true;
                            break;
                        case CategoryEvent.TAG_UNTIL:
                            SEARCH_UNTIL = true;
                            UNITL_DATE = ((CategoryEvent) event).getDate();
                            break;
                        case CategoryEvent.TAG_DEFAULT:
                            break;
                    }
                }
            }
        });
    }

    private void clearSearchHint() {
        SEARCH_VIDEO = false;
        SEARCH_UNTIL = false;
        SEARCH_NEAR = false;
        UNITL_DATE = null;
    }

    /**
     * Method to load more tweets after user scroll to end
     *
     * @param firstVisibleItemPosition
     */
    @Override
    public void scrollToEnd(final int firstVisibleItemPosition) {
        if (subLoadMoreTweets != null && !subLoadMoreTweets.isUnsubscribed()) {
            return;
        }
        //No internet
        if (!netWorkApi.isConnectedToInternet()) {
            Timber.d("cannot search tweets - no internet connection");
            v.showErrorView();
            v.showSnackBar(v.getStringRes(R.string.no_internet_connection));
            return;
        }

        //To decide which id should load from
        final long lastTweetId = v.getLastTweetId();

        //Search for video
        if (SEARCH_VIDEO) {
            Timber.d("Load more for video, keyword is " + keywords);
            subLoadMoreTweets = twitterApi.searchTweets(keywords, lastTweetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchMoreTweetsSubscriber(firstVisibleItemPosition));
            return;
        }
        //Search for near tweets
        else if (SEARCH_NEAR) {
            mGPSmanager.getLocation();
            //check if the location is null
            if (mGPSmanager.canGetLocation()) {
                double latitude = mGPSmanager.getLatitude();
                double longitude = mGPSmanager.getLongitude();
                Timber.d("Can get location. Latitude is " + latitude + " longitude is " + longitude);
                subLoadMoreTweets = twitterApi.searchTweetsWithLocation(keywords, latitude, longitude, lastTweetId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getSearchMoreTweetsSubscriber(firstVisibleItemPosition));
            } else {
                // can't get location,GPS or Network is not enabled.Ask user to enable GPS/network in settings
                if (!mGPSmanager.showSettingsAlert()) {
                    //If GPS or Network is enabled, but still can't get position.Then it must caused by permission deny
                    v.showSnackBar(v.getStringRes(R.string.permission_rationale_location));
                }
            }
        }
        //Search tweets until a certain date
        else if (SEARCH_UNTIL) {
            if (UNITL_DATE == null) {
                return;
            }
            subLoadMoreTweets = twitterApi.searchTweetsWithDate(keywords, UNITL_DATE, lastTweetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchMoreTweetsSubscriber(firstVisibleItemPosition));
        }
        //Normal search
        else {
            subLoadMoreTweets = twitterApi.searchTweets(keywords, lastTweetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchMoreTweetsSubscriber(firstVisibleItemPosition));
        }
    }

    public void searchTweets(final String keyword) {
        Timber.d("attempting to search tweets with keyword %s", keyword);
        safelyUnsubscribe(subLoadMoreTweets, subSearchTweets);
        keywords = keyword;
        //No internet
        if (!netWorkApi.isConnectedToInternet()) {
            Timber.d("cannot search tweets - no internet connection");
            v.showErrorView();
            v.showSnackBar(v.getStringRes(R.string.no_internet_connection));
            return;
        }
        //Just in case the search work is a bunch of blanks
        if (!twitterApi.canSearchTweets(keywords)) {
            v.showErrorView();
            Timber.d("cannot search tweets - invalid keyword: %s", keywords);
            return;
        }
        //Search for tweets that have video
        if (SEARCH_VIDEO) {
            keywords = keywords + VIDEO_FILTER;
            Timber.d("Search for video, keyword is " + keywords);
            subSearchTweets = twitterApi.searchTweets(keywords)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchTweetsSubscriber(keywords));
            return;
        }
        //Search for near tweets
        else if (SEARCH_NEAR) {
            mGPSmanager.getLocation();
            if (mGPSmanager.canGetLocation()) {
                double latitude = mGPSmanager.getLatitude();
                double longitude = mGPSmanager.getLongitude();
                Timber.d("Can get location. Latitude is " + latitude + " longitude is " + longitude);
                subSearchTweets = twitterApi.searchTweetsWithLocation(keywords, latitude, longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getSearchTweetsSubscriber(keywords));
            } else {
                // can't get location.GPS or Network is not enabled.Ask user to enable GPS/network in settings
                if (!mGPSmanager.showSettingsAlert()) {
                    //If GPS or Network is enabled, but still can't get position.Then it must caused by permission deny
                    v.showSnackBar(v.getStringRes(R.string.permission_rationale_location));
                }
            }
        }
        //Search for tweets until a certain date
        else if (SEARCH_UNTIL) {
            if (UNITL_DATE == null) {
                return;
            }
            subLoadMoreTweets = twitterApi.searchTweetsWithDate(keywords, UNITL_DATE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchTweetsSubscriber(keywords));
        }
        //Normal search
        else {
            subSearchTweets = twitterApi.searchTweets(keywords)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchTweetsSubscriber(keywords));
        }
    }

    /**
     * Subscriber for load more tweets
     *
     * @param firstVisibleItemPosition
     * @return
     */
    private Subscriber<List<Status>> getSearchMoreTweetsSubscriber(final int firstVisibleItemPosition) {
        return new Subscriber<List<Status>>() {
            @Override
            public void onStart() {
                //Show loading view
                v.setPbLoadMoreTweetsVisible();
                Timber.d("loading more tweets");
            }

            @Override
            public void onCompleted() {
                v.setPbLoadMoreTweetsGone();
                Timber.d("more tweets loaded");
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                //No internet error
                if (!netWorkApi.isConnectedToInternet()) {
                    v.showSnackBar(v.getStringRes(R.string.no_internet_connection));
                    v.showErrorView();
                    Timber.d("no internet connection");
                } else {
                    v.showSnackBar(v.getStringRes(R.string.cannot_load_more_tweets));
                }
                v.setPbLoadMoreTweetsGone();
                Timber.d("couldn't load more tweets");
            }

            @Override
            public void onNext(List<Status> newTweets) {
                v.createNewTweetsAdapterAndRefresh(newTweets, firstVisibleItemPosition);
            }
        };
    }

    /**
     * Subscriber for new search keywords
     *
     * @param keyword
     * @return
     */
    private Subscriber<List<Status>> getSearchTweetsSubscriber(final String keyword) {
        return new Subscriber<List<Status>>() {

            @Override
            public void onStart() {
                //Show loading view
                Timber.d("searching tweets for keyword: %s", keyword);
                v.showLoadingView();
            }


            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(final Throwable e) {
                final String message = getErrorMessage((TwitterException) e);
                v.showSnackBar(message);
                v.showErrorView();
                Timber.d("error during search: %s", message);
            }

            @Override
            public void onNext(final List<Status> tweets) {
                Timber.d("search finished");
                handleSearchResults(tweets, keyword);
            }
        };
    }

    private void safelyUnsubscribe(final Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
                Timber.d("subscription %s unsubscribed", subscription.toString());
            }
        }
    }

    /**
     * Get error message from TwitterException
     *
     * @param e
     * @return
     */
    @NonNull
    private String getErrorMessage(final TwitterException e) {
        if (e.getErrorCode() == twitterApi.getApiRateLimitExceededErrorCode()) {
            return v.getStringRes(R.string.api_rate_limit_exceeded);
        }
        return v.getStringRes(R.string.error_during_search);
    }

    /**
     * Handle search result
     *
     * @param tweets
     * @param keyword
     */
    private void handleSearchResults(final List<Status> tweets, final String keyword) {
        Timber.d("handling search results");
        if (tweets.isEmpty()) {
            Timber.d("no tweets");
            final String message = String.format(v.getStringRes(R.string.no_tweets_formatted), handleKeyWordsInDifferentCategory(keyword));
            v.showSnackBar(message);
            v.showEmptyView();
            return;
        }

        Timber.d("passing search results to view");
        v.showSearchResult(tweets, handleKeyWordsInDifferentCategory(keyword));
    }

    /**
     * Change the keywords shown in snackbar in different situation
     *
     * @param keyword
     * @return
     */
    private String handleKeyWordsInDifferentCategory(String keyword) {
        if (SEARCH_VIDEO) {
            int index = keyword.indexOf(VIDEO_FILTER);
            return new String(keyword.substring(0, index) + " with video");
        } else if (SEARCH_NEAR) {
            return new String(keyword + " tweeted near you");
        } else if (SEARCH_UNTIL) {
            return new String(keyword + " until " + UNITL_DATE);
        } else {
            return keyword;
        }
    }

    @Override
    public void unsubscribeAll() {
        safelyUnsubscribe(subLoadMoreTweets, subSearchTweets);
    }
}

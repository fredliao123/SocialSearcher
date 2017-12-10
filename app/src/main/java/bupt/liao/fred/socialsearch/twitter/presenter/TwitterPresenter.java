package bupt.liao.fred.socialsearch.twitter.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.app.BaseApplication;
import bupt.liao.fred.socialsearch.mvp.presenter.BasePresenter;
import bupt.liao.fred.socialsearch.twitter.di.DaggerTwitterComponent;
import bupt.liao.fred.socialsearch.twitter.di.TwitterComponent;
import bupt.liao.fred.socialsearch.twitter.di.TwitterModule;
import bupt.liao.fred.socialsearch.twitter.model.GPSmanager;
import bupt.liao.fred.socialsearch.twitter.model.ITwitterApi;
import bupt.liao.fred.socialsearch.twitter.view.TwitterAdapter;
import bupt.liao.fred.socialsearch.twitter.view.TwitterFragment;
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

public class TwitterPresenter extends BasePresenter<TwitterFragment> {
    TwitterComponent component;

    //Search content
    private String keywords = "";
    private Context context;

    private Subscription subSearchTweets;
    private Subscription subLoadMoreTweets;

    @Inject
    protected ITwitterApi twitterApi;

    @Inject
    protected GPSmanager mGPSmanager;

    //flag for video search
    public volatile static boolean SEARCH_VIDEO = false;
    //flag for time search
    public volatile static boolean SEARCH_UNTIL = false;
    //flag for near search
    public volatile static boolean SEARCH_NEAR = false;
    //Date for time search
    public volatile static String UNITL_DATE = null;
    //Add string for video search
    private static final String VIDEO_FILTER = " filter:vine";

    /**
     * Method to set up search for video
     */
    public static void searchForVideo() {
        SEARCH_VIDEO = true;
        SEARCH_UNTIL = false;
        SEARCH_NEAR = false;
        UNITL_DATE = null;
    }

    /**
     * Method to set up search for time
     */
    public static void searchForUnitl(String unitl_date) {
        SEARCH_VIDEO = false;
        SEARCH_UNTIL = true;
        SEARCH_NEAR = false;
        UNITL_DATE = unitl_date;
    }

    /**
     * Method to set up search for near tweets
     */
    public static void searchForNear() {
        SEARCH_VIDEO = false;
        SEARCH_UNTIL = false;
        SEARCH_NEAR = true;
        UNITL_DATE = null;
    }

    /**
     * Method to clear category search flag
     */
    public static void clearSearchHint() {
        SEARCH_VIDEO = false;
        SEARCH_UNTIL = false;
        SEARCH_NEAR = false;
        UNITL_DATE = null;
    }


    public TwitterComponent getComponent() {
        if (component == null) {
            component = DaggerTwitterComponent.builder().twitterModule(new TwitterModule(context)).build();
        }
        return component;
    }

    public TwitterPresenter(Context context) {
        this.context = context;
        component = DaggerTwitterComponent.builder().twitterModule(new TwitterModule(context)).build();
        component.inject(this);
    }

    public static TwitterPresenter newInstance(Context context) {
        return new TwitterPresenter(context);
    }

    /**
     * Method to load more tweets after user scroll to end
     *
     * @param firstVisibleItemPosition
     */
    public void scrollToEnd(final int firstVisibleItemPosition) {
        if (subLoadMoreTweets != null && !subLoadMoreTweets.isUnsubscribed()) {
            return;
        }
        //No internet
        if (!BaseApplication.getComponent().getNetWorkApi().isConnectedToInternet(context)) {
            Timber.d("cannot search tweets - no internet connection");
            getV().getStateControllerLayout().showError();
            getV().showSnackBar(getV().msgNoInternetConnection);
            return;
        }

        //To decide which id should load from
        final long lastTweetId = ((TwitterAdapter) getV().getRecyclerView().getAdapter()).getLastTweetId();

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
                    getV().showSnackBar(getV().getResources().getString(R.string.permission_rationale_location));
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
        if (!BaseApplication.getComponent().getNetWorkApi().isConnectedToInternet(context)) {
            Timber.d("cannot search tweets - no internet connection");
            getV().getStateControllerLayout().showError();
            getV().showSnackBar(getV().msgNoInternetConnection);
            return;
        }
        //Just in case the search work is a bunch of blanks
        if (!twitterApi.canSearchTweets(keywords)) {
            getV().getStateControllerLayout().showError();
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
                    getV().showSnackBar(getV().getResources().getString(R.string.permission_rationale_location));
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
                getV().getPbLoadMoreTweets().setVisibility(View.VISIBLE);
                Timber.d("loading more tweets");
            }

            @Override
            public void onCompleted() {
                getV().getPbLoadMoreTweets().setVisibility(View.GONE);
                Timber.d("more tweets loaded");
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                //No internet error
                if (!BaseApplication.getComponent().getNetWorkApi().isConnectedToInternet(context)) {
                    getV().showSnackBar(getV().msgNoInternetConnection);
                    getV().getStateControllerLayout().showError();
                    Timber.d("no internet connection");
                } else {
                    getV().showSnackBar(getV().msgCannotLoadMoreTweets);
                }
                getV().getPbLoadMoreTweets().setVisibility(View.GONE);
                Timber.d("couldn't load more tweets");
            }

            @Override
            public void onNext(List<Status> newTweets) {
                getV().createNewTweetsAdapterAndRefresh(newTweets, firstVisibleItemPosition);
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
                getV().getStateControllerLayout().showLoading();
            }


            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(final Throwable e) {
                final String message = getErrorMessage((TwitterException) e);
                getV().showSnackBar(message);
                getV().getStateControllerLayout().showError();
                Timber.d("error during search: %s", message);
            }

            @Override
            public void onNext(final List<Status> tweets) {
                Timber.d("search finished");
                handleSearchResults(tweets, keyword);
            }
        };
    }

    public void safelyUnsubscribeAll() {
        safelyUnsubscribe(subLoadMoreTweets, subSearchTweets);
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
            return getV().msgApiRateLimitExceeded;
        }
        return getV().msgErrorDuringSearch;
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
            final String message = String.format(getV().msgNoTweetsFormatted, handleKeyWordsInDifferentCategory(keyword));
            getV().showSnackBar(message);
            getV().getStateControllerLayout().showEmpty();
            return;
        }

        Timber.d("passing search results to view");
        getV().showSearchResult(tweets, handleKeyWordsInDifferentCategory(keyword));
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
}

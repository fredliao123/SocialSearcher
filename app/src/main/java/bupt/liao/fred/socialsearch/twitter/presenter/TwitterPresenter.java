package bupt.liao.fred.socialsearch.twitter.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

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
 * Description:
 */

public class TwitterPresenter extends BasePresenter<TwitterFragment> {
    TwitterComponent component;

    private String keywords = "";
    private Context context;

    private Subscription subSearchTweets;
    private Subscription subLoadMoreTweets;

    @Inject
    protected ITwitterApi twitterApi;

    @Inject
    protected GPSmanager mGPSmanager;

    public volatile static boolean SEARCH_VIDEO = false;
    public volatile static boolean SEARCH_UNTIL = false;
    public volatile static boolean SEARCH_NEAR = false;
    public volatile static String UNITL_DATE = null;

    public static void searchForVideo() {
        SEARCH_VIDEO = true;
        SEARCH_UNTIL = false;
        SEARCH_NEAR = false;
        UNITL_DATE = null;
    }

    public static void searchForUnitl(String unitl_date) {
        SEARCH_VIDEO = false;
        SEARCH_UNTIL = true;
        SEARCH_NEAR = false;
        UNITL_DATE = unitl_date;
    }

    public static void searchForNear() {
        SEARCH_VIDEO = false;
        SEARCH_UNTIL = false;
        SEARCH_NEAR = true;
        UNITL_DATE = null;
    }

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

    public void scrollToEnd(final int firstVisibleItemPosition) {
        if (subLoadMoreTweets != null && !subLoadMoreTweets.isUnsubscribed()) {
            return;
        }

        if (!BaseApplication.getComponent().getNetWorkApi().isConnectedToInternet(context)) {
            Timber.d("cannot search tweets - no internet connection");
            getV().getStateControllerLayout().showError();
            getV().showSnackBar(getV().msgNoInternetConnection);
            return;
        }

        final long lastTweetId = ((TwitterAdapter) getV().getRecyclerView().getAdapter()).getLastTweetId();

        if (SEARCH_VIDEO) {
            Timber.d("Load more for video, keyword is " + keywords);
            subLoadMoreTweets = twitterApi.searchTweets(keywords, lastTweetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchMoreTweetsSubscriber(firstVisibleItemPosition));
            return;
        } else if (SEARCH_NEAR) {
            mGPSmanager.getLocation();
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
                mGPSmanager.showSettingsAlert();
            }
        } else if (SEARCH_UNTIL) {
            if (UNITL_DATE == null) {
                return;
            }
            subLoadMoreTweets = twitterApi.searchTweetsWithDate(keywords, UNITL_DATE, lastTweetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchMoreTweetsSubscriber(firstVisibleItemPosition));
        } else {
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
        if (!BaseApplication.getComponent().getNetWorkApi().isConnectedToInternet(context)) {
            Timber.d("cannot search tweets - no internet connection");
            getV().getStateControllerLayout().showError();
            getV().showSnackBar(getV().msgNoInternetConnection);
            return;
        }
        if (!twitterApi.canSearchTweets(keywords)) {
            getV().getStateControllerLayout().showError();
            Timber.d("cannot search tweets - invalid keyword: %s", keywords);
            return;
        }

        if (SEARCH_VIDEO) {
            keywords = keywords + " filter:vine";
            Timber.d("Search for video, keyword is " + keywords);
            subSearchTweets = twitterApi.searchTweets(keywords)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchTweetsSubscriber(keywords));
            return;
        } else if (SEARCH_NEAR) {
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
                mGPSmanager.showSettingsAlert();
            }
        } else if (SEARCH_UNTIL) {
            if (UNITL_DATE == null) {
                return;
            }
            subLoadMoreTweets = twitterApi.searchTweetsWithDate(keywords, UNITL_DATE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchTweetsSubscriber(keywords));
        } else {
            subSearchTweets = twitterApi.searchTweets(keywords)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSearchTweetsSubscriber(keywords));
        }
    }

    private Subscriber<List<Status>> getSearchMoreTweetsSubscriber(final int firstVisibleItemPosition) {
        return new Subscriber<List<Status>>() {
            @Override
            public void onStart() {
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

    private Subscriber<List<Status>> getSearchTweetsSubscriber(final String keyword) {
        return new Subscriber<List<Status>>() {

            @Override
            public void onStart() {
                Timber.d("searching tweets for keyword: %s", keyword);
                getV().getStateControllerLayout().showLoading();
            }


            @Override
            public void onCompleted() {
                // we don't have to implement this method

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
            getV().getStateControllerLayout().showEmpty();
            return;
        }

        Timber.d("passing search results to view");
        getV().showSearchResult(tweets, keyword);
    }
}

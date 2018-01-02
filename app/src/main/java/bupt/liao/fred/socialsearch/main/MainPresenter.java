package bupt.liao.fred.socialsearch.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.app.data.SharedPrefsHelper;
import bupt.liao.fred.socialsearch.app.rxbus.CategoryEvent;
import bupt.liao.fred.socialsearch.app.rxbus.RxBus;
import bupt.liao.fred.socialsearch.main.permission.LocationPermissionManager;
import bupt.liao.fred.socialsearch.ui.common.BasePresenter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/9.
 * Email:fredliaobupt@qq.com
 * Description: Presenter that handles sharedPreference and permission for ACCESS_FINE_LOCATION
 * for MainActivity
 */

public class MainPresenter extends BasePresenter<MainContract.MainView> implements MainContract.MainPresenter {

    @Inject
    LocationPermissionManager locationPermissionManager;

    private SharedPrefsHelper sharedPrefsHelper;

    private RxBus rxBus;
    /**
     * For search history
     * Key for get saved search history from sharedpreference
     */
    static final String HISTORY_KEY = "history";

    Set<String> historySuggestions = new HashSet<>();

    private Subscription subGetHistorySet;

    @Inject
    public MainPresenter(MainContract.MainView view,
                         SharedPrefsHelper sharedPrefsHelper,
                         RxBus rxbus) {
        super(view);
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.rxBus = rxbus;
    }


    //Asynchronously read search history from sharedPreference
    @Override
    public void getHistorySet() {
        safelyUnsubscribe(subGetHistorySet);
        subGetHistorySet = sharedPrefsHelper
                .getStringSet(HISTORY_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Set<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Set<String> set) {
                        historySuggestions = set;
                        String[] suggestions = set.toArray(new String[set.size()]);
                        v.setSuggestions(suggestions);
                        v.showSuggestions();
                    }
                });
    }

    @Override
    public void saveHistorySet() {
        sharedPrefsHelper.put(HISTORY_KEY, historySuggestions);
    }

    /**
     * Unsubscribe subsciptions
     *
     * @param subscriptions
     */
    private void safelyUnsubscribe(final Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
                Timber.d("subscription %s unsubscribed", subscription.toString());
            }
        }
    }

    /**
     * Enables the location permission if the fine location permission has been granted.
     */
    public void enableLocationPermission(AppCompatActivity appCompatActivity) {
        if (ContextCompat.checkSelfPermission(appCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            locationPermissionManager.requestPermission();
        } else {
            // Access to the location has been granted to the app.
            Timber.d("Permission has been granted");
        }
    }

    @Override
    public boolean isPermissionGranted(String[] permissions, int[] grantResult, String permission) {
        return locationPermissionManager.isPermissionGranted(permissions, grantResult, permission);
    }

    @Override
    public void addQueryToHistorySet(String query) {
        historySuggestions.add(query);
        String[] suggestions = historySuggestions.toArray(new String[historySuggestions.size()]);
        v.setSuggestions(suggestions);
    }

    @Override
    public void clearSearchHint() {
        if(rxBus.hasObservers()) {
            rxBus.send(new CategoryEvent(CategoryEvent.TAG_CLEAR));
        }
    }

    @Override
    public void searchForVideo() {
        if(rxBus.hasObservers()) {
            rxBus.send(new CategoryEvent(CategoryEvent.TAG_VIDEO));
        }
    }

    @Override
    public void searchForNear() {
        if(rxBus.hasObservers()) {
            rxBus.send(new CategoryEvent(CategoryEvent.TAG_NEAR));
        }
    }

    @Override
    public void searchForUntil(String date) {
        if (rxBus.hasObservers()) {
            CategoryEvent event = new CategoryEvent(CategoryEvent.TAG_UNTIL);
            event.setDate(date);
            rxBus.send(event);

        }
    }

    @Override
    public void unsubscribeAll() {
        safelyUnsubscribe(subGetHistorySet);
    }
}

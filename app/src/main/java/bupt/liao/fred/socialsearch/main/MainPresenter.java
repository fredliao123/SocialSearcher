package bupt.liao.fred.socialsearch.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.app.BaseApplication;
import bupt.liao.fred.socialsearch.kit.PermissionKit;
import bupt.liao.fred.socialsearch.mvp.presenter.BasePresenter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/9.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class MainPresenter extends BasePresenter<MainActivity> {

    private Context context;

    /**
     * For search history
     * Key for get saved search history from sharedpreference
     */
    public static String HISTORY_KEY = "history";
    Subscription subGetHistorySet;
    Subscription subSaveHistorySet;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    //Flag indicating whether a requested permission has been denied after returning in
    private boolean mPermissionDenied = false;

    private MainPresenter(Context context) {
        this.context = context;
    }

    @Singleton
    public static MainPresenter newInstance(Context context) {
        return new MainPresenter(context);
    }

    //Asynchronously read search history from sharedPreference
    public void initHistorySet() {
        safelyUnsubscribe(subGetHistorySet, subSaveHistorySet);
        subGetHistorySet = BaseApplication
                .getComponent()
                .getSharedPrefsHelper()
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
                        getV().setHistorySuggestions(set);
                        String[] suggestions = getV().getHistorySuggestions().toArray(new String[getV().getHistorySuggestions().size()]);
                        getV().searchView.setSuggestions(suggestions);
                        getV().searchView.showSuggestions();
                    }
                });
    }

    private void safelyUnsubscribe(final Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
                Timber.d("subscription %s unsubscribed", subscription.toString());
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionKit.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

        } else {
            mPermissionDenied = true;
        }
    }

    /**
     * Enables the location permission if the fine location permission has been granted.
     */
    public void enableLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionKit.requestPermission(getV(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        } else {
            PermissionKit.requestPermission(getV(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
            // Access to the location has been granted to the app.
            Timber.d("Permission has been granted");
        }
    }


    public boolean ismPermissionDenied() {
        return mPermissionDenied;
    }

    public void setmPermissionDenied(boolean mPermissionDenied) {
        this.mPermissionDenied = mPermissionDenied;
    }
}

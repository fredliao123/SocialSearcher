package bupt.liao.fred.socialsearch.app.data;

import android.content.SharedPreferences;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;


/**
 * Created by Fred.Liao on 2017/12/7.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Singleton
public class SharedPrefsHelper {

    public static String PREF_KEY_ACCESS_TOKEN = "access-token";

    private SharedPreferences mSharedPreferences;

    @Inject
    public SharedPrefsHelper(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public void put(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void put(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void put(String key, Set<String>stringSet){
        mSharedPreferences.edit().putStringSet(key, stringSet).apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public Integer get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public Float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public Boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public Set<String> get(String key){
        return mSharedPreferences.getStringSet(key, null);
    }

    public void deleteSavedData(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public Observable<Set<String>> getStringSet(final String key){
        return Observable.create(new Observable.OnSubscribe<Set<String>>(){
            @Override
            public void call(Subscriber<? super Set<String>> subscriber) {
                Set<String> set = get(key);
                subscriber.onNext(set);
            }
        });
    }

    public Observable putStringSet(final String key, final Set<String> set){
        return Observable.create(new Observable.OnSubscribe(){
            @Override
            public void call(Object o) {
                put(key, set);
            }
        });
    }
}
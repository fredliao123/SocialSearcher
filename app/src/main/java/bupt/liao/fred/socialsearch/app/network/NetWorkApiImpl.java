package bupt.liao.fred.socialsearch.app.network;

import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.ConnectivityStatus;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:The implementation of the INetWorkApi
 */

public final class NetWorkApiImpl implements INetWorkApi {

    private ReactiveNetwork reactiveNetwork;

    @Inject
    public NetWorkApiImpl(ReactiveNetwork reactiveNetwork) {
        this.reactiveNetwork = reactiveNetwork;
    }

    /**
     * Check internet connection
     *
     * @param context
     * @return if the internet is connected
     */
    @Override
    public boolean isConnectedToInternet(Context context) {
        try {
            final ConnectivityStatus status = reactiveNetwork.getConnectivityStatus(context, true);
            final boolean connectedToWifi = status == ConnectivityStatus.WIFI_CONNECTED_HAS_INTERNET;
            final boolean connectedToMobile = status == ConnectivityStatus.MOBILE_CONNECTED;
            return connectedToWifi || connectedToMobile;
        }catch (NullPointerException e){
            Timber.d("A null pointer exception happened in isConnectedToInternet " + e);
        }
        return false;
    }

    /**
     * Observe the connection of internet. inform observer when internet status changed
     *
     * @param context
     * @return
     */
    @Override
    public Observable<ConnectivityStatus> observeConnectivity(Context context) {
        return new ReactiveNetwork().enableInternetCheck().observeConnectivity(context);
    }
}


package bupt.liao.fred.socialsearch.app.network;

import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.ConnectivityStatus;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.app.network.INetWorkApi;
import rx.Observable;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public final class NetWorkApiImpl implements INetWorkApi {

    private ReactiveNetwork reactiveNetwork;

    @Inject
    public NetWorkApiImpl(ReactiveNetwork reactiveNetwork) {
        this.reactiveNetwork = reactiveNetwork;
    }

    @Override
    public boolean isConnectedToInternet(Context context) {
        final ConnectivityStatus status = reactiveNetwork.getConnectivityStatus(context, true);
        final boolean connectedToWifi = status == ConnectivityStatus.WIFI_CONNECTED_HAS_INTERNET;
        final boolean connectedToMobile = status == ConnectivityStatus.MOBILE_CONNECTED;
        return connectedToWifi || connectedToMobile;
    }

    @Override
    public Observable<ConnectivityStatus> observeConnectivity(Context context) {
        return new ReactiveNetwork().enableInternetCheck().observeConnectivity(context);
    }
}


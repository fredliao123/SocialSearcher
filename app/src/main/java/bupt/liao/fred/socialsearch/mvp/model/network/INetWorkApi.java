package bupt.liao.fred.socialsearch.mvp.model.network;

import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.ConnectivityStatus;

import rx.Observable;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public interface INetWorkApi {
    boolean isConnectedToInternet(Context context);

    Observable<ConnectivityStatus> observeConnectivity(final Context context);
}

package bupt.liao.fred.socialsearch.app.network;

import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2018/1/2.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Module
public class NetWorkModule {
    @Provides
    @Singleton
    INetWorkApi provideNetWorkApi(Context context) {
        return new NetWorkApiImpl(new ReactiveNetwork(), context);
    }
}

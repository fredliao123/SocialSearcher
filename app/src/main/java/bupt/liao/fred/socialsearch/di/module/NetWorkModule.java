package bupt.liao.fred.socialsearch.di.module;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.mvp.model.network.INetWorkApi;
import bupt.liao.fred.socialsearch.mvp.model.network.NetWorkApiImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Module
public final class NetWorkModule {
    @Provides
    @Singleton
    public INetWorkApi provideNetworkApi() {
        return new NetWorkApiImpl();
    }
}
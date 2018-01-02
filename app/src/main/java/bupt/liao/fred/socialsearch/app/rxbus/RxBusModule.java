package bupt.liao.fred.socialsearch.app.rxbus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fred.Liao on 2018/1/2.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Module
public class RxBusModule {
    @Provides
    @Singleton
    RxBus provideRxBus(){
        return new RxBus();
    }
}

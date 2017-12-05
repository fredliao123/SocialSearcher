package bupt.liao.fred.socialsearch.di;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.di.module.NetWorkModule;
import bupt.liao.fred.socialsearch.di.module.TwitterModule;
import bupt.liao.fred.socialsearch.mvp.view.ui.MainActivity;
import dagger.Component;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Singleton
@Component(modules = { TwitterModule.class, NetWorkModule.class })
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}

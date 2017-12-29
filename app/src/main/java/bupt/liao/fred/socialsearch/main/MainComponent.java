package bupt.liao.fred.socialsearch.main;

import bupt.liao.fred.socialsearch.app.AppComponent;
import bupt.liao.fred.socialsearch.app.scope.PerAcitivity;
import dagger.Component;

/**
 * Created by Fred.Liao on 2017/12/29.
 * Email:fredliaobupt@qq.com
 * Description:
 */
@PerAcitivity
@Component(dependencies = AppComponent.class, modules = MainModule.class)
interface MainComponent {
    void inject(MainActivity mainActivity);
}

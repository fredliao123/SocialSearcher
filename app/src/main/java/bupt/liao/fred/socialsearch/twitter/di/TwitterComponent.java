package bupt.liao.fred.socialsearch.twitter.di;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.app.AppComponent;
import bupt.liao.fred.socialsearch.app.gps.GPSmanager;
import bupt.liao.fred.socialsearch.app.scope.PerFragment;
import bupt.liao.fred.socialsearch.twitter.model.ITwitterApi;
import bupt.liao.fred.socialsearch.twitter.presenter.TwitterPresenter;
import bupt.liao.fred.socialsearch.twitter.view.TwitterFragment;
import dagger.Component;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description:Component for DI.
 */
@PerFragment
@Component(dependencies = AppComponent.class, modules = {TwitterModule.class, TwitterViewModule.class})
public interface TwitterComponent {
    void inject(TwitterFragment fragment);
}

package bupt.liao.fred.socialsearch.twitter.di;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.twitter.model.ITwitterApi;
import bupt.liao.fred.socialsearch.twitter.presenter.TwitterPresenter;
import dagger.Component;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description:
 */

@Singleton
@Component(modules = {TwitterModule.class})
public interface TwitterComponent {
    void inject(TwitterPresenter presenter);

    ITwitterApi getTwitterApi();
}

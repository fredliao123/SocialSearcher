package bupt.liao.fred.socialsearch.mvp.presenter;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description: Presenter interface using MVP model
 */

public interface IPresenter<V> {
    void attachV(V view);

    void detachV();
}

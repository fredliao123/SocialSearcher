package bupt.liao.fred.socialsearch.ui.common;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description: BasePresenter using MVP model
 */

public abstract class BasePresenter <V> {
    protected final V v;

    protected BasePresenter(V view){
        this.v = view;
    }

    public abstract void unsubscribeAll();
}


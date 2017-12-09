package bupt.liao.fred.socialsearch.mvp.view;

import android.os.Bundle;
import android.view.View;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description: Interface for views using MVP model
 */

public interface IViewBase<P> extends IView<P>{
    void bindUI(View rootView);

    void bindEvent();

    void initData(Bundle savedInstanceState);

    int getOptionsMenuId();

    int getLayoutId();

    boolean useEventBus();
}

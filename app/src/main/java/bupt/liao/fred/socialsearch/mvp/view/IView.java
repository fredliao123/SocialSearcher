package bupt.liao.fred.socialsearch.mvp.view;

import android.os.Bundle;
import android.view.View;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public interface IView<P> {
    void bindUI(View rootView);

    void bindEvent();

    void initData(Bundle savedInstanceState);

    int getOptionsMenuId();

    int getLayoutId();

    boolean useEventBus();

    P newP();
}

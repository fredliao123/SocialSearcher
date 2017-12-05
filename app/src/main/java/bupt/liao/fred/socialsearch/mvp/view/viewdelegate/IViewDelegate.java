package bupt.liao.fred.socialsearch.mvp.view.viewdelegate;

import android.view.View;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public interface IViewDelegate {
    void resume();

    void pause();

    void destory();

    void visible(boolean flag, View view);
    void gone(boolean flag, View view);
    void inVisible(View view);

    void toastShort(String msg);
    void toastLong(String msg);
}

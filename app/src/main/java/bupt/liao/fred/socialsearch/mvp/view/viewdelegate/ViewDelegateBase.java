package bupt.liao.fred.socialsearch.mvp.view.viewdelegate;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class ViewDelegateBase implements IViewDelegate {

    private Context context;

    private ViewDelegateBase(Context context) {
        this.context = context;
    }

    public static IViewDelegate create(Context context) {
        return new ViewDelegateBase(context);
    }


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destory() {

    }

    @Override
    public void visible(boolean flag, View view) {
        if (flag) view.setVisibility(View.VISIBLE);
    }

    @Override
    public void gone(boolean flag, View view) {
        if (flag) view.setVisibility(View.GONE);
    }

    @Override
    public void inVisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toastShort(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastLong(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}


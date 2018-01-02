package bupt.liao.fred.socialsearch.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.kit.KnifeKit;
import butterknife.Unbinder;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:BaseActivity using MVP model
 */

public abstract class BaseActivity extends RxAppCompatActivity{

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            bindUI(null);
        }
        initData(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    public void bindUI(View rootView) {
        unbinder = KnifeKit.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KnifeKit.unbind(unbinder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getOptionsMenuId() > 0) {
            getMenuInflater().inflate(getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    protected int getOptionsMenuId() {
        return 0;
    }

    public void showSnackBar(final String message) {
        final View containerId = this.findViewById(R.id.container);
        Snackbar.make(containerId, message, Snackbar.LENGTH_LONG).show();
    }

}
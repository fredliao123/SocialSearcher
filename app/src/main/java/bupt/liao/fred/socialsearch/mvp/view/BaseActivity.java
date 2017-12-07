package bupt.liao.fred.socialsearch.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import bupt.liao.fred.socialsearch.app.Conf;
import bupt.liao.fred.socialsearch.mvp.view.viewdelegate.IViewDelegate;
import bupt.liao.fred.socialsearch.mvp.view.viewdelegate.ViewDelegateBase;
import bupt.liao.fred.socialsearch.event.BusProvider;
import bupt.liao.fred.socialsearch.kit.KnifeKit;
import bupt.liao.fred.socialsearch.mvp.presenter.IPresenter;
import butterknife.Unbinder;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public abstract class BaseActivity <P extends IPresenter> extends RxAppCompatActivity implements IViewBase<P> {

    private IViewDelegate vDelegate;
    private P p;
    protected Activity context;

    private RxPermissions rxPermissions;

    private Unbinder unbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            bindUI(null);
            bindEvent();
        }
        initData(savedInstanceState);

    }

    @Override
    public void bindUI(View rootView) {
        unbinder = KnifeKit.bind(this);
    }

    protected IViewDelegate getvDelegate() {
        if (vDelegate == null) {
            vDelegate = ViewDelegateBase.create(context);
        }
        return vDelegate;
    }

    protected P getP() {
        if (p == null) {
            p = newP();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusProvider.getBus().register(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getvDelegate().resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        getvDelegate().pause();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            BusProvider.getBus().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }
        getvDelegate().destory();
        p = null;
        vDelegate = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getOptionsMenuId() > 0) {
            getMenuInflater().inflate(getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    protected RxPermissions getRxPermissions() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(Conf.DEV);
        return rxPermissions;
    }

    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    @Override
    public void bindEvent() {

    }

    protected void safelyUnsubscribe(final Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
                Timber.d("subscription %s unsubscribed", subscription.toString());
            }
        }
    }
}
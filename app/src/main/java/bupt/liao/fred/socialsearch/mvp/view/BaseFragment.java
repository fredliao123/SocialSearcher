package bupt.liao.fred.socialsearch.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.event.BusProvider;
import bupt.liao.fred.socialsearch.kit.KnifeKit;
import bupt.liao.fred.socialsearch.mvp.presenter.IPresenter;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:BaseFragment using MVP model
 */

public abstract class BaseFragment <P extends IPresenter> extends RxFragment implements IViewBase<P> {

    private P p;
    protected Activity context;
    private View rootView;
    protected LayoutInflater layoutInflater;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        if (rootView == null && getLayoutId() > 0) {
            rootView = inflater.inflate(getLayoutId(), null);
            bindUI(rootView);
        } else {
            if(rootView != null) {
                ViewGroup viewGroup = (ViewGroup) rootView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(rootView);
                }
            }else{
                Timber.d("BaseFragment: rootView is null, no layout id is sent in");
            }
        }

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (useEventBus()) {
            BusProvider.getBus().register(this);
        }
        bindEvent();
        initData(savedInstanceState);
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = KnifeKit.bind(this, rootView);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (useEventBus()) {
            BusProvider.getBus().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }
        p = null;
    }

    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    @Override
    public void bindEvent() {

    }

    public void showSnackBar(final String message) {
        final View containerId = getActivity().findViewById(R.id.container);
        Snackbar.make(containerId, message, Snackbar.LENGTH_LONG).show();
    }
}

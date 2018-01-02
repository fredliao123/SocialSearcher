package bupt.liao.fred.socialsearch.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.kit.KnifeKit;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:BaseFragment using MVP model
 */

public abstract class BaseFragment extends RxFragment {


    protected Context context;
    protected View rootView;
    protected LayoutInflater layoutInflater;
    protected Unbinder unbinder;

    protected abstract int getLayoutId();

    protected abstract void initData(@Nullable Bundle savedInstanceState);

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
        bindEvent();
        initData(savedInstanceState);
    }

    public void bindUI(View rootView) {
        unbinder = KnifeKit.bind(this, rootView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KnifeKit.unbind(unbinder);
    }

    protected void bindEvent() {}

    protected void showSnackBar(final String message) {
        final View containerId = getActivity().findViewById(R.id.container);
        Snackbar.make(containerId, message, Snackbar.LENGTH_LONG).show();
    }
}

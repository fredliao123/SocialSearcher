package bupt.liao.fred.socialsearch.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

import bupt.liao.fred.socialsearch.mvp.view.IView;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public abstract class BasePresenter <V extends IView> implements IPresenter<V> {
    private WeakReference<V> v;

    @Override
    public void attachV(V view) {
        v = new WeakReference<V>(view);
    }

    @Override
    public void detachV() {
        if (v.get() != null) {
            v.clear();
        }
        v = null;
    }


    protected V getV() {
        if (v == null || v.get() == null) {
            throw new IllegalStateException("v can not be null");
        }
        return v.get();
    }
}


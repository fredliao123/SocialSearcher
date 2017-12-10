package bupt.liao.fred.socialsearch.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

import bupt.liao.fred.socialsearch.mvp.view.IView;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description: BasePresenter using MVP model
 */

public abstract class BasePresenter <V extends IView> implements IPresenter<V> {
    private V v;

    @Override
    public void attachV(V view) {
        this.v = view;
    }

    @Override
    public void detachV() {
        v = null;
    }


    protected V getV() {
        if (v == null || v == null) {
            throw new IllegalStateException("v can not be null");
        }
        return v;
    }
}


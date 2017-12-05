package bupt.liao.fred.socialsearch.mvp.view.ui;

import android.os.Bundle;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.view.BaseFragment;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class FlickrFragment extends BaseFragment{
    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_flickr;
    }

    public static FlickrFragment newInstance(){
        return new FlickrFragment();
    }
}

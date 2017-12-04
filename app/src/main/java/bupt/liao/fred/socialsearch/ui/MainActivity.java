package bupt.liao.fred.socialsearch.ui;

import android.os.Bundle;
import android.widget.TextView;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.base.BaseActivity;
import butterknife.BindView;

public class MainActivity extends BaseActivity{
    @BindView(R.id.test)
    TextView textView;

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Object newP() {
        return null;
    }
}

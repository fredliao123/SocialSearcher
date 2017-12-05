package bupt.liao.fred.socialsearch.mvp.view.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.view.BaseActivity;
import bupt.liao.fred.socialsearch.mvp.view.adapter.BaseFragmentPagerAdapter;
import butterknife.BindView;

public class MainActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"Twitter", "Flickr"};

    BaseFragmentPagerAdapter adapter;


    @Override
    public void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        fragmentList.clear();
        fragmentList.add(TwitterFragment.newInstance());
        fragmentList.add(FlickrFragment.newInstance());


        if (adapter == null) {
            adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getOptionsMenuId() {
        return R.menu.menu_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return result;
    }

    @Override
    public Object newP() {
        return null;
    }
}

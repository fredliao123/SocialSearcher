package bupt.liao.fred.socialsearch.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.app.BaseApplication;
import bupt.liao.fred.socialsearch.mvp.view.BaseActivity;
import bupt.liao.fred.socialsearch.mvp.view.adapter.BaseFragmentPagerAdapter;
import bupt.liao.fred.socialsearch.mvp.view.ui.FlickrFragment;
import bupt.liao.fred.socialsearch.twitter.view.TwitterFragment;
import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
    private static String HISTORY_KEY = "history";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.search_view)
    AdvancedSearchView searchView;
    @BindView(R.id.welcomecontainer)
    View welcomeContainer;
    @BindView(R.id.viewpager_container)
    RelativeLayout viewpagerContainer;


    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"Twitter", "Flickr"};

    BaseFragmentPagerAdapter adapter;

    Set<String> historySuggestions = null;

    Subscription subGetHistorySet;

    Subscription subSaveHistorySet;


    @Override
    public void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        initSearchView();
        initHistorySet();
        fragmentList.clear();
        fragmentList.add(TwitterFragment.newInstance());


        if (adapter == null) {
            adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setupWithViewPager(viewPager);
        showWelcomeView();
    }


    private void initHistorySet() {
        safelyUnsubscribe(subGetHistorySet, subSaveHistorySet);
        subGetHistorySet = BaseApplication
                .getComponent()
                .getSharedPrefsHelper()
                .getStringSet(HISTORY_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Set<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Set<String> set) {
                        historySuggestions = set;
                        if (historySuggestions == null) {
                            historySuggestions = new HashSet<>();
                        } else {
                            String[] suggestions = historySuggestions.toArray(new String[historySuggestions.size()]);
                            searchView.setSuggestions(suggestions);
                            searchView.showSuggestions();
                        }
                    }
                });
    }

    private void initSearchView() {
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.search_view_cursor);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Timber.d("pressed search icon");
                historySuggestions.add(query);
                searchView.setSuggestions(historySuggestions.toArray(new String[historySuggestions.size()]));
                if (fragmentList.get(0) instanceof TwitterFragment) {
                    ((TwitterFragment) fragmentList.get(0)).searchTweets(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                welcomeContainer.setVisibility(View.GONE);
                return false;
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        safelyUnsubscribe(subSaveHistorySet);
        BaseApplication
                .getComponent()
                .getSharedPrefsHelper()
                .putStringSet(HISTORY_KEY, historySuggestions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        safelyUnsubscribe(subGetHistorySet, subSaveHistorySet);
    }

    @Override
    public Object newP() {
        return null;
    }

    public void showWelcomeView() {
        welcomeContainer.setVisibility(View.VISIBLE);
        viewpagerContainer.setVisibility(View.VISIBLE);
    }

}

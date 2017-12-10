package bupt.liao.fred.socialsearch.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import bupt.liao.fred.socialsearch.twitter.view.TwitterFragment;
import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:
 * Using fragment and viewpager. Make it expandable for more social media.
 * But due to limited time. Only finished search for twitter
 * MainActivity is only responsible for the management of searchview and viewpager
 * The detailed manage of search result are dispatched to each fragment
 */


public class MainActivity extends BaseActivity<MainPresenter> implements ICategoryViewController {

    //flag to show whether the searchView has text;
    private boolean searchViewHasText = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //Because only one social media is added to search, so the tablayout's visibility is set to GONE
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.search_view)
    AdvancedSearchView searchView;
    //Show a welcome view when user first open the app
    @BindView(R.id.welcomecontainer)
    View welcomeContainer;
    @BindView(R.id.viewpager_container)
    RelativeLayout viewpagerContainer;

    //Expandable for more social medias
    List<Fragment> fragmentList = new ArrayList<>();
    //The title for each page, expandable
    String[] titles = {"Twitter"};
    BaseFragmentPagerAdapter adapter;
    /**
     * This set can only be in Activity,but not hold by presenter,because
     * when onDestroy be called ,the presenter has already be recycled. a wrong set will be saved.
     */
    Set<String> historySuggestions = new HashSet<>();

    @Override
    public void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        initSearchView();
        getP().initHistorySet();
        initViewPager();
        showWelcomeView();
    }


    /**
     * Init searchview
     */
    private void initSearchView() {
        //set the callback to control the search category select view from this activity
        searchView.setCategoryViewController(this);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.search_view_cursor);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            /**
             * This method will be called when a search text is entered and user pressed search button
             * @param query The search text user typed in
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Timber.d("pressed search icon");
                //Add query to search history and to be further saved in sharedpreference
                historySuggestions.add(query);
                searchView.setSuggestions(historySuggestions.toArray(new String[historySuggestions.size()]));
                //Due to only one page, so we can write this way. If added more page, alternation will be needed
                if (fragmentList.get(0) instanceof TwitterFragment) {
                    ((TwitterFragment) fragmentList.get(0)).searchTweets(query);
                }
                return false;
            }

            /**
             * This method well be called when the searchview is clicked and open
             * So once the searchview is open, the welcome view should be hidden
             * @param newText
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                welcomeContainer.setVisibility(View.GONE);
                if (!newText.equalsIgnoreCase("")) {
                    searchViewHasText = true;
                } else {
                    searchViewHasText = false;
                }
                changeCategoryViewShowStatus();
                return false;
            }
        });
    }

    /**
     * Init viewpager, although there is only one page, viewpager will make this app expandable
     * in the future
     */
    private void initViewPager() {
        fragmentList.clear();
        fragmentList.add(TwitterFragment.newInstance());
        if (adapter == null) {
            adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        //tablayout's visibility is GONE, because there is only one page.
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

    /**
     * Set up searchview in the menu position of toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return result;
    }

    /**
     * Save search history when activity is destroyed
     */
    @Override
    protected void onPause() {
        super.onPause();
        BaseApplication
                .getComponent()
                .getSharedPrefsHelper()
                .put(getP().HISTORY_KEY, historySuggestions);
    }


    @Override
    public MainPresenter newP() {
        return MainPresenter.newInstance(context);
    }

    /**
     * Show a welcome view when user first entered the app
     * Note that viewpagerContainer cannot be set as GONE
     * If set to GONE, the onMeasure in viewpager will not be called
     * So the fragment will not be attached to activity. This will eventually lead to
     * a null context in fragment.
     */
    public void showWelcomeView() {
        welcomeContainer.setVisibility(View.VISIBLE);
        viewpagerContainer.setVisibility(View.VISIBLE);
    }

    /**
     * To decide whether to show the search category select view is divided in four
     * situations as flows
     */
    @Override
    public void changeCategoryViewShowStatus() {
        //No input text, and no hint, show category
        if (!searchViewHasText && !searchView.isHintShown()) {
            searchView.showSearchForCategoryView();
        }
        //Has input text, and no hint, dismiss category
        else if (searchViewHasText && !searchView.isHintShown()) {
            searchView.dismissSearchForCategoryView();
        }
        //No input text, has hint, dismiss category
        else if (!searchViewHasText && searchView.isHintShown()) {
            searchView.dismissSearchForCategoryView();
        }
        //Has input text, has hint, dismiss category
        else if (searchViewHasText && searchView.isHintShown()) {
            searchView.dismissSearchForCategoryView();
        }
    }

    @Override
    public void isLocationPermissionGranted() {
        getP().enableLocationPermission();
    }

    public Set<String> getHistorySuggestions() {
        return historySuggestions;
    }

    public void setHistorySuggestions(Set<String> historySuggestions) {
        this.historySuggestions = historySuggestions;
    }
}

package bupt.liao.fred.socialsearch.twitter.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.app.App;
import bupt.liao.fred.socialsearch.app.Conf;
import bupt.liao.fred.socialsearch.twitter.di.DaggerTwitterComponent;
import bupt.liao.fred.socialsearch.twitter.di.TwitterModule;
import bupt.liao.fred.socialsearch.twitter.di.TwitterViewModule;
import bupt.liao.fred.socialsearch.ui.view.BaseFragment;
import bupt.liao.fred.socialsearch.ui.view.BaseStateControllerLayout;
import bupt.liao.fred.socialsearch.twitter.TwitterContract;
import bupt.liao.fred.socialsearch.twitter.presenter.TwitterPresenter;
import butterknife.BindView;
import dagger.android.support.AndroidSupportInjection;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description: Fragment for twitter
 */

public class TwitterFragment extends BaseFragment implements TwitterContract.TwitterView {

    @Inject
    TwitterPresenter twitterPresenter;

    @BindView(R.id.recycler_view_container)
    BaseStateControllerLayout stateControllerLayout;

    @BindView(R.id.pb_loading_more_tweets)
    ProgressBar pbLoadMoreTweets;

    @BindView(R.id.recycler_view_tweets)
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        DaggerTwitterComponent.builder().
                appComponent(((App)getActivity().getApplication()).getComponent()).
                twitterModule(new TwitterModule()).
                twitterViewModule(new TwitterViewModule(this)).
                build().
                inject(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initRecyclerView();
        initStateControlerLayout();
    }

    public void initStateControlerLayout() {
        stateControllerLayout.loadingView(View.inflate(context, R.layout.view_loading, null));
        stateControllerLayout.errorView(View.inflate(context, R.layout.view_error, null));
        stateControllerLayout.emptyView(View.inflate(context, R.layout.view_empty, null));
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new TwitterAdapter(getContext(), new LinkedList<Status>()));
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(createInfiniteScrollListener());
    }

    @NonNull
    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(Conf.MAX_TWEET_PER_REQUEST, layoutManager) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                twitterPresenter.scrollToEnd(firstVisibleItemPosition);
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_twitter;
    }

    public static TwitterFragment newInstance() {
        return new TwitterFragment();
    }

    @Override
    public void createNewTweetsAdapterAndRefresh(List<Status> newTweets, int position) {
        final TwitterAdapter adapter = (TwitterAdapter) recyclerView.getAdapter();
        final List<Status> oldTweets = adapter.getTweets();
        final List<Status> tweets = new LinkedList<>();
        tweets.addAll(oldTweets);
        tweets.addAll(newTweets);
        TwitterAdapter newAdapter = new TwitterAdapter(getContext(), tweets);
        recyclerView.setAdapter(newAdapter);
        recyclerView.invalidate();
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void showSearchResult(final List<Status> tweets, final String keyword) {
        final TwitterAdapter adapter = new TwitterAdapter(getContext(), tweets);
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        stateControllerLayout.showContent();
        final String message = String.format(getStringRes(R.string.searched_formatted), keyword);
        showSnackBar(message);
    }

    @Override
    public void showErrorView() {
        stateControllerLayout.showError();
    }

    @Override
    public void showLoadingView() {
        stateControllerLayout.showLoading();
    }

    @Override
    public void showEmptyView() {
        stateControllerLayout.showEmpty();
    }

    @Override
    public void showSnackBar(String msg){
        super.showSnackBar(msg);
    }

    @Override
    public long getLastTweetId() {
        TwitterAdapter adapter = (TwitterAdapter) recyclerView.getAdapter();
        if(adapter != null){
            return adapter.getLastTweetId();
        }
        return 0;
    }

    @Override
    public void setPbLoadMoreTweetsVisible() {
        pbLoadMoreTweets.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPbLoadMoreTweetsGone() {
        pbLoadMoreTweets.setVisibility(View.GONE);
    }

    @Override
    public String getStringRes(@StringRes int id){
        return context.getString(id);
    }


    public void searchTweets(String keywords) {
        twitterPresenter.searchTweets(keywords);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        twitterPresenter.unsubscribeAll();
    }
}

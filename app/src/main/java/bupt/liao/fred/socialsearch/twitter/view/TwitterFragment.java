package bupt.liao.fred.socialsearch.twitter.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.LinkedList;
import java.util.List;

import bupt.liao.fred.socialsearch.app.Conf;
import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.twitter.presenter.TwitterPresenter;
import bupt.liao.fred.socialsearch.mvp.view.BaseFragment;
import butterknife.BindString;
import butterknife.BindView;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class TwitterFragment extends BaseFragment<TwitterPresenter> {

    @BindString(R.string.no_internet_connection) public String msgNoInternetConnection;
    @BindString(R.string.cannot_load_more_tweets) public String msgCannotLoadMoreTweets;
    @BindString(R.string.no_tweets) public String msgNoTweets;
    @BindString(R.string.no_tweets_formatted) public String msgNoTweetsFormatted;
    @BindString(R.string.searched_formatted) public String msgSearchedFormatted;
    @BindString(R.string.api_rate_limit_exceeded) public String msgApiRateLimitExceeded;
    @BindString(R.string.error_during_search) public String msgErrorDuringSearch;

    @BindView(R.id.recycler_view_container)
    RelativeLayout rlRecyclerViewContainer;

    @BindView(R.id.pb_loading_more_tweets)
    ProgressBar pbLoadMoreTweets;

    @BindView(R.id.pb_loading_tweets)
    ProgressBar pbLoadingTweets;

    @BindView(R.id.recycler_view_tweets)
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;


    @Override
    public void initData(Bundle savedInstanceState) {
        initRecyclerView();

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
                getP().scrollToEnd(firstVisibleItemPosition);
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
    public TwitterPresenter newP() {
        return TwitterPresenter.newInstance(getContext());
    }


    public void showSnackBar(final String message) {
        final View containerId = getActivity().findViewById(R.id.container);
        Snackbar.make(containerId, message, Snackbar.LENGTH_LONG).show();
    }

    @NonNull
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

    public void showSearchResult(final List<Status> tweets, final String keyword){
        final TwitterAdapter adapter = new TwitterAdapter(getContext(), tweets);
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        recyclerView.setVisibility(View.VISIBLE);
        //TODO
        //messageContainerLayout.setVisibility(View.GONE);
        final String message = String.format(msgSearchedFormatted, keyword);
        showSnackBar(message);
    }

    public void showErrorMessageContainer(final String message, final int imageResourceId) {
        //TODO
    }

    public void searchTweets(String keywords){
        getP().searchTweets(keywords);
    }

    public void searchTweetsWithDelay(String keywords){
        getP().searchTweetsWithDelay(keywords);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        getP().safelyUnsubscribeAll();
    }

    public RelativeLayout getRlRecyclerViewContainer() {
        return rlRecyclerViewContainer;
    }

    public ProgressBar getPbLoadMoreTweets() {
        return pbLoadMoreTweets;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ProgressBar getPbLoadingTweets() {
        return pbLoadingTweets;
    }
}

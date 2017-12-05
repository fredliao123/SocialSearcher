package bupt.liao.fred.socialsearch.mvp.view.ui;

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

import bupt.liao.fred.socialsearch.Conf;
import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.presenter.TwitterPresenter;
import bupt.liao.fred.socialsearch.mvp.view.BaseFragment;
import bupt.liao.fred.socialsearch.mvp.view.adapter.TwitterAdapter;
import butterknife.BindView;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class TwitterFragment extends BaseFragment<TwitterPresenter> {

    @BindView(R.id.recycler_view_container)
    RelativeLayout rlRecyclerViewContainer;

    @BindView(R.id.pb_loading_more_tweets)
    ProgressBar pbLoadMoreTweets;

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
        return TwitterPresenter.newInstance();
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

    public RelativeLayout getRlRecyclerViewContainer() {
        return rlRecyclerViewContainer;
    }

    public ProgressBar getPbLoadMoreTweets() {
        return pbLoadMoreTweets;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    
}

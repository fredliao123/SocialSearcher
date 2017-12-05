package bupt.liao.fred.socialsearch.mvp.view.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.LinkedList;
import java.util.List;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.presenter.TwitterPresenter;
import bupt.liao.fred.socialsearch.mvp.view.BaseFragment;
import bupt.liao.fred.socialsearch.mvp.view.adapter.TwitterAdapter;
import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class TwitterFragment extends BaseFragment<TwitterPresenter>{

    @BindView(R.id.recycler_view_container)
    RelativeLayout rlRecyclerViewContainer;

    @BindView(R.id.pb_loading_more_tweets)
    ProgressBar pbLoadMoreTweets;

    @BindView(R.id.recycler_view_tweets)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    @Override
    public Object newP() {
        return new TwitterPresenter();
    }

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
        return new InfiniteScrollListener(twitterApi.getMaxTweetsPerRequest(), layoutManager) {
            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                if (subLoadMoreTweets != null && !subLoadMoreTweets.isUnsubscribed()) {
                    return;
                }

                final long lastTweetId = ((TweetsAdapter) recyclerViewTweets.getAdapter()).getLastTweetId();

                subLoadMoreTweets = twitterApi.searchTweets(lastKeyword, lastTweetId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Status>>() {
                            @Override public void onStart() {
                                progressLoadingMoreTweets.setVisibility(View.VISIBLE);
                                Timber.d("loading more tweets");
                            }

                            @Override public void onCompleted() {
                                progressLoadingMoreTweets.setVisibility(View.GONE);
                                Timber.d("more tweets loaded");
                                unsubscribe();
                            }

                            @Override public void onError(Throwable e) {
                                if (!networkApi.isConnectedToInternet(MainActivity.this)) {
                                    showSnackBar(msgNoInternetConnection);
                                    Timber.d("no internet connection");
                                } else {
                                    showSnackBar(msgCannotLoadMoreTweets);
                                }
                                progressLoadingMoreTweets.setVisibility(View.GONE);
                                Timber.d("couldn't load more tweets");
                            }

                            @Override public void onNext(List<Status> newTweets) {
                                final TweetsAdapter newAdapter = createNewTweetsAdapter(newTweets);
                                refreshView(recyclerViewTweets, newAdapter, firstVisibleItemPosition);
                            }
                        });
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_twitter;
    }

    public static TwitterFragment newInstance(){
        return new TwitterFragment();
    }
}

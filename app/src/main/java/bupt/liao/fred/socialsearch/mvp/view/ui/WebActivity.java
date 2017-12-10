package bupt.liao.fred.socialsearch.mvp.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.app.BaseApplication;
import bupt.liao.fred.socialsearch.mvp.view.BaseActivity;
import bupt.liao.fred.socialsearch.mvp.view.BaseStateControllerLayout;
import butterknife.BindView;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description:WebActivity for links in tweets
 */

public class WebActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.contentLayout)
    BaseStateControllerLayout contentLayout;

    String url;
    String desc;

    public static final String PARAM_URL = "url";
    public static final String PARAM_DESC = "desc";


    @Override
    public void initData(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(PARAM_URL);
        desc = getIntent().getStringExtra(PARAM_DESC);


        initToolbar();
        initContentLayout();
        initRefreshLayout();
        if(checkInternet()) {
            initWebView();
        }else{
            contentLayout.showError();
        }

    }

    private void initContentLayout() {
        contentLayout.loadingView(View.inflate(context, R.layout.view_loading, null));
        contentLayout.errorView(View.inflate(context,R.layout.view_error, null));
    }

    private boolean checkInternet(){
        return BaseApplication.getComponent().getNetWorkApi().isConnectedToInternet(context);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });

    }

    private void initWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (contentLayout != null)
                        contentLayout.showContent();
                    if (webView != null)
                        url = webView.getUrl();
                } else {
                    if (contentLayout != null)
                        contentLayout.showLoading();
                }
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);

        webView.loadUrl(url);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
        }
    }

    public static void launch(Context context, String url, String desc) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_DESC, desc);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public Object newP() {
        return null;
    }
}

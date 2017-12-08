package bupt.liao.fred.socialsearch.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.kit.drawingKit;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/7.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class AdvancedSearchView extends MaterialSearchView {
    private View searchLayout;
    private View searchHintView;
    private View searchForView;
    private TextView searchForVideo;
    private TextView searchForNear;
    private TextView searchForUntil;
    private View mTintView;
    private LinearLayout searchTopBarContainer;
    private RelativeLayout searchTopBar;
    private ImageView cancelHint;
    private TextView hintText;
    private Context context;
    private SearchClickedListener searchClickedListener;

    public AdvancedSearchView(Context context) {
        super(context);
        init(context);
    }

    public AdvancedSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdvancedSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        searchLayout = findViewById(R.id.search_layout);
        searchTopBar = (RelativeLayout) searchLayout.findViewById(R.id.search_top_bar);
        mTintView = findViewById(R.id.transparent_view);
        mTintView.setBackgroundColor(context.getResources().getColor(R.color.tint_view_color));

        searchTopBarContainer = (LinearLayout) searchTopBar.getParent();
        searchForView = LayoutInflater.from(context).inflate(R.layout.search_options, null);
        searchForVideo = searchForView.findViewById(R.id.saerch_for_video);
        searchForNear = searchForView.findViewById(R.id.saerch_for_near);
        searchForUntil = searchForView.findViewById(R.id.saerch_for_until);
        searchForNear.setOnClickListener(searchForOnClickListener);
        searchForVideo.setOnClickListener(searchForOnClickListener);
        searchForUntil.setOnClickListener(searchForOnClickListener);

        searchForUntil = searchForView.findViewById(R.id.saerch_for_until);
        searchTopBarContainer.addView(searchForView);

        searchHintView = LayoutInflater.from(context).inflate(R.layout.search_view_hint, null);
        hintText = searchHintView.findViewById(R.id.search_hint_text);
        cancelHint = searchHintView.findViewById(R.id.cancel_hint);
        cancelHint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissSearchHintView();
            }
        });

        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.addRule(RelativeLayout.RIGHT_OF, R.id.action_up_btn);
        layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
        searchHintView.setLayoutParams(layoutParams2);
        searchTopBar.addView(searchHintView);
        dismissSearchHintView();

        EditText editText = searchLayout.findViewById(R.id.searchTextView) ;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.search_hint_container);
        editText.setPadding((int)drawingKit.convertDpToPixel(10, context), 0, (int)drawingKit.convertDpToPixel(50, context), 0);
        editText.setLayoutParams(layoutParams);

    }

    public void showSearchHintView(){
        searchHintView.setVisibility(VISIBLE);
    }

    public void dismissSearchHintView(){
        searchHintView.setVisibility(GONE);
    }

    public void setHintText(String text){
        hintText.setText(text);
    }

    private final OnClickListener searchForOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v == searchForVideo) {
                Timber.d("Search for video");
            } else if (v == searchForNear) {
                Timber.d("search for near");
            } else if (v == searchForUntil) {
                Timber.d("search for until");
            }
        }
    };

    @Override
    public void setSuggestions(String[] suggestions){
        super.setSuggestions(suggestions);
        mTintView.setVisibility(GONE);
    }

//    @Override
//    public void setMenuItem(MenuItem menuItem) {
//        super.setMenuItem(menuItem);
//        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                showSearch();
//                searchClickedListener.onSearchClicked();
//                return true;
//            }
//        });
//    }
//
//    public void setSearchClickedListener(SearchClickedListener searchClickedListener) {
//        this.searchClickedListener = searchClickedListener;
//    }
}

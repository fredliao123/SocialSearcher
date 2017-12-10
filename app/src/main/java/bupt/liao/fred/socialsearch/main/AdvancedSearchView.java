package bupt.liao.fred.socialsearch.main;

import android.Manifest;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.kit.PermissionKit;
import bupt.liao.fred.socialsearch.kit.drawingKit;
import bupt.liao.fred.socialsearch.twitter.presenter.TwitterPresenter;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/7.
 * Email:fredliaobupt@qq.com
 * Description:A search view based on MaterialSearchView
 * Added search category and search category hint
 */

public class AdvancedSearchView extends MaterialSearchView {
    //The root view
    private View searchLayout;
    //The container that wraps search top bar. ie.the edittext etc.
    private LinearLayout searchTopBarContainer;
    //Search top bar, has edittext and some icons
    private RelativeLayout searchTopBar;

    //The search category hint view that show on the left side of search top bar
    private View searchHintView;
    //The cancel button in the search hint view
    private ImageView cancelHint;
    //The hint text in search hint view
    private TextView hintText;

    //The search category select view that shows below search top bar
    private SearchForCategoryView searchForCategoryView;
    //The callback to change the visibility of searchForCategoryView
    private ICategoryViewController categoryViewController;

    //The tint view that will show after setSuggestion. Need to be removed in our view.
    private View mTintView;

    private Context context;


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

    //TODO
    private void init(Context context) {
        this.context = context;
        searchLayout = findViewById(R.id.search_layout);
        searchTopBar = searchLayout.findViewById(R.id.search_top_bar);
        mTintView = findViewById(R.id.transparent_view);

        initSearchForCategoryView();
        initSearchHintView();
        adjustEditTextPosition();
    }

    /**
     * Init the search for category select view and add it below the search top bar
     */
    private void initSearchForCategoryView() {
        //Init the container of search top bar and add the search for category select view into it
        searchTopBarContainer = (LinearLayout) searchTopBar.getParent();
        searchForCategoryView = SearchForCategoryView.newInstance(context);
        searchTopBarContainer.addView(searchForCategoryView);
        //click listener for video, near, until feature
        searchForCategoryView.setOnClickListenerToVideo(searchForCategoryOnClickListener);
        searchForCategoryView.setOnClickListenerToNear(searchForCategoryOnClickListener);
        searchForCategoryView.setOnClickListenerToUntil(searchForCategoryOnClickListener);
    }

    /**
     * Init search hint view and add it on the left side of search top bar
     */
    private void initSearchHintView() {
        searchHintView = LayoutInflater.from(context).inflate(R.layout.search_view_hint, null);
        hintText = searchHintView.findViewById(R.id.search_hint_text);
        cancelHint = searchHintView.findViewById(R.id.cancel_hint);
        cancelHint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissSearchHintView();
                categoryViewController.changeCategoryViewShowStatus();
                TwitterPresenter.clearSearchHint();
            }
        });
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.addRule(RelativeLayout.RIGHT_OF, R.id.action_up_btn);
        layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
        searchHintView.setLayoutParams(layoutParams2);
        searchTopBar.addView(searchHintView);
        dismissSearchHintView();
    }

    /**
     * Adjust the position of EditText in search view, to make room for search hint view
     */
    private void adjustEditTextPosition() {
        EditText editText = searchLayout.findViewById(R.id.searchTextView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.search_hint_container);
        editText.setPadding((int) drawingKit.convertDpToPixel(10, context), 0, (int) drawingKit.convertDpToPixel(50, context), 0);
        editText.setLayoutParams(layoutParams);
    }

    /**
     * Methods that deals with search hint view
     */
    public void showSearchHintView() {
        searchHintView.setVisibility(VISIBLE);
    }

    public void dismissSearchHintView() {
        searchHintView.setVisibility(GONE);
    }

    public void setHintText(String text) {
        hintText.setText(text);
    }

    public boolean isHintShown() {
        return (searchHintView.getVisibility() == ViewGroup.VISIBLE);
    }

    /**
     * Methods that deals with search for category select view
     */
    public void showSearchForCategoryView() {
        searchForCategoryView.setVisibility(VISIBLE);
    }

    public void dismissSearchForCategoryView() {
        searchForCategoryView.setVisibility(GONE);
    }

    /**
     * OnClickListener for video, near, until
     */
    private final OnClickListener searchForCategoryOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v.getId() == R.id.saerch_for_video) {
                Timber.d("Search for video");
                setHintText(context.getResources().getString(R.string.search_for_video));
                showSearchHintView();
                TwitterPresenter.searchForVideo();
                categoryViewController.changeCategoryViewShowStatus();
            } else if (v.getId() == R.id.saerch_for_near) {
                Timber.d("search for near");
                setHintText(context.getResources().getString(R.string.search_for_near));
                showSearchHintView();
                TwitterPresenter.searchForNear();
                categoryViewController.changeCategoryViewShowStatus();
                categoryViewController.isLocationPermissionGranted();
            } else if (v.getId() == R.id.saerch_for_until) {
                Timber.d("search for until");
                showTimePicker();
            }
        }
    };

    /**
     * Show the time picker dialog after clicking Until button
     * Because Twitter search Api can only search for tweets in 7 days
     * So, the start day should be 7 days before current time
     */
    private void showTimePicker() {
        Calendar endDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, -7);
        TimePickerView pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                setHintText(context.getResources().getString(R.string.search_for_util));
                showSearchHintView();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                TwitterPresenter.searchForUnitl(format.format(date));
                categoryViewController.changeCategoryViewShowStatus();
            }
        })
                .isDialog(true)
                //only show year, month and day.
                .setType(new boolean[]{true, true, true, false, false, false})
                .setRangDate(startDate, endDate)
                .build();
        pvTime.show();
    }

    /**
     * The only reason for override this method is to set visibility of TintView to GONE
     * Because original setSuggestions will set visibility to VISIBLE but this view will
     * conflict with searchForCategoryView
     *
     * @param suggestions
     */
    @Override
    public void setSuggestions(String[] suggestions) {
        super.setSuggestions(suggestions);
        mTintView.setVisibility(GONE);
    }


    public void setCategoryViewController(ICategoryViewController categoryViewController) {
        this.categoryViewController = categoryViewController;
    }
}

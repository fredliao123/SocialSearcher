package bupt.liao.fred.socialsearch.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.kit.KnifeKit;
import butterknife.BindView;

/**
 * Created by Fred.Liao on 2017/12/8.
 * Email:fredliaobupt@qq.com
 * Description:Search for category selection view. ie, Video, Until, Near
 */

public class SearchForCategoryView extends RelativeLayout {
    private Context context;
    SearchForCategoryView searchForHintView;

    @BindView(R.id.saerch_for_video)
    TextView searchForVideo;

    @BindView(R.id.saerch_for_near)
    TextView searchForNear;

    @BindView(R.id.saerch_for_until)
    TextView searchForUntil;

    public static SearchForCategoryView newInstance(Context context){
        return new SearchForCategoryView(context);
    }

    public SearchForCategoryView(Context context) {
        super(context);
        init(context);
    }

    public SearchForCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchForCategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.search_options, this);
        KnifeKit.bind(this);
    }

    public void setOnClickListenerToVideo(OnClickListener listener){
        searchForVideo.setOnClickListener(listener);
    }

    public void setOnClickListenerToNear(OnClickListener listener){
        searchForNear.setOnClickListener(listener);
    }

    public void setOnClickListenerToUntil(OnClickListener listener){
        searchForUntil.setOnClickListener(listener);
    }
}

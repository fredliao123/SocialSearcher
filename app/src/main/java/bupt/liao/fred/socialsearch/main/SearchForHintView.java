package bupt.liao.fred.socialsearch.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bupt.liao.fred.socialsearch.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/8.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class SearchForHintView extends RelativeLayout {
    private Context context;
    SearchForHintView searchForHintView;

    @BindView(R.id.saerch_for_video)
    TextView searchForVideo;

    @BindView(R.id.saerch_for_near)
    TextView searchForNear;

    @BindView(R.id.saerch_for_until)
    TextView searchForUntil;

    @OnClick(R.id.saerch_for_video)
    void clickForVideo(){
        Timber.d("Search for video");
    }

    public SearchForHintView(Context context) {
        super(context);
    }

    public SearchForHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchForHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.search_options, this);
        ButterKnife.bind(this);

    }
}

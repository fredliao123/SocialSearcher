package bupt.liao.fred.socialsearch.app.rxbus;

/**
 * Created by Fred.Liao on 2018/1/2.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class CategoryEvent implements IEvent{
    public static final int TAG_DEFAULT = -1;
    public static final int TAG_VIDEO = 0;
    public static final int TAG_NEAR = 1;
    public static final int TAG_UNTIL = 2;
    public static final int TAG_CLEAR = 3;

    private int tag = TAG_DEFAULT;
    private String date = null;

    public CategoryEvent(int tag){
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getTag() {
        return tag;
    }
}

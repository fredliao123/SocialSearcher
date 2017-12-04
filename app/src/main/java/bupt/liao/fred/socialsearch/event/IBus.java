package bupt.liao.fred.socialsearch.event;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public interface IBus {
    void register(Object object);
    void unregister(Object object);
    void post(IEvent event);
    void postSticky(IEvent event);


    interface IEvent {
        int getTag();
    }

}

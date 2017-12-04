package bupt.liao.fred.socialsearch.event;

/**
 * Created by Fred.Liao on 2017/12/4.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class BusProvider {

    private static RxBusImpl bus;

    public static RxBusImpl getBus() {
        if (bus == null) {
            synchronized (BusProvider.class) {
                if (bus == null) {
                    bus = RxBusImpl.get();
                }
            }
        }
        return bus;
    }
}

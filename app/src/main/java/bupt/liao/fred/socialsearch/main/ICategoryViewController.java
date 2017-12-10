package bupt.liao.fred.socialsearch.main;

/**
 * Created by Fred.Liao on 2017/12/9.
 * Email:fredliaobupt@qq.com
 * Description:Interface for the callback to change
 * the visibility status of search for category select view
 */

public interface ICategoryViewController {
    void changeCategoryViewShowStatus();

    void isLocationPermissionGranted();
}

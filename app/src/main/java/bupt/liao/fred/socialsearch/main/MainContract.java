package bupt.liao.fred.socialsearch.main;

import android.support.v7.app.AppCompatActivity;

import java.util.Set;

/**
 * Created by Fred.Liao on 2017/12/28.
 * Email:fredliaobupt@qq.com
 * Description:
 */
interface MainContract {
    interface MainView{
        void setSuggestions(String[] suggestions);
        void showSuggestions();
    }

    interface MainPresenter{
        void getHistorySet();
        void saveHistorySet();
        void enableLocationPermission(AppCompatActivity appCompatActivity);
        void addQueryToHistorySet(String query);
    }

}

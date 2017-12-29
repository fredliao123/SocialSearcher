package bupt.liao.fred.socialsearch.kit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.web.WebActivity;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description: A kit include string related operations.
 */

public class StringKit {

    @Singleton
    public static StringKit getInstance() {
        return new StringKit();
    }

    /**
     * Extract web url from a string
     *
     * @param text string to be extracted
     * @return a string array contains urls
     */
    public String[] extractLinks(String text) {
        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
            Timber.d("url extracted:" + url);
            links.add(url);
        }
        return links.toArray(new String[links.size()]);
    }

    /**
     * Change the color of the urls in a string and made the url clickable
     *
     * @param context
     * @param urlText     all the urls in the string
     * @param totalString the string to be changed
     * @return
     */
    public Spannable getSpanText(@NonNull Context context, @NonNull String[] urlText, @NonNull String totalString) {
        Spannable spanText = new SpannableString(totalString);
        for (String s : urlText) {
            //Decide the start index and end index of a given url
            int start = totalString.indexOf(s);
            int end = start + s.length();
            //Set color
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getColor(R.color.colorPrimary));
            //Set click event
            UrlClickableSpan urlClickableSpan = new UrlClickableSpan(context, s, start, end);
            //Apply settings to text
            spanText.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanText.setSpan(urlClickableSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        }
        return spanText;
    }

    /**
     * The click event of a url in a string
     */
    class UrlClickableSpan extends ClickableSpan {
        String url = "";
        int start = 0;
        int end = 0;
        Context context;

        /**
         * Constructor
         *
         * @param context
         * @param string  url
         * @param start   start index of url
         * @param end     end index of url
         */
        public UrlClickableSpan(Context context, String string, int start, int end) {
            super();
            url = string;
            this.start = start;
            this.end = end;
            this.context = context;
        }

        /**
         * Click the url will launch a webacivity to handle the url
         *
         * @param tv
         */
        public void onClick(View tv) {
            if (tv instanceof TextView) {
                Timber.d("StringKit: url clicked " + url);
                WebActivity.launch(context, url, "");
            }
        }
    }
}

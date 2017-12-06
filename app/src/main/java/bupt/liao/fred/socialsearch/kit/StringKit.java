package bupt.liao.fred.socialsearch.kit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.inject.Singleton;

import bupt.liao.fred.socialsearch.BaseApplication;
import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.view.ui.WebActivity;
import timber.log.Timber;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class StringKit {

    @Singleton
    public static StringKit getInstance(){
        return new StringKit();
    }

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

    public Spannable getSpanText(@NonNull Context context, @NonNull String[] urlText, @NonNull String totalString) {
        Spannable spanText = new SpannableString(totalString);
        for (String s : urlText) {
            int start = totalString.indexOf(s);
            int end = start + s.length();
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(BaseApplication.getContext().getColor(R.color.colorPrimary));
            UrlClickableSpan urlClickableSpan = new UrlClickableSpan(context, s, start, end);
            spanText.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanText.setSpan(urlClickableSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        }
        return  spanText;
    }

    class UrlClickableSpan extends ClickableSpan {
        String url = "";
        int start = 0;
        int end  = 0;
        Context context;
        public UrlClickableSpan(Context context, String string, int start, int end)
        {
            super();
            url = string;
            this.start = start;
            this.end = end;
            this.context = context;
        }
        public void onClick(View tv)
        {
            if(tv instanceof TextView) {
                Timber.d("StringKit: url clicked " + url);
                WebActivity.launch(context, url ,"");
//                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(BaseApplication.getContext().getColor(R.color.colorAccent));
//                Spannable spanText = (Spannable) ((TextView) tv).getText();
//                spanText.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                ((TextView) tv).setText(spanText);
//                ((TextView)tv).setMovementMethod(LinkMovementMethod.getInstance ());
            }
        }
        public void updateDrawState(TextPaint ds)
        {
            //ds.setColor(BaseApplication.getContext().getColor(R.color.colorAccent));//set text color
            //ds.setUnderlineText(true); // set to false to remove underline
        }
    }
}

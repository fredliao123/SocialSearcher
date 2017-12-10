package bupt.liao.fred.socialsearch.twitter.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.kit.StringKit;
import twitter4j.Status;

/**
 * Created by Fred.Liao on 2017/12/5.
 * Email:fredliaobupt@qq.com
 * Description: Adapter for RecyclerView in TwitterFragment
 */

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder> {
    private static final String LOGIN_FORMAT = "@%s";
    private static final String DATE_TIME_PATTERN = "dd MMM";
    private final Context context;
    private final List<Status> tweets;

    public TwitterAdapter(final Context context, final List<Status> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Status tweet = tweets.get(position);
        //Load head image using Picasso
        Picasso.with(context).load(tweet.getUser().getProfileImageURL()).into(holder.ivAvatar);
        holder.tvName.setText(tweet.getUser().getName());
        final String formattedLogin = String.format(LOGIN_FORMAT, tweet.getUser().getScreenName());
        holder.tvLogin.setText(formattedLogin);
        final DateTime createdAt = new DateTime(tweet.getCreatedAt());
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_TIME_PATTERN);
        holder.tvDate.setText(formatter.print(createdAt));
        //Extract links in content text
        String[] urls = StringKit.getInstance().extractLinks(tweet.getText());
        //Change url color in content text
        holder.tvMessage.setText(StringKit.getInstance().getSpanText(context, urls, tweet.getText()));
        holder.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    /**
     * This method returns last id to load more tweets
     *
     * @return
     */
    public long getLastTweetId() {
        final Status tweet = tweets.get(getItemCount() - 1);
        return tweet.getId();
    }

    public List<Status> getTweets() {
        return Collections.unmodifiableList(tweets);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivAvatar;
        protected TextView tvName;
        protected TextView tvLogin;
        protected TextView tvDate;
        protected TextView tvMessage;

        public ViewHolder(final View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvLogin = (TextView) itemView.findViewById(R.id.tv_login);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}

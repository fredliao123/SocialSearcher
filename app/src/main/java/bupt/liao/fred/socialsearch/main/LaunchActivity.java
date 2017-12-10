package bupt.liao.fred.socialsearch.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import bupt.liao.fred.flipviewlibrary.FlipView;
import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.mvp.view.BaseActivity;

/**
 * Created by Fred.Liao on 2017/12/10.
 * Email:fredliaobupt@qq.com
 * Description: First activity to launch after open the app
 * Using my own animation library FlipView
 */

public class LaunchActivity extends BaseActivity {

    FlipView flipView;

    @Override
    public void initData(Bundle savedInstanceState) {
        flipView = (FlipView) findViewById(R.id.flipview);
        //When the animation is over, jump to MainActivity
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flipView.getAnimatorSet().addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                        LaunchActivity.this.finish();

                    }
                });
                flipView.startFlip();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public Object newP() {
        return null;
    }
}

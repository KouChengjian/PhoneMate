package com.kcj.phonesuperviser.ui;

import java.util.Random;

import com.kcj.phonesuperviser.CleanerService;
import com.kcj.phonesuperviser.CoreService;
import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.SysWidService;
import com.kcj.phonesuperviser.util.SharedPreferencesUtils;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * @ClassName: SplishActivity
 * @Description: 引导界面
 * @author: KouChengjian
 * @date: 5015-5-16 20:27
 */
public class SplishActivity extends BaseActivity {

	private Animation mFadeIn;
    private Animation mFadeInScale;
    private Animation mFadeOut;
    
    private int index;
    private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splish);
        initViews();
        initEvents();
        initDatas();
	}

	@Override
	protected void initViews() {
		mImageView = (ImageView) findViewById(R.id.iv_splash_bg);
		index = new Random().nextInt(2);
		if (index == 1) {
            mImageView.setImageResource(R.drawable.iv_splash_bg_1);
        } else {
            mImageView.setImageResource(R.drawable.iv_splash_bg_2);
        }
		mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
        mFadeInScale = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
        mFadeOut = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_out);
	}

	@Override
	protected void initEvents() {
		mFadeIn.setDuration(500);
		mFadeInScale.setDuration(2000);
		mFadeOut.setDuration(500);
		mImageView.startAnimation(mFadeIn);
		mFadeIn.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {}

            public void onAnimationRepeat(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                mImageView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {}

            public void onAnimationRepeat(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
            	startAnimActivity(MainActivity.class);
                finish();
                // mImageView.startAnimation(mFadeOut);
            }
        });
	}

	@Override
	protected void initDatas() {}
	
	@Override
	protected void onResume() {
		super.onResume();
		startService(new Intent(this, CoreService.class));
		startService(new Intent(this, CleanerService.class));
		startService(new Intent(this, SysWidService.class));
		if (!SharedPreferencesUtils.isShortCut(mContext)) {
         createShortCut();
      }
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 快捷方式
	 */
	public void createShortCut(){
		Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT"); //创建快捷方式的Intent 
        intent.putExtra("duplicate", false); //不允许重复创建    
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "一键加速");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.short_cut_icon));
        Intent i = new Intent();
        i.setAction("com.kcj.shortcut");
        i.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        sendBroadcast(intent);
        SharedPreferencesUtils.setIsShortCut(mContext, true);
	}
}

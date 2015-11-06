package com.kcj.phonesuperviser.ui;

import java.util.Date;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kcj.phonesuperviser.ui.fragment.HomeFragment;
import com.kcj.phonesuperviser.ui.fragment.NavigationDrawerFragment;
import com.kcj.phonesuperviser.ui.fragment.RelaxFragment;
import com.kcj.phonesuperviser.ui.fragment.SettingsFragment;
import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.util.ActivityTack;
import com.kcj.phonesuperviser.util.SystemBarTintManager;
import com.kcj.phonesuperviser.util.UIElementsHelper;
import com.kcj.phonesuperviser.view.ActionBarDrawerToggle;
import com.kcj.phonesuperviser.view.DrawerArrowDrawable;

/**
 * @ClassName: MainActivity
 * @Description: 主界面容器
 * @author: KouChengjian
 * @date: 2015-5-27
 */
public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	protected DrawerLayout mDrawerLayout;
	private View mFragmentContainerView;
	// Drawer action
	private DrawerArrowDrawable drawerArrow;
	private ActionBarDrawerToggle mDrawerToggle;
	// 双击退出
	private long preTime;
	public static final long TWO_SECOND = 2 * 1000;
	// fragment
	private HomeFragment mMainFragment;
	private RelaxFragment mRelaxFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionBar = getActionBar();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager mTintManager = new SystemBarTintManager(this);
			mTintManager.setStatusBarTintEnabled(true);
			mTintManager.setNavigationBarTintEnabled(true);
			mTintManager.setTintDrawable(UIElementsHelper.getGeneralActionBarBackground(this));
			getActionBar().setBackgroundDrawable(UIElementsHelper.getGeneralActionBarBackground(this));
		}
		initViews();
		initEvents();
		initDatas();
	}

	@Override
	protected void initViews() {
		mFragmentContainerView = (View) findViewById(R.id.navigation_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	}

	@Override
	protected void initEvents() {
		actionBar.setDisplayHomeAsUpEnabled(true);// 给home icon的左边加上一个返回的图标
		actionBar.setHomeButtonEnabled(true);// 需要api level 14 使用home-icon 可点击
		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();
	}

	@Override
	protected void initDatas() {
		onNavigationDrawerItemSelected(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
				mDrawerLayout.closeDrawer(mFragmentContainerView);
			} else {
				mDrawerLayout.openDrawer(mFragmentContainerView);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 当前activity完全运行时回调
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/**
	 * 屏幕发生改变
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * 设置透明度
	 */
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 导航
	 */
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);

		switch (position) {
		case 0:
			closeDrawer();
			if (mMainFragment == null) {
				mMainFragment = new HomeFragment();
				transaction.add(R.id.container, mMainFragment);
			} else {
				transaction.show(mMainFragment);
			}
			transaction.commit();
			break;
		case 1:
			closeDrawer();
			if (mRelaxFragment == null) {
				mRelaxFragment = new RelaxFragment();
				transaction.add(R.id.container, mRelaxFragment);
			} else {
				transaction.show(mRelaxFragment);
			}
			transaction.commit();
			break;
		case 2:
			closeDrawer();
			SettingsFragment.launch(MainActivity.this);
			break;
		}
	}

	/**
	 * 隐藏界面
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mMainFragment != null) {
			transaction.hide(mMainFragment);
		}
		if (mRelaxFragment != null) {
			transaction.hide(mRelaxFragment);
		}
	}

	/**
	 * 清除Drawer
	 */
	public void closeDrawer() {
		mDrawerLayout.closeDrawers();
	}

	/**
	 * 退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 截获后退键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long currentTime = new Date().getTime();

			// 如果时间间隔大于2秒, 不处理
			if ((currentTime - preTime) > TWO_SECOND) {
				// 显示消息
				ShowToast("再按一次退出应用程序");
				// 更新时间
				preTime = currentTime;
				// 截获事件,不再处理
				return true;
			} else {
				ActivityTack.getInstanse().exit(mContext);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}

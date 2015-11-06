package com.kcj.phonesuperviser.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import butterknife.InjectView;

import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.adapter.WeakFragmentPagerAdapter;
import com.kcj.phonesuperviser.ui.fragment.AutoStartFragment;
import com.kcj.phonesuperviser.view.SlidingTab;


/**
 * @ClassName: AutoStartManageActivity
 * @Description: 自启管理
 * @author: KouChengjian
 * @date: 5015-5-23
 */
public class AutoStartManageActivity extends BaseSwipeBackActivity{

//	protected ActionBar ab;
//	protected Resources res;
    @InjectView(R.id.tabs)
    protected SlidingTab tabs;
    @InjectView(R.id.pagerFragmentTask)
    protected ViewPager pager;
    private MyPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autostart);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		initViews();
		initEvents();
		initDatas();
	}
	
	@Override
	protected void initViews() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
	}

	@Override
	protected void initEvents() {}

	@Override
	protected void initDatas() {
		pager.setAdapter(adapter);
		int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources() .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setTextColor(Color.parseColor("#ffffff"));
        tabs.setIndicatorColor(Color.parseColor("#ffffff"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ffffff"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	
	
	
	public class MyPagerAdapter extends WeakFragmentPagerAdapter {

        private final String[] TITLES = {"普通软件", "系统软件"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            AutoStartFragment fragment = new AutoStartFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            saveFragment(fragment);
            return fragment;
        }
    }
}

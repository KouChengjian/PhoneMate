package com.kcj.phonesuperviser.ui;


import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.kcj.phonesuperviser.CleanerService;
import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.adapter.RublishMemoryAdapter;
import com.kcj.phonesuperviser.bean.CacheListItem;
import com.kcj.phonesuperviser.bean.StorageSize;
import com.kcj.phonesuperviser.util.StorageUtil;
import com.kcj.phonesuperviser.widget.textcounter.CounterView;
import com.kcj.phonesuperviser.widget.textcounter.DecimalFormatter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: RubbishCleanActivity
 * @Description: 垃圾清理
 * @author:
 * @date:
 */
public class RubbishCleanActivity extends BaseSwipeBackActivity implements OnDismissCallback, CleanerService.OnActionListener{

	@InjectView(R.id.listview)
	protected ListView mListView;
    @InjectView(R.id.empty)
    protected TextView mEmptyView;
    @InjectView(R.id.header)
    protected RelativeLayout header;
    @InjectView(R.id.textCounter)
    protected CounterView textCounter;
    @InjectView(R.id.sufix)
    protected TextView sufix;
    @InjectView(R.id.progressBar)
    protected View mProgressBar;
    @InjectView(R.id.progressBarText)
    protected TextView mProgressBarText;
    @InjectView(R.id.bottom_lin)
    protected LinearLayout bottom_lin;
    @InjectView(R.id.clear_button)
    protected Button clearButton;
	
	protected static final int SCANING = 5;
    protected static final int SCAN_FINIFSH = 6;
    protected static final int PROCESS_MAX = 8;
    protected static final int PROCESS_PROCESS = 9;
    protected static final int INITIAL_DELAY_MILLIS = 300;
    
//    private Resources res;
//    private int ptotal = 0;
//    private int pprocess = 0;
    private boolean mAlreadyScanned = false;
    private boolean mAlreadyCleaned = false;
    private CleanerService mCleanerService;
    private RublishMemoryAdapter rublishMemoryAdapter;
    private List<CacheListItem> mCacheListItem = new ArrayList<>();
//    private SwingBottomInAnimationAdapter swingBottomInAnimationAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rubbishclean);
		getActionBar().setDisplayHomeAsUpEnabled(true);
//        res = getResources();
		initViews();
		initEvents();
		initDatas();
	}

	@Override
	protected void initViews() {
		rublishMemoryAdapter = new RublishMemoryAdapter(mContext, mCacheListItem);
	}

	@Override
	protected void initEvents() {
		int footerHeight = mContext.getResources().getDimensionPixelSize(R.dimen.footer_height);
		mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(rublishMemoryAdapter);
        mListView.setOnItemClickListener(rublishMemoryAdapter);
        mListView.setOnScrollListener(new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, bottom_lin, footerHeight));
	}

	@Override
	protected void initDatas() {
		bindService(new Intent(mContext, CleanerService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCleanerService = ((CleanerService.CleanerServiceBinder) service).getService();
            mCleanerService.setOnActionListener(RubbishCleanActivity.this);

            if (!mCleanerService.isScanning() && !mAlreadyScanned) {
                mCleanerService.scanCache();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCleanerService.setOnActionListener(null);
            mCleanerService = null;
        }
    };
	
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
		unbindService(mServiceConnection);
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
	
	@OnClick(R.id.clear_button)
    public void onClickClear() {
        if (mCleanerService != null && !mCleanerService.isScanning() &&
                !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
            mAlreadyCleaned = false;

            mCleanerService.cleanCache();
        }
    }

	@Override
	public void onScanStarted(Context context) {
		mProgressBarText.setText(R.string.scanning);
        showProgressBar(true);
	}

	@Override
	public void onScanProgressUpdated(Context context, int current, int max) {
		mProgressBarText.setText(getString(R.string.scanning_m_of_n, current, max));
	}

	@Override
	public void onScanCompleted(Context context, List<CacheListItem> apps) {
		showProgressBar(false);
        mCacheListItem.clear();
        mCacheListItem.addAll(apps);
        rublishMemoryAdapter.notifyDataSetChanged();
        header.setVisibility(View.GONE);
        if (apps.size() > 0) {
            header.setVisibility(View.VISIBLE);
            bottom_lin.setVisibility(View.VISIBLE);

            long medMemory = mCleanerService != null ? mCleanerService.getCacheSize() : 0;

            StorageSize mStorageSize = StorageUtil.convertStorageSize(medMemory);
            textCounter.setAutoFormat(false);
            textCounter.setFormatter(new DecimalFormatter());
            textCounter.setAutoStart(false);
            textCounter.setStartValue(0f);
            textCounter.setEndValue(mStorageSize.value);
            textCounter.setIncrement(5f); // the amount the number increments at each time interval
            textCounter.setTimeInterval(50); // the time interval (ms) at which the text changes
            sufix.setText(mStorageSize.suffix);
            //  textCounter.setSuffix(mStorageSize.suffix);
            textCounter.start();
        } else {
            header.setVisibility(View.GONE);
            bottom_lin.setVisibility(View.GONE);
        }

        if (!mAlreadyScanned) {
            mAlreadyScanned = true;

        }
	}

	@Override
	public void onCleanStarted(Context context) {
		if (isProgressBarVisible()) {
            showProgressBar(false);
        }

        if (!RubbishCleanActivity.this.isFinishing()) {
            showDialogLoading();
        }
	}

	@Override
	public void onCleanCompleted(Context context, long cacheSize) {
		dismissDialogLoading();
        Toast.makeText(context, context.getString(R.string.cleaned, Formatter.formatShortFileSize(
                mContext, cacheSize)), Toast.LENGTH_LONG).show();
        header.setVisibility(View.GONE);
        bottom_lin.setVisibility(View.GONE);
        mCacheListItem.clear();
        rublishMemoryAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDismiss(ViewGroup listView, int[] reverseSortedPositions) {}
	
	
	
	
	private boolean isProgressBarVisible() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }
	
	private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
            mProgressBar.setVisibility(View.GONE);
        }
    }

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// 截获后退键
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			scrollToFinishActivity();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}

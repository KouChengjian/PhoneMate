package com.kcj.phonesuperviser.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import butterknife.InjectView;
import butterknife.OnClick;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.kcj.phonesuperviser.CoreService;
import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.adapter.ClearMemoryAdapter;
import com.kcj.phonesuperviser.bean.AppProcessInfo;
import com.kcj.phonesuperviser.bean.StorageSize;
import com.kcj.phonesuperviser.util.ActivityTack;
import com.kcj.phonesuperviser.util.StorageUtil;
import com.kcj.phonesuperviser.widget.textcounter.CounterView;
import com.kcj.phonesuperviser.widget.textcounter.DecimalFormatter;
import com.kcj.phonesuperviser.widget.waveview.WaveView;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

/**
 * @ClassName: MemoryCleanActivity
 * @Description: 内存清理
 * @author: KouChengjian
 * @date: 
 */
public class MemoryCleanActivity extends BaseSwipeBackActivity implements OnDismissCallback, CoreService.OnPeocessActionListener {

    @InjectView(R.id.listview)
    protected ListView mListView;
    @InjectView(R.id.wave_view)
    protected WaveView mwaveView;
    @InjectView(R.id.header)
    protected RelativeLayout header;
    private List<AppProcessInfo> mAppProcessInfos = new ArrayList<>();
    private ClearMemoryAdapter mClearMemoryAdapter;
    @InjectView(R.id.textCounter)
    protected CounterView textCounter;
    @InjectView(R.id.sufix)
    protected TextView sufix;
    public long Allmemory;
    @InjectView(R.id.bottom_lin)
    protected LinearLayout bottom_lin;
    @InjectView(R.id.progressBar)
    protected View mProgressBar;
    @InjectView(R.id.progressBarText)
    protected TextView mProgressBarText;
    @InjectView(R.id.clear_button)
    protected Button clearButton;
//    private static final int INITIAL_DELAY_MILLIS = 300;
    protected SwingBottomInAnimationAdapter swingBottomInAnimationAdapter;
    private CoreService mCoreService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memoryclean);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		initViews();
		initEvents();
		initDatas();
	}

	@Override
	protected void initViews() {
        mClearMemoryAdapter = new ClearMemoryAdapter(mContext, mAppProcessInfos);
	}

	@Override
	protected void initEvents() {
		textCounter.setAutoFormat(false);
        textCounter.setFormatter(new DecimalFormatter());
        textCounter.setAutoStart(false);
        textCounter.setIncrement(5f); // the amount the number increments at each time interval
        textCounter.setTimeInterval(50); // the time interval (ms) at which the text changes
	}

	@Override
	protected void initDatas() {
		mListView.setAdapter(mClearMemoryAdapter);
		int footerHeight = mContext.getResources().getDimensionPixelSize(R.dimen.footer_height);
	    mListView.setOnScrollListener(new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, bottom_lin, footerHeight));
		bindService(new Intent(mContext, CoreService.class),mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCoreService = ((CoreService.ProcessServiceBinder) service).getService();
            mCoreService.setOnActionListener(MemoryCleanActivity.this);
            mCoreService.scanRunProcess();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCoreService.setOnActionListener(null);
            mCoreService = null;
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
        long killAppmemory = 0;
        for (int i = mAppProcessInfos.size() - 1; i >= 0; i--) {
            if (mAppProcessInfos.get(i).checked) {
                killAppmemory += mAppProcessInfos.get(i).memory;
                mCoreService.killBackgroundProcesses(mAppProcessInfos.get(i).processName);
                mAppProcessInfos.remove(mAppProcessInfos.get(i));
                mClearMemoryAdapter.notifyDataSetChanged();
            }
        }
        Allmemory = Allmemory - killAppmemory;
        ShowToast("共清理" + StorageUtil.convertStorage(killAppmemory) + "内存");
        if (Allmemory > 0) {
            refeshTextCounter();
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
	public void onScanCompleted(Context context, List<AppProcessInfo> apps) {
		mAppProcessInfos.clear();

        Allmemory = 0;
        for (AppProcessInfo appInfo : apps) {
            if (!appInfo.isSystem) {
                mAppProcessInfos.add(appInfo);
                Allmemory += appInfo.memory;
            }
        }
        refeshTextCounter();
        mClearMemoryAdapter.notifyDataSetChanged();
        showProgressBar(false);
        if (apps.size() > 0) {
            header.setVisibility(View.VISIBLE);
            bottom_lin.setVisibility(View.VISIBLE);
        } else {
            header.setVisibility(View.GONE);
            bottom_lin.setVisibility(View.GONE);
        }
	}

	@Override
	public void onCleanStarted(Context context) {}

	@Override
	public void onCleanCompleted(Context context, long cacheSize) {}

	@Override
	public void onDismiss(ViewGroup listView, int[] reverseSortedPositions) {}

	private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
            mProgressBar.setVisibility(View.GONE);
        }
    }
	
	private void refeshTextCounter() {
        mwaveView.setProgress(20);
        StorageSize mStorageSize = StorageUtil.convertStorageSize(Allmemory);
        textCounter.setStartValue(0f);
        textCounter.setEndValue(mStorageSize.value);
        sufix.setText(mStorageSize.suffix);
        textCounter.start();
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

package com.kcj.phonesuperviser.ui.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.bean.SDCardInfo;
import com.kcj.phonesuperviser.ui.AutoStartManageActivity;
import com.kcj.phonesuperviser.ui.MemoryCleanActivity;
import com.kcj.phonesuperviser.ui.RubbishCleanActivity;
import com.kcj.phonesuperviser.ui.SoftwareManageActivity;
import com.kcj.phonesuperviser.util.AppUtil;
import com.kcj.phonesuperviser.util.StorageUtil;
import com.kcj.phonesuperviser.widget.circleprogress.ArcProgress;

/**
 * @ClassName: HomeFragment
 * @Description: 
 * @author: 
 * @date: 
 */
public class HomeFragment extends BaseFragment {

	@InjectView(R.id.arc_store)
	protected ArcProgress arcStore;
    @InjectView(R.id.arc_process)
    protected ArcProgress arcProcess;
    @InjectView(R.id.capacity)
    protected TextView capacity;
    private Timer timer;
    private Timer timer2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.fragment_home, container, false);
		 ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initEvents();
		initDatas();
	}

	@Override
	protected void initViews() {}

	@Override
	protected void initEvents() {}

	@Override
	protected void initDatas() {
		timer = new Timer();
	    timer2 = new Timer();
	    /** 获取内测 */
	    long l = AppUtil.getAvailMemory(mContext); // 获取可用内存
        long y = AppUtil.getTotalMemory(mContext); // 总内存
        final double x = (((y - l) / (double) y) * 100);
        arcProcess.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (arcProcess.getProgress() >= (int) x) {
                            timer.cancel();
                        } else {
                            arcProcess.setProgress(arcProcess.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);
        /** 获取sd */
        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(mContext);
        long nAvailaBlock;
        long TotalBlocks;
        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }
        final double percentStore = (((TotalBlocks - nAvailaBlock) / (double) TotalBlocks) * 100);
        capacity.setText(StorageUtil.convertStorage(TotalBlocks - nAvailaBlock) + "/" + StorageUtil.convertStorage(TotalBlocks));
        arcStore.setProgress(0);
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcStore.getProgress() >= (int) percentStore) {
                            timer2.cancel();
                        } else {
                            arcStore.setProgress(arcStore.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		initDatas();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		ButterKnife.reset(this);
		timer.cancel();
        timer2.cancel();
		super.onDestroy();
	}
	
	@OnClick(R.id.arc_process)
	protected void arcProgressMemorydUp() {
        startAnimActivity(MemoryCleanActivity.class);
    }

    @OnClick(R.id.arc_store)
    protected void arcProgressRubbishClean() {
        startAnimActivity(RubbishCleanActivity.class);
    }
	
	@OnClick(R.id.card1)
	protected void memorydUp() {
        startAnimActivity(MemoryCleanActivity.class);
    }

    @OnClick(R.id.card2)
    protected void rubbishClean() {
        startAnimActivity(RubbishCleanActivity.class);
    }

    @OnClick(R.id.card3)
    protected void AutoStartManage() {
        startAnimActivity(AutoStartManageActivity.class);
    }

    @OnClick(R.id.card4)
    protected void SoftwareManage() {
        startAnimActivity(SoftwareManageActivity.class);
    }
	
}

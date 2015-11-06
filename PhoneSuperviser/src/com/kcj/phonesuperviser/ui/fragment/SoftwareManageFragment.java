package com.kcj.phonesuperviser.ui.fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.adapter.SoftwareAdapter;
import com.kcj.phonesuperviser.bean.AppInfo;
import com.kcj.phonesuperviser.util.StorageUtil;


/**
 * @ClassName: SoftwareManageFragment
 * @Description: 软件管理
 * @author: 
 * @date: 
 */
public class SoftwareManageFragment extends BaseFragment{

	public static final int REFRESH_BT = 111;
    private static final String ARG_POSITION = "position";
    private int position; // 0:应用软件，2 系统软件
    private SoftwareAdapter mAutoStartAdapter;
    private List<AppInfo> userAppInfos = null;
    private List<AppInfo> systemAppInfos = null;
    private Method mGetPackageSizeInfoMethod;
    private AsyncTask<Void, Integer, List<AppInfo>> task;
    @InjectView(R.id.listview)
    protected ListView listview;
    @InjectView(R.id.topText)
    protected TextView topText;
    @InjectView(R.id.progressBar)
    protected View mProgressBar;
    @InjectView(R.id.progressBarText)
    protected TextView mProgressBarText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_autostart, container, false);
		ButterKnife.inject(this, view);
	    return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
		initViews();
		initEvents();
		initDatas();
	}
	
	@Override
	protected void initViews() {
		mContext = getActivity();
        try {
            mGetPackageSizeInfoMethod = mContext.getPackageManager().getClass().getMethod(
                    "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
	}

	@Override
	protected void initEvents() {}

	@Override
	protected void initDatas() {
		if (position == 0) {
            topText.setText("");
        } else {
            topText.setText("卸载下列软件，会影响正常使用");
        }
        task = new AsyncTask<Void, Integer, List<AppInfo>>() {
            private int mAppCount = 0;

            @Override
            protected List<AppInfo> doInBackground(Void... params) {
                PackageManager pm = mContext.getPackageManager();
                List<PackageInfo> packInfos = pm.getInstalledPackages(0);
                publishProgress(0, packInfos.size());
                List<AppInfo> appinfos = new ArrayList<AppInfo>();
                for (PackageInfo packInfo : packInfos) {
                    publishProgress(++mAppCount, packInfos.size());
                    final AppInfo appInfo = new AppInfo();
                    Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
                    appInfo.setAppIcon(appIcon);

                    int flags = packInfo.applicationInfo.flags;

                    int uid = packInfo.applicationInfo.uid;

                    appInfo.setUid(uid);

                    if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        appInfo.setUserApp(false);//系统应用
                    } else {
                        appInfo.setUserApp(true);//用户应用
                    }
                    if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                        appInfo.setInRom(false);
                    } else {
                        appInfo.setInRom(true);
                    }
                    String appName = packInfo.applicationInfo.loadLabel(pm).toString();
                    appInfo.setAppName(appName);
                    String packname = packInfo.packageName;
                    appInfo.setPackname(packname);
                    String version = packInfo.versionName;
                    appInfo.setVersion(version);
                    try {
                        mGetPackageSizeInfoMethod.invoke(mContext.getPackageManager(), new Object[]{
                                packname,
                                new IPackageStatsObserver.Stub() {
                                    @Override
                                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                        synchronized (appInfo) {
                                            appInfo.setPkgSize(pStats.cacheSize + pStats.codeSize + pStats.dataSize);

                                        }
                                    }
                                }
                        });
                    } catch (Exception e) {
                    }

                    appinfos.add(appInfo);
                }
                return appinfos;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                try {
                    mProgressBarText.setText(getString(R.string.scanning_m_of_n, values[0], values[1]));
                } catch (Exception e) {

                }
            }

            @Override
            protected void onPreExecute() {
                try {
                    showProgressBar(true);
                    mProgressBarText.setText(R.string.scanning);
                } catch (Exception e) {
                }
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(List<AppInfo> result) {
                super.onPostExecute(result);
                try {
                    showProgressBar(false);
                    userAppInfos = new ArrayList<>();
                    systemAppInfos = new ArrayList<>();
                    long allSize = 0;
                    for (AppInfo a : result) {
                        if (a.isUserApp()) {
                            allSize += a.getPkgSize();
                            userAppInfos.add(a);
                        } else {
                            systemAppInfos.add(a);
                        }
                    }
                    if (position == 0) {
                        topText.setText(getString(R.string.software_top_text, userAppInfos.size(), StorageUtil.convertStorage(allSize)));
                        mAutoStartAdapter = new SoftwareAdapter(mContext, userAppInfos);
                        listview.setAdapter(mAutoStartAdapter);

                    } else {

                        mAutoStartAdapter = new SoftwareAdapter(mContext, systemAppInfos);
                        listview.setAdapter(mAutoStartAdapter);

                    }
                } catch (Exception e) {

                }
            }

        };
        task.execute();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		task.cancel(true);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		ButterKnife.reset(this);
		super.onDestroy();
	}
	
	private boolean isProgressBarVisible() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.startAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.fade_out));
            mProgressBar.setVisibility(View.GONE);
        }
    }
	
}

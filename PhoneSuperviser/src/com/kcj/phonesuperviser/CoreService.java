package com.kcj.phonesuperviser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.kcj.phonesuperviser.bean.AppProcessInfo;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.Toast;

/**
 * @ClassName: CoreService
 * @Description: 清理内存服务
 * @author:
 * @date:
 */
public class CoreService extends Service {

	private static final String TAG = "CleanerService";
	public static final String ACTION_CLEAN_AND_EXIT = "com.yzy.service.cleaner.CLEAN_AND_EXIT";
	private OnPeocessActionListener mOnActionListener;
	private boolean mIsScanning = false;
	private boolean mIsCleaning = false;
	private ActivityManager activityManager = null;
	private List<AppProcessInfo> list = null;
	private PackageManager packageManager = null;
	private Context mContext ;

	@Override
	public void onCreate() {
		// super.onCreate();
		mContext = getApplicationContext();
		try {
			activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			packageManager = getApplicationContext().getPackageManager();
		} catch (Exception e) {}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		if (action != null) {
			if (action.equals(ACTION_CLEAN_AND_EXIT)) {
				setOnActionListener(new OnPeocessActionListener() {
					@Override
					public void onScanStarted(Context context) {}

					@Override
					public void onScanProgressUpdated(Context context,int current, int max) {}

					@Override
					public void onScanCompleted(Context context,List<AppProcessInfo> apps) {}

					@Override
					public void onCleanStarted(Context context) {}

					@Override
					public void onCleanCompleted(Context context, long cacheSize) {
						String msg = getString(R.string.cleaned,Formatter.formatShortFileSize(CoreService.this,	cacheSize));
						Toast.makeText(CoreService.this, msg, Toast.LENGTH_LONG).show();
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								stopSelf();
							}
						}, 5000);
					}
				});
				// 扫描运行进度
				scanRunProcess();
			}
		}
		return START_NOT_STICKY; // START_NOT_STICKY START_STICKY
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private ProcessServiceBinder mBinder = new ProcessServiceBinder();
	
	public class ProcessServiceBinder extends Binder {

        public CoreService getService() {
            return CoreService.this;
        }
    }
	
	public void cleanAllProcess() {
		new TaskClean().execute();
    }
	
	public void scanRunProcess() {
		new TaskScan().execute();
	}
	

	private class TaskScan extends
			AsyncTask<Void, Integer, List<AppProcessInfo>> {

		private int mAppCount = 0;

		@Override
		protected void onPreExecute() {
			if (mOnActionListener != null) {
				mOnActionListener.onScanStarted(CoreService.this);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (mOnActionListener != null) {
				mOnActionListener.onScanProgressUpdated(CoreService.this,
						values[0], values[1]);
			}
		}

		@Override
		protected void onPostExecute(List<AppProcessInfo> result) {
			if (mOnActionListener != null) {
				mOnActionListener.onScanCompleted(CoreService.this, result);
			}
			mIsScanning = false;
		}

		@Override
		protected List<AppProcessInfo> doInBackground(Void... params) {
			list = new ArrayList<AppProcessInfo>();
			ApplicationInfo appInfo = null;
			AppProcessInfo abAppProcessInfo = null;
			List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager
					.getRunningAppProcesses();
			publishProgress(0, appProcessList.size());
			for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
				publishProgress(++mAppCount, appProcessList.size());
				abAppProcessInfo = new AppProcessInfo(
						appProcessInfo.processName, appProcessInfo.pid,
						appProcessInfo.uid);
				try {
					appInfo = packageManager.getApplicationInfo(
							appProcessInfo.processName, 0);
					if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
						abAppProcessInfo.isSystem = true;
					} else {
						abAppProcessInfo.isSystem = false;
					}
					Drawable icon = appInfo.loadIcon(packageManager);
					String appName = appInfo.loadLabel(packageManager)
							.toString();
					abAppProcessInfo.icon = icon;
					abAppProcessInfo.appName = appName;
				} catch (PackageManager.NameNotFoundException e) {
					if (appProcessInfo.processName.indexOf(":") != -1) {
						appInfo = getApplicationInfo(appProcessInfo.processName
								.split(":")[0]);
						if (appInfo != null) {
							Drawable icon = appInfo.loadIcon(packageManager);
							abAppProcessInfo.icon = icon;
						} else {
							abAppProcessInfo.icon = mContext.getResources()
									.getDrawable(R.drawable.ic_launcher);
						}

					} else {
						abAppProcessInfo.icon = mContext.getResources()
								.getDrawable(R.drawable.ic_launcher);
					}
					abAppProcessInfo.isSystem = true;
					abAppProcessInfo.appName = appProcessInfo.processName;
				}
				long memsize = activityManager
						.getProcessMemoryInfo(new int[] { appProcessInfo.pid })[0]
						.getTotalPrivateDirty() * 1024;
				abAppProcessInfo.memory = memsize;
				list.add(abAppProcessInfo);
			}
			return list;
		}
	}
	
	private class TaskClean extends AsyncTask<Void, Void, Long> {
		
		@Override
        protected void onPreExecute() {
            if (mOnActionListener != null) {
                mOnActionListener.onCleanStarted(CoreService.this);
            }
        }
		
		@Override
        protected void onPostExecute(Long result) {
            if (mOnActionListener != null) {
                mOnActionListener.onCleanCompleted(CoreService.this, result);
            }
        }
		
		@Override
        protected Long doInBackground(Void... params) {
            long beforeMemory = 0;
            long endMemory = 0;
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            beforeMemory = memoryInfo.availMem;
            List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : appProcessList) {
                killBackgroundProcesses(info.processName);
            }
            activityManager.getMemoryInfo(memoryInfo);
            endMemory = memoryInfo.availMem;
            return endMemory - beforeMemory;
        }
	}
	
	public void killBackgroundProcesses(String processName) {
        String packageName = null;
        try {
            if (processName.indexOf(":") == -1) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            activityManager.killBackgroundProcesses(packageName);

            Method forceStopPackage = activityManager.getClass()
                    .getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	public ApplicationInfo getApplicationInfo(String processName) {
		if (processName == null) {
			return null;
		}
		List<ApplicationInfo> appList = packageManager
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (ApplicationInfo appInfo : appList) {
			if (processName.equals(appInfo.processName)) {
				return appInfo;
			}
		}
		return null;
	}

	public void setOnActionListener(OnPeocessActionListener listener) {
		mOnActionListener = listener;
	}

	public static interface OnPeocessActionListener {
		public void onScanStarted(Context context);

		public void onScanProgressUpdated(Context context, int current, int max);

		public void onScanCompleted(Context context, List<AppProcessInfo> apps);

		public void onCleanStarted(Context context);

		public void onCleanCompleted(Context context, long cacheSize);
	}
}

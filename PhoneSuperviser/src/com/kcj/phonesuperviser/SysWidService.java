package com.kcj.phonesuperviser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

/**
 * @ClassName: SysWidService
 * @Description: 浮窗
 * @author:
 * @date:
 */
public class SysWidService extends Service{

	private Timer timer;
	private Handler handler = new Handler();
	private AlarmManager mAlarmManager = null;
	private PendingIntent mPendingIntent = null;
	
	@Override
	public void onCreate() {
		Intent intent = new Intent(getApplicationContext(), SysWidService.class);
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		mPendingIntent = PendingIntent.getService(this, 0, intent,Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 60000,mPendingIntent);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 开启定时器，每隔0.5秒刷新一次
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return START_STICKY;	 //START_STICKY  super.onStartCommand(intent, flags, startId)
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer = null;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	class RefreshTask extends TimerTask {
		
		@Override
		public void run() {
			// 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
//			if ((isHome()||isCurrentAppTop()) && !MyWindowManager.isWindowShowing()) {
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//									
//					}
//				});
//			}
		}
	}

	/**
	 * 判断手机当前界面是否是该程序
	 */
	private boolean isCurrentAppTop(){
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return rti.get(0).topActivity.getPackageName().equals(getPackageName());
	}
	
	/**
	 * 判断当前界面是否是桌面
	 */
	private boolean isHome() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return getHomes().contains(rti.get(0).topActivity.getPackageName());
	}
	
	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
}

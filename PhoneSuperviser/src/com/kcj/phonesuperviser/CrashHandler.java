package com.kcj.phonesuperviser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


/**
 * @ClassName: CrashHandler
 * @Description: 全局异常捕获
 * @author: 
 * @date: 
 */
public class CrashHandler implements UncaughtExceptionHandler{

	public static final String TAG = "CrashHandler";
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private int userId;
	private Context mContext;
	
	/**
	 * 初始化
	 */
	public CrashHandler(Context mContext ,int userId) {
		this.mContext = mContext;
		this.userId = userId;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (ex == null && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			Log.e("CrashHandler", "error", ex);
			// 使用Toast来显示异常信息
			new Thread() {
				@Override
				public void run() {
					Looper.prepare();
					Toast toast = Toast.makeText(mContext, "少侠！很抱歉,程序出现异常,即将退出",
							Toast.LENGTH_LONG);
					toast.setGravity(
							Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 200);
					toast.show();
					Looper.loop();
				}
			}.start();
			uploadCrash(ex);
		}
	}
	
	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 */
	public void uploadCrash(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				sb.append("userid=" + userId + "\n");
				sb.append("versionName=" + versionName + "\n");
				sb.append("versionCode=" + versionCode + "\n");
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
	}

}

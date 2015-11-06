package com.kcj.phonesuperviser;


import android.app.Application;

/**
 * @ClassName: 
 * @Description: 
 * @author: 
 * @date: 
 */
public class MyApplication extends Application{

	public static MyApplication mInstance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		init();
	}
	
	public void init(){
//		new CrashHandler(this,999999); // 全局异常捕获
	}
	
	public static MyApplication getInstance() {
		return mInstance;
	}
	
}

package com.kcj.phonesuperviser.ui;

import butterknife.ButterKnife;

import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.util.ActivityTack;
import com.kcj.phonesuperviser.view.ProgressDialogView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;





/**
 * @ClassName: BaseActivity
 * @Description: 基类
 * @author: KouChengjian
 * @date: 5015-5-16 20:27
 */
abstract public class BaseActivity extends FragmentActivity{

	protected ActionBar actionBar;
	protected Toast mToast;
	protected Context mContext;
	protected String TAG; // 打印的名称
	protected ProgressDialogView mProgressDialogFragment;
	protected ActivityTack tack = ActivityTack.getInstanse();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		actionBar = getActionBar();  
		TAG = this.getClass().getSimpleName();
		tack.addActivity(this);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.inject(this);
	}
	
	@Override
	public void finish() {
		super.finish();
		tack.removeActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		tack.removeActivity(this);
		super.onDestroy();
	}
	
	protected abstract void initViews();
	
	protected abstract void initEvents();
	
	protected abstract void initDatas();
	
	public void setActionBarHide(){
		actionBar.hide();  
	}
	public void setActionBarShow(){
		actionBar.show(); 
	}
	
	public void startAnimActivity(Class<?> cla) {
		this.startActivity(new Intent(this, cla));
	}

	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}
	
	/** 含有Bundle通过Class跳转界面 **/
	protected void startAnimActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});

		}
	}

	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(
							BaseActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}
	
	
	public void showDialogLoading() {
		showDialogLoading(null);
	}

	public void showDialogLoading(String msg) {
		if (mProgressDialogFragment == null) {
			mProgressDialogFragment = ProgressDialogView.newInstance(0,
					null);
		}
		if (msg != null) {
			mProgressDialogFragment.setMessage(msg);
		}
		mProgressDialogFragment.show(getFragmentManager(), "basedialog");
	}

	public void dismissDialogLoading() {
		if (mProgressDialogFragment != null) {
			mProgressDialogFragment.dismiss();
		}
	}
}
